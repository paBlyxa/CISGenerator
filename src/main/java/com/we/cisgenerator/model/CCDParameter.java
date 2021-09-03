package com.we.cisgenerator.model;

public class CCDParameter {

	private String name;
	private String valueOffline;
	private String valueOnline;
	private boolean different;
	
	public CCDParameter(String name, String valueOffline, String valueOnline){
		this.name = name;
		this.valueOffline = valueOffline;
		this.valueOnline = valueOnline;
		this.different = !valueOffline.equals(valueOnline);
	}
	
	public CCDParameter(String name, int valueOffline, int valueOnline){
		this.name = name;
		this.valueOffline = Integer.toHexString(valueOffline);
		this.valueOnline = Integer.toHexString(valueOnline);
		this.different = !(valueOffline == valueOnline);
	}
	
	public CCDParameter(String name, float valueOffline, float valueOnline){
		this.name = name;
		this.valueOffline = Float.toString(valueOffline);
		this.valueOnline = Float.toString(valueOnline);
		this.different = !(valueOffline == valueOnline);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValueOffline() {
		return valueOffline;
	}
	public void setValueOffline(String valueOffline) {
		this.valueOffline = valueOffline;
	}
	public String getValueOnline() {
		return valueOnline;
	}
	public void setValueOnline(String valueOnline) {
		this.valueOnline = valueOnline;
	}
	public boolean isDifferent(){
		return different;
	}
	
}
