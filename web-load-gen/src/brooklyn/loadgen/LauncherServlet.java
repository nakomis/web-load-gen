package brooklyn.loadgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yahoo.ycsb.Client;

public class LauncherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	LoadgenRunner runner;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		
		if("/start".equals(req.getPathInfo())) {
			resp.setContentType("text/plain");
			resp.getWriter().append("start " + System.currentTimeMillis());
			LoadConfig cfg = new LoadConfig(req.getParameter("db"),
											stripHosts(req.getParameter("hosts")), 
											Integer.parseInt(req.getParameter("load")));
			if(start(cfg)) {
				setConfig(req, cfg);
			}
		} else if("/stop".equals(req.getPathInfo())) {
			resp.setContentType("text/plain");
			resp.getWriter().append("stop " + System.currentTimeMillis());
			stop();
		} else if("/status".equals(req.getPathInfo())) {
			writeStatus(resp);
		}
	}
	
	private void writeStatus(HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		if(runner != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(resp.getWriter(), runner.getStatus());
			} catch (JsonGenerationException e) {
				throw new ServletException(e);
			} catch (JsonMappingException e) {
				throw new ServletException(e);
			}
		} else {
			resp.getWriter().append("{\"stopped\": true}");
		}
	}

	private synchronized boolean start(LoadConfig cfg) {
		if(runner == null) {
			runner = new LoadgenRunner(cfg);
			runner.start();
			return true;
		}
		return false;
	}
	
	private synchronized void stop() {
		if(runner != null) {
			System.out.println("stop");
			runner.exit();
			try {
				runner.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runner = null;
		}
	}
	
	static class LoadgenRunner extends Thread {
		public LoadStatusNotify notify = new LoadStatusNotify();
		private LoadConfig cfg;

		Client c = new Client();

		public LoadgenRunner(LoadConfig cfg) {
			this.cfg = cfg;
		}

		@Override
		public void run() {
			File workload = getWorkload();
			System.out.println("Using workload file " + workload.getAbsolutePath());
			String[] args = new String[]{
				"-P",
				workload.getAbsolutePath(), 
				"-t",
				"-target",
				"" + cfg.getLoad(),
				"-s"
			};

			c.start(notify, args);
		}
		
		private File getWorkload() {
			try {
				String newLine = System.getProperty("line.separator");
				File tmp = File.createTempFile("workload-", ".ycsb");
				BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("workload"), "UTF-8"));
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					w.append(line.replace("${hosts}", cfg.getHosts())
								.replace("${db}", cfg.getDb().getDriver()));
					w.append(newLine);
				}
				w.close();
				reader.close();
				return tmp;
			} catch (IOException e) {
				return null;
			}
		}

		private void exit() {
			c.stop();
		}
		
		public StatusBean getStatus() {
			return notify.getStatus();
		}
		
	}

	public static String stripHosts(String h) {
		String plain = h.replace("[", "")
				.replace("]", "")
				.replace("\"", "")
				.replace(" ", "")
				.trim();
		String[] arr = plain.split(",");
		StringBuilder hosts = new StringBuilder();
		for(int i = 0; i < arr.length; i++) {
			String s = arr[i];
			int pos = s.indexOf(":");
			if(pos > -1) {
				arr[i] = s.substring(0, pos);
			}
			if(i > 0) {
				hosts.append(",");
			}
			hosts.append(arr[i]);
		}
		return hosts.toString();
	}
	
	public static void setConfig(HttpServletRequest req, LoadConfig cfg) {
		req.getSession().setAttribute("loadconfig", cfg);
	}
	
	public static LoadConfig getConfig(HttpServletRequest req) {
		return (LoadConfig)req.getSession().getAttribute("loadconfig");
	}

}
