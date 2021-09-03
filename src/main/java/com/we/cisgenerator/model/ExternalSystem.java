package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Внешние системы")
public class ExternalSystem {
	
	private String system;
	private String addressMain;
	private String addressRezerv;
	private String name;
	
	public ExternalSystem(){
	}

	public String getSystem() {
		return system;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Система")
	public void setSystem(String system) {
		this.system = system;
	}

	public String getAddressMain() {
		return addressMain;
	}
	@ColumnAccess(columnName = "Ip адрес (осн)")
	public void setAddressMain(String addressMain) {
		this.addressMain = addressMain;
	}

	public String getAddressRezerv() {
		return addressRezerv;
	}
	@ColumnAccess(columnName = "Ip адрес (рез)")
	public void setAddressRezerv(String addressRezerv) {
		this.addressRezerv = addressRezerv;
	}

	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "Name")
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof ExternalSystem){
			ExternalSystem sys = (ExternalSystem) object;
			if (sys.system.equals(this.system)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.system.hashCode();
	}
}
