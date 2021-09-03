package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Шкафы")
public class Cabinet {

	private final static String TABLE_NAME = "Шкафы";
	
	private int id;
	private String name;
	private String shortName;
	
	public int getId() {
		return id;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Код")
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "Шкаф")
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	@ColumnAccess(columnName = "Name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof Cabinet){
			Cabinet cab = (Cabinet) object;
			if (cab.getId() == this.id){
				return true;
			}
			if ((cab.getName() == this.name) && (cab.getShortName() == this.shortName)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return Integer.hashCode(id);
	}
}
