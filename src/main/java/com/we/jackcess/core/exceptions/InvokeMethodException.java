package com.we.jackcess.core.exceptions;

import com.we.jackcess.core.MyMethod;

public class InvokeMethodException extends AccessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2898654801846108890L;
	private MyMethod method;
	private Object element;
	private Object value;
	private Exception e;
	
	public InvokeMethodException(MyMethod method, Object element, Object value, Exception e){
		super(e + " in '" + method.getMethod().getName()
				+ "' of '" + element.toString() + "' value '" + value + "'", e);
		this.method = method;
		this.element = element;
		this.value = value;
		this.e = e;
	}
	
	@Override
	public String toString(){
		return e + " in '" + method.getMethod().getName()
				+ "' of '" + element.toString() + "' value '" + value + "'";
	}
}
