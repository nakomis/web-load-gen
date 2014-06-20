<%@page import="brooklyn.loadgen.LoadConfig"%>
<%@page import="brooklyn.loadgen.LauncherServlet"%>
<%
	String db;
	String hosts;
	String load;
	LoadConfig cfg = LauncherServlet.getConfig(request);
	if(cfg != null) {
		db = cfg.getDb().getName();
		hosts = cfg.getHosts();
		load = "" + cfg.getLoad();
	} else {
		db = System.getProperty("load_gen.db", "couchbase2");
		hosts = LauncherServlet.stripHosts(
					System.getProperty("load_gen.hosts",
						System.getProperty("brooklyn.example.couchbase.nodes", "")));
		load = "10000";
	}
%><!doctype html>
<html>
	<head>
		<title>Couchbase stress test demo</title>
		<link rel="stylesheet" href="lib/bootstrap/css/bootstrap.min.css">		
		<script type="text/javascript" src="lib/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="lib/amcharts/amcharts.js"></script>
		<script type="text/javascript" src="lib/amcharts/serial.js"></script>
		<script type="text/javascript" src="app.js"></script>
	</head>
	<body style="padding:20px;">
		<div class="row">
			<form id="info" class="col-xs-4 col-xs-offset-4">
				<div class="form-group">
					<label for="db">Database:</label>
					<select id="db" name="db" class="form-control">
						<option value="couchbase2" <%="couchbase2".equals(db) ? "selected" : ""%>>Couchbase 2.0</option>
						<option value="mongodb" <%="mongodb".equals(db) ? "selected" : ""%>>MongoDB</option>
						<option value="cassandra" <%="cassandra".equals(db) ? "selected" : ""%>>Cassandra</option>
					</select>
				</div>
				<div class="form-group">
					<label for="hosts">Hosts:</label>
					<input type="text" id="hosts" name="hosts" 
						class="form-control"
						required="required" value="<%=hosts%>">
				</div>
				<div class="form-group">
					<label for="load">Load:</label>
					<input type="number" id="load" name="load"
						class="form-control" 
						value="<%=load%>">
				</div>
				<button id="stop" disabled="disabled" class="btn btn-danger" style="float:right">Stop</button>
				<button id="start" disabled="disabled" class="btn btn-success">Start</button>
			</form>
		</div>
		<div class="row">
			<fieldset class="col-xs-10 col-xs-offset-1">
				<legend>Status</legend>
				<div id="status"></div>
				<div id="chart" style="height: 250px;"></div>
				<div id="legend"></div>
			</fieldset>
		</div>
	</body>
</html>
