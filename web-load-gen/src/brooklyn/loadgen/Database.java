package brooklyn.loadgen;

public enum Database {
	COUCHBASE2("com.yahoo.ycsb.couchbase.CouchbaseClient2_0"),
	MONGODB("com.yahoo.ycsb.mongo.MongoDbClient"),
	CASSANDRA("com.yahoo.ycsb.cassandra.CassandraClient");
	
	private String driver;
	private Database(String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}
	
	public String getName() {
		return this.toString().toLowerCase();
	}
}
