package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Периоды")
public class Period {
	
	private int id;
	private float period;
	private String units;
	private String nameGroup;
	private String name;

	public Period() {
		
	}

	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "Название")
	public void setName(String name){
		this.name = name;
	}

	public String getNameGroup() {
		return nameGroup;
	}
	@ColumnAccess(columnName = "Название группы")
	public void setNameGroup(String nameGroup){
		this.nameGroup = nameGroup;
	}
	
	public int getId() {
		return id;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Код")
	public void setId(int id) {
		this.id = id;
	}

	public float getPeriod() {
		return period;
	}
	@ColumnAccess(columnName = "Период обновления")
	public void setPeriod(float period) {
		this.period = period;
	}

	public String getUnits() {
		return units;
	}
	@ColumnAccess(columnName = "Единица изм")
	public void setUnits(String units) {
		this.units = units;
	}
	

	@Override
	public boolean equals(Object object){
		if (object instanceof Period){
			Period period = (Period) object;
			if (period.getId() == this.id){
				return true;
			}
			if ((this.name.equals(period.name)) && (this.nameGroup.equals(period.nameGroup))
					&& (this.period == period.period) && (this.units.equals(period.units))){
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
