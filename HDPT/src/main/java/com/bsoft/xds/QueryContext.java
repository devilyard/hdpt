package com.bsoft.xds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryContext implements Serializable {

	private static final long serialVersionUID = 4648572381978459035L;

	private int start;
	private int limit;
	private List<Object> cnds;
	private String sortInfo;
	private boolean paged = false;
	private Map<String, Object> parameters;
	private List<String> fields = new ArrayList<String>();
	private boolean getDocument = false;// @@ 表示是否是取文档。

	// private List<String> projections;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<Object> getCnds() {
		return cnds;
	}

	public void setCnds(List<Object> cnds) {
		this.cnds = cnds;
	}

	public String getSortInfo() {
		return sortInfo;
	}

	public void setSortInfo(String sortInfo) {
		this.sortInfo = sortInfo;
	}

	/**
	 * 是否需要分页查询。
	 * 
	 * @return
	 */
	public boolean isPaged() {
		return paged;
	}

	public void setPaged(boolean paged) {
		this.paged = paged;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void addField(String field) {
		this.fields.add(field);
	}

	public boolean isGetDocument() {
		return getDocument;
	}

	public void setGetDocument(boolean getDocument) {
		this.getDocument = getDocument;
	}

	// public void setProjections(List projections){
	// this.projections = projections;
	// }

}
