package com.we.jackcess.core;

import com.we.jackcess.core.exceptions.InvokeMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyMethod {
	
	private final Method method;
	private final String columnName;
	private final Class<?> foreignClass;
	private final boolean simple;
	private final boolean primaryKey;
	private final Integer index;
	
	public MyMethod(Method method, String columnName, boolean primaryKey){
		this.method = method;
		this.columnName = columnName;
		this.foreignClass = null;
		this.simple = true;
		this.primaryKey = primaryKey;
		this.index = null;
	}
	
	public MyMethod(Method method, String columnName, boolean primaryKey, int index){
		this.method = method;
		this.columnName = columnName;
		this.foreignClass = null;
		this.simple = true;
		this.primaryKey = primaryKey;
		this.index = index;
	}
	
	public MyMethod(Method method, String columnName, Class<?> foreignClass){
		this.method = method;
		this.columnName = columnName;
		this.foreignClass = foreignClass;
		this.simple = false;
		this.primaryKey = false;
		this.index = null;
	}
	
	public Object invokeMethod(Object obj, Object...args) throws IllegalAccessException, InvokeMethodException{
		try {
			return method.invoke(obj, args);
		} catch(IllegalArgumentException e){
			throw new InvokeMethodException(this, obj, args[0], e);
		} catch(InvocationTargetException e){
			throw new InvokeMethodException(this, obj, args[0], e);
		}
	}
	
	public String getColumnName(){
		return columnName;
	}
	
	public Method getMethod(){
		return method;
	}
	
	public boolean isSimple(){
		return simple;
	}
	
	public boolean isPrimaryKey(){
		return primaryKey;
	}
	
	public Class<?> getForeignClass(){
		return foreignClass;
	}
	
	public Integer getIndex(){
		return index;
	}
}
