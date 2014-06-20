package brooklyn.loadgen;

import java.io.IOException;

import com.yahoo.ycsb.measurements.exporter.MeasurementsExporter;

public class MeasurementsReader implements MeasurementsExporter {
	private String type;
	private String name;
	private double value;
	
	public MeasurementsReader(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	@Override
	public void close() throws IOException {
	}
	
	@Override
	public void write(String arg0, String arg1, double arg2) throws IOException {
		if(type.equals(arg0) && name.equals(arg1)) {
			value = arg2;
		}
	}
	
	@Override
	public void write(String arg0, String arg1, int arg2) throws IOException {
		
	}
	
	public double getValue() {
		return value;
	}
}

