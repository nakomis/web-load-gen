package com.yahoo.ycsb;

import com.yahoo.ycsb.measurements.Measurements;

public interface StatusNotify {
	public void update(long sec, int ops, double opsPerSecond, Measurements m);
}
