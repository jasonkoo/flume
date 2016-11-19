package com.lenovo.push.marketing.lestat.flume.interceptor;

public class PatternBean {
	private int index;
	private String pattern;
	private String matchedDBName;
	private String matchedTBLName;
	private String notMatchedDBName;
	private String notMatchedTBLName;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getMatchedDBName() {
		return matchedDBName;
	}
	public void setMatchedDBName(String matchedDBName) {
		this.matchedDBName = matchedDBName;
	}
	public String getMatchedTBLName() {
		return matchedTBLName;
	}
	public void setMatchedTBLName(String matchedTBLName) {
		this.matchedTBLName = matchedTBLName;
	}
	public String getNotMatchedDBName() {
		return notMatchedDBName;
	}
	public void setNotMatchedDBName(String notMatchedDBName) {
		this.notMatchedDBName = notMatchedDBName;
	}
	public String getNotMatchedTBLName() {
		return notMatchedTBLName;
	}
	public void setNotMatchedTBLName(String notMatchedTBLName) {
		this.notMatchedTBLName = notMatchedTBLName;
	}
	
}
