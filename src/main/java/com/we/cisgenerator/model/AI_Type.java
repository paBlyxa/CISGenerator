package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Типы аналоговых сигналов")
public class AI_Type {

	private int id;
	private DMC.DMC_TYPE dmcType;
	private boolean invers;
	
	public int getId() {
		return id;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Код")
	public void setId(int id) {
		this.id = id;
	}
	public DMC.DMC_TYPE getDmcType() {
		return dmcType;
	}
	public void setDmcType(DMC.DMC_TYPE dmcType) {
		this.dmcType = dmcType;
	}
	@ColumnAccess(columnName = "TypePLC")
	public void setDmcType(String name){
		this.dmcType = DMC.DMC_TYPE.valueOf(name);
	}
	public boolean isInvers() {
		return invers;
	}
	@ColumnAccess(columnName = "Invers")
	public void setInvers(boolean invers) {
		this.invers = invers;
	}
	
	@Override
	public String toString(){
		return dmcType.toString();
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof AI_Type){
			AI_Type type = (AI_Type) object;
			if (type.getId() == this.id){
				return true;
			}
			//return dmcType.equals(((AI_Type) object).getDmcType());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return Integer.hashCode(id);
	}
	
}
