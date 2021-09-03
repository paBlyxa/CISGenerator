package com.we.cisgenerator.model.internal;

import com.we.jackcess.annotations.ColumnAccess;

public abstract class AnalogSignal extends XZAlarms{

	private Float defaultValue = 0.0f;
	private String format;
	private String units = "";
	
	public Float getDefaultValue() {
		return defaultValue;
	}
	@ColumnAccess(columnName = "Значение по умолчанию")
	public void setDefaultValue(Float defaultValue) {
		if (defaultValue != null){
			this.defaultValue = defaultValue;
		}
	}

	public String getFormat() {
		return format;
	}
	@ColumnAccess(columnName = "Формат")
	public void setFormat(String format) {
		this.format = format;
	}

	public String getUnits() {
		return units;
	}
	@ColumnAccess(columnName = "ЕД ИЗМ")
	public void setUnits(String units) {
		if (units != null){
			this.units = units;
		} else {
			this.units = "";
		}
	}
}
