package com.we.jackcess.core.exceptions;

import com.we.jackcess.core.Criteria;

public class NotFoundException extends AccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7396293787951285655L;
	private final Criteria<?> criteria;
	
	public NotFoundException(Criteria<?> criteria){
		super("Not found: " + criteria);
		this.criteria = criteria;
	}
	
	public Criteria<?> getCriteria(){
		return criteria;
	}
}
