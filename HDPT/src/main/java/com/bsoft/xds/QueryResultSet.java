package com.bsoft.xds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResultSet implements Serializable {
	private static final long serialVersionUID = 6195741402452890231L;
	private long totalCount;
	private int start;
	private int limit;
	private List<Map<String, Object>> records;
	private Map<String, Object> properties;

	public QueryResultSet(Map<String, Object> record) {
		this.totalCount = 1;
		this.start = 0;
		this.limit = 1;
		this.records = new ArrayList<Map<String, Object>>(1);
		this.records.add(record);
	}

	public QueryResultSet(List<Map<String, Object>> records) {
		this.limit = records == null ? 0 : records.size();
		this.start = 0;
		this.totalCount = this.limit;
		this.records = records;
	}

	public QueryResultSet(long totalCount, int start, int limit,
			List<Map<String, Object>> records) {
		this.totalCount = totalCount;
		this.start = start;
		this.limit = limit;
		this.records = records;
	}

	/**
	 * 通常情况下在需要分布时totalCount表示总记录数，否则表示的是本次取出的记录数。
	 * 
	 * @return
	 */
	public long getTotal() {
		return totalCount;
	}

	public long getStart() {
		return start;
	}

	public long getLimit() {
		return limit;
	}

	public List<Map<String, Object>> getRecords() {
		return records;
	}

	public void setProperty(String nm, Object v) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		properties.put(nm, v);
	}

	public Object getProperty(String nm) {
		if (properties == null) {
			return null;
		}
		return properties.get(nm);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
}
