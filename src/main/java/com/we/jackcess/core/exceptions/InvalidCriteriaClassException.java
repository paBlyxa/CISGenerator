package com.we.jackcess.core.exceptions;

public class InvalidCriteriaClassException extends AccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -671683417695104764L;
	
	private final Class<?> criteriaClass;
	
	public InvalidCriteriaClassException(Class<?> criteriaClass){
		super("Invalid criteria class - " + criteriaClass.getCanonicalName());
		this.criteriaClass = criteriaClass;
	}
	
	public Class<?> getCriteriaClass(){
		return criteriaClass;
	}
}
