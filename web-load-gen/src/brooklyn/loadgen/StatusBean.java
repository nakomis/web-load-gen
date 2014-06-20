package brooklyn.loadgen;

public class StatusBean {
	private long seconds;
	private long ops;
	private double opsPerSecond;
	private double updateAvgLatency;
	private double readAvgLatency;
	public StatusBean(long seconds, long ops, double opsPerSecond, double updateAvgLatency,
			double readAvgLatency) {
		this.seconds = seconds;
		this.ops = ops;
		this.opsPerSecond = opsPerSecond;
		this.updateAvgLatency = updateAvgLatency;
		this.readAvgLatency = readAvgLatency;
	}
	public long getSeconds() {
		return seconds;
	}
	public long getOps() {
		return ops;
	}
	public double getOpsPerSecond() {
		return opsPerSecond;
	}
	public double getUpdateAvgLatency() {
		return updateAvgLatency;
	}
	public double getReadAvgLatency() {
		return readAvgLatency;
	}
}
