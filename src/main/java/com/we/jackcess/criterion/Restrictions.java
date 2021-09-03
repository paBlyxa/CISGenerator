package com.we.jackcess.criterion;

import com.healthmarketscience.jackcess.Row;
import com.we.jackcess.core.exceptions.NullPointerAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Restrictions {

	private final static Logger logger = LoggerFactory.getLogger(Restrictions.class);
	
	public static Criterion eq(String propertyName, Object value) throws NullPointerAccessException{
		return new Criterion(propertyName, value, new ApplyConstraint(){

			@Override
			public boolean constraint(Row row) {
				if (row.containsKey(propertyName)){
					logger.trace("Value {} is {}", row.get(propertyName).toString(), value.equals(row.get(propertyName).toString()));
					return value.equals(row.get(propertyName).toString());
				}
				logger.trace("No such [{}] columns", propertyName);
				return false;
			}
			
		});
	}
	
	public static Criterion in(String propertyName, Collection<Object> values) throws NullPointerAccessException{
		return new Criterion(propertyName, values, new ApplyConstraint(){
			
			@Override
			public boolean constraint(Row row){
				if (row.get(propertyName) != null){
					for (Object obj : values){
						if (row.get(propertyName).equals(obj)){
							return true;
						}
					}
				}
				return false;
			}
		});
	}
	
	public static Criterion in(String propertyName, Object[] values) throws NullPointerAccessException{
		return new Criterion(propertyName, values, new ApplyConstraint(){
			
			@Override
			public boolean constraint(Row row){
				if (row.get(propertyName) != null){
					for (Object obj : values){
						if (row.get(propertyName).equals(obj)){
							return true;
						}
					}
				}
				return false;
			}
		});
	}
	
	public static Criterion contains(String propertyName, String value) throws NullPointerAccessException{
		return new Criterion(propertyName, value, new ApplyConstraint(){
			
			@Override
			public boolean constraint(Row row){
				return row.containsKey(propertyName) && row.getString(propertyName).contains(value);
			}
		});
	}
}
