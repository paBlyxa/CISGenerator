package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "DI внутренние")
public class DI_Internal extends DI {
	
	private Integer idAlarm;
	
	public DI_Internal(String ident){
		super(ident);
	}
	
	public DI_Internal(){
	}
	
	public int getIdAlarm(){
		return idAlarm;
	}
	@ColumnAccess(columnName = "IDALARM")
	public void setIdAlarm(Integer idAlarm){
		this.idAlarm = idAlarm;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("DI-Internal: ");
		str.append(getIdent());
		str.append(" - ");
		str.append(getName());
		str.append(", Шкаф: ").append(getCabinet() != null ? getCabinet().getName() : null);
		str.append(", PLC: ").append(getPlc() != null ? getPlc().getPosition() : null);
		str.append(", Module: ").append(getModuleNum());
		str.append(", Channel: ").append(getChannelNum());
		return str.toString();
	}
}
