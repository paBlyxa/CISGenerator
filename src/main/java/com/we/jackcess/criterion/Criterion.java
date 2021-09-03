package com.we.jackcess.criterion;

import com.healthmarketscience.jackcess.Row;
import com.we.jackcess.core.exceptions.NullPointerAccessException;

public class Criterion {

	private String propertyName;
	private Object value;
	private Criterion next;
	
	private ApplyConstraint applyConstraint;
	
	public Criterion(String propertyName, Object value, ApplyConstraint applyConstraint) throws NullPointerAccessException{
		if (propertyName == null){
			throw new NullPointerAccessException("Try to add restriction with null columnName");
		}
		if (value == null){
			throw new NullPointerAccessException("Try to add restriction with null columnValue");
		}
		this.propertyName = propertyName;
		this.value = value;
		this.applyConstraint = applyConstraint;
	}
	
	public boolean checkConstraint(Row row){
		return applyConstraint.constraint(row);
	}
	
	public String getPropertyName(){
		return propertyName;
	}
	
	public Object getValue(){
		return value;
	}
	
	public Criterion getNext(){
		return next;
	}
}
