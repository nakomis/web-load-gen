package brooklyn.loadgen;

public class LoadConfig {
	private Database db;
	private String hosts;
	private int load;

	public LoadConfig(String db, String hosts, int load) {
		this.db = Database.valueOf(db.toUpperCase());
		this.hosts = hosts;
		this.load = load;
	}

	public Database getDb() {
		return db;
	}

	public String getHosts() {
		return hosts;
	}
	
	public int getLoad() {
		return load;
	}
}
