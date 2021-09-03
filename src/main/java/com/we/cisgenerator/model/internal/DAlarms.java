package com.we.cisgenerator.model.internal;

import com.we.jackcess.annotations.ColumnAccess;

public abstract class DAlarms {

	private final static int COUNT_ALARM_MODES = 6;
	
	protected final Integer[] idAlarms;
	
	public DAlarms(){
		idAlarms = new Integer[COUNT_ALARM_MODES];
	}
	
	public Integer[] getIdAlarms(){
		return idAlarms;
	}
	@ColumnAccess(columnName = "IDALARM", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addIdAlarm(Integer alarmValue, int index){
		idAlarms[index] = alarmValue;
	}
}
