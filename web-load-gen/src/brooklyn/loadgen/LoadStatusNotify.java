package brooklyn.loadgen;

import java.io.IOException;

import com.yahoo.ycsb.StatusNotify;
import com.yahoo.ycsb.measurements.Measurements;

public class LoadStatusNotify implements StatusNotify {
	private static final String TYPE_UPDATE = "UPDATE";
	private static final String TYPE_READ = "READ";
	private static final String AVG_LATENCY = "AverageLatency(us)";
	
	private StatusBean status = new StatusBean(0, 0, 0, 0, 0);

	@Override
	public void update(long sec, int ops, double opsPerSecond, Measurements m) {
		if(ops == 0) return;
		
		status = new StatusBean(sec, ops, opsPerSecond, 
				readMeasurement(m, TYPE_UPDATE, AVG_LATENCY),
				readMeasurement(m, TYPE_READ, AVG_LATENCY));
	}
	
	
	
	public StatusBean getStatus() {
		return status;
	}



	private double readMeasurement(Measurements m, String type, String name) {
		try {
			MeasurementsReader reader = new MeasurementsReader(type, name);
			m.exportMeasurements(reader);
			return reader.getValue();
		} catch (IOException e) {
		}
		return 0;
	}

}
