package com.we.cisgenerator.model.internal;


import com.we.jackcess.core.exceptions.NullPointerAccessException;
import com.we.jackcess.criterion.Criterion;
import com.we.jackcess.criterion.Restrictions;

public class DPFilter {
	
	public DPFilter(String name, String column, String value, FilterType type){
		this.name = name;
		this.column = column;
		this.value = value;
		this.type = type;
	}
	
	public String name;
	public String column;
	public String value;
	public FilterType type;
	
	@Override
	public String toString(){
		return name;
	}
	
	public Criterion getRestriction() throws NullPointerAccessException{
		switch(type){
		case CONTAINS:
			return Restrictions.contains(column, value);
		case EQ:
			return Restrictions.eq(column, value);
		case IN:
			return Restrictions.in(column, (value.trim().split(",|;|\t")));	
		}
		return null;
	}
	
	public enum FilterType{
		EQ, IN, CONTAINS;
	}
}
