package com.we.jackcess.core;

import com.we.jackcess.core.exceptions.InvalidCriteriaClassException;
import com.we.jackcess.core.exceptions.InvokeMethodException;
import com.we.jackcess.core.exceptions.NotFoundException;
import com.we.jackcess.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for search criteria
 * 
 * @author fakadey
 *
 * @param <T>
 */
public class Criteria<T> {

	final static Logger logger = LoggerFactory.getLogger(Criteria.class);
	
	/**
	 * 
	 */
	private final Factory factory;
	private final Class<T> criteriaClass;
	private final String tableName;
	//private final Map<String, Object> restrictions;
	private final List<Criterion> criterias;
	private Object value;

	Criteria(Factory factory, Class<T> criteriaClass) throws InvalidCriteriaClassException {
		this.factory = factory;
		logger.debug("Create new Criteria<{}>", criteriaClass.getCanonicalName());
		this.criteriaClass = criteriaClass;
		tableName = this.factory.parseTableName(criteriaClass);
		//restrictions = new HashMap<String, Object>();
		criterias = new ArrayList<Criterion>();
	}

	public Criteria<T> addPrimaryKeyValue(Object value) {
		this.value = value;
		return this;
	}

	public List<T> list() throws NotFoundException, InvalidCriteriaClassException, InvokeMethodException {
		return this.factory.find(this);
	}

	public Class<T> getCriteriaClass() {
		return criteriaClass;
	}

	public String getTableName() {
		return tableName;
	}

	public Object getPrimaryKeyValue() {
		return value;
	}
	
	public List<String> getColumns() throws IOException{
		return this.factory.getColumns(this);
	}
	
/*	public Criteria<T> addRestriction(String columnName, Object columnValue) throws NullPointerAccessException{
		if (columnName == null){
			throw new NullPointerAccessException("Try to add restriction with null columnName");
		}
		if (columnValue == null){
			throw new NullPointerAccessException("Try to add restriction with null columnValue");
		}
		restrictions.put(columnName, columnValue);
		return this;
	}
	
	protected Map<String, Object> getRestrictions(){
		return restrictions;
	}*/
	
	protected List<Criterion> getCriterias(){
		return criterias;
	}
	
	public Criteria<T> add(Criterion criterion){
		criterias.add(criterion);
		return this;
	}
	
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("Criteria: ");
		str.append(criteriaClass.getCanonicalName());
		str.append(", primaryKey - ");
		str.append(value);
		//if (!restrictions.isEmpty()){
		if (!criterias.isEmpty()){
			str.append(", restrictions: [");
			boolean first = true;
			//for (Map.Entry<String, Object> entry : restrictions.entrySet()){
			for (Criterion criterion : criterias){
				if (!first){
					str.append(", ");
					first = false;
				}
				//str.append(entry.getKey());
				str.append(criterion.getPropertyName());
				str.append(" - ");
				//str.append(entry.getValue());
				str.append(criterion.getValue());
			}
			str.append("]");
		}
		return str.toString();
	}
}