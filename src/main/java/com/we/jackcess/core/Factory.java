package com.we.jackcess.core;

import com.healthmarketscience.jackcess.*;
import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;
import com.we.jackcess.core.exceptions.InvalidCriteriaClassException;
import com.we.jackcess.core.exceptions.InvokeMethodException;
import com.we.jackcess.core.exceptions.NotFoundException;
import com.we.jackcess.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for getting data from MS Access
 * 
 * @author fakadey
 *
 */
public class Factory {

	final static Logger logger = LoggerFactory.getLogger(Factory.class);

	private final File file;
	private final Map<Class<?>, Map<Object, Object>> cache = new HashMap<>();
	
	private Database db;

	public Factory(File file) {
		this.file = file;
	}

	public <T> Criteria<T> createCriteria(Class<T> criteriaClass) throws InvalidCriteriaClassException {
		return new Criteria<T>(this, criteriaClass);
	}

	private <T> T findByPrimaryKey(Criteria<T> criteria) throws IOException, NotFoundException, InstantiationException,
			IllegalAccessException, InvalidCriteriaClassException, InvokeMethodException {
		Table table = db.getTable(criteria.getTableName());
		Row row = CursorBuilder.findRowByPrimaryKey(table, criteria.getPrimaryKeyValue());
		if (row != null) {
			T element = createNewElement(criteria.getCriteriaClass(), row, null);
			return element;
		} else {
			throw new NotFoundException(criteria);
		}
	}

	<T> List<T> find(Criteria<T> criteria)
			throws NotFoundException, InvalidCriteriaClassException, InvokeMethodException {
		logger.debug("Try to find criteria {}", criteria);
		List<T> list = new ArrayList<>();
		try {
			db = DatabaseBuilder.open(file);
			Table table = db.getTable(criteria.getTableName());
			List<MyMethod> methods = getMethods(criteria.getCriteriaClass());
			for (Row row : table) {
				boolean isSuitable = true;
				//for (Entry<String, Object> restriction : criteria.getRestrictions().entrySet()) {
				for (Criterion criterion : criteria.getCriterias()) {
					/*if (restriction.getValue() instanceof Collection) {
						@SuppressWarnings("unchecked")
						Collection<Object> objs = (Collection<Object>) restriction.getValue();
						isSuitable = false;
						Object valueFromDB = row.get(restriction.getKey());
						for (Object obj : objs) {
							if (obj.equals(valueFromDB)) {
								isSuitable = true;
								break;
							}
						}
					} else {
						isSuitable = row.get(restriction.getKey()).equals(restriction.getValue());
					}*/
					if (!criterion.checkConstraint(row)){
						isSuitable = false;
						break;
					}
				}
				if (isSuitable) {
					T element = createNewElement(criteria.getCriteriaClass(), row, methods);
					list.add(element);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					logger.debug("Close MS Access db connection");
					db.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	private <T> T createNewElement(Class<T> criteriaClass, Row row, List<MyMethod> methods)
			throws InstantiationException, IllegalAccessException, NotFoundException, InvalidCriteriaClassException,
			IOException, InvokeMethodException {
		T element = criteriaClass.newInstance();
		Object obj = null;
		if (methods == null) {
			methods = getMethods(criteriaClass);
		}
		try {
			// First try to find in cache
			for (MyMethod method : methods) {
				if (method.isPrimaryKey()) {
					obj = row.get(method.getColumnName());
					method.invokeMethod(element, obj);
					// Save element in cache
					Map<Object, Object> map = cache.get(criteriaClass);
					if (map == null) {
						map = new HashMap<Object, Object>();
						cache.put(criteriaClass, map);
						map.put(obj, element);
					} else {
						@SuppressWarnings("unchecked")
						T savedElement = (T) map.get(obj);
						if (savedElement != null){
							return savedElement;
						} else {
							map.put(obj, element);
						}
					}
				}
			}
			// simple methods
			for (MyMethod method : methods) {

				if (method.isSimple() && (!method.isPrimaryKey())) {
					obj = row.get(method.getColumnName());
					if (obj != null) {
						if (method.getIndex() != null) {
							method.invokeMethod(element, obj, method.getIndex());
						} else {
							method.invokeMethod(element, obj);
						}
						
					}
				}
			}
			// Other methods
			for (MyMethod method : methods) {

				if (!method.isSimple()) {
					obj = row.get(method.getColumnName());
					if (obj != null) {
						Map<Object, Object> map = cache.get(method.getForeignClass());
						Object value = null;
						if (map == null) {
							map = new HashMap<Object, Object>();
							cache.put(method.getForeignClass(), map);
						} else {
							value = map.get(obj);
						}
						if (value == null) {
							// Нужно найти объект в базе
							value = findByPrimaryKey(createCriteria(method.getForeignClass()).addPrimaryKeyValue(obj));
							// Сохранить в кэше
							map.put(obj, value);
						}
						// Вызвать метод для сохранения в объекте
						method.invokeMethod(element, value);
					}
				}
			}
		} catch (InvokeMethodException e) {
			logger.error(e.toString(), e);
			throw e;
		}
		return element;
	}

	private List<MyMethod> getMethods(Class<?> criteriaClass) {
		List<MyMethod> methods = new ArrayList<>();
		for (Method method : criteriaClass.getMethods()) {
			ColumnAccess anno = method.getAnnotation(ColumnAccess.class);
			if (anno != null) {
				if (anno.count() > 1) {
					for (int i = 1; i <= anno.count(); i++) {
						if (anno.accessByIndex()) {
							methods.add(new MyMethod(method, anno.columnName() + i,
									method.isAnnotationPresent(ColumnPrimaryKey.class), i-1));
						} else {
							methods.add(new MyMethod(method, anno.columnName() + i,
									method.isAnnotationPresent(ColumnPrimaryKey.class)));
						}
					}
				} else {
					methods.add(new MyMethod(method, anno.columnName(),
							method.isAnnotationPresent(ColumnPrimaryKey.class)));
				}
			} else {
				ColumnManyToOne annotation = method.getAnnotation(ColumnManyToOne.class);
				if (annotation != null) {
					if (annotation.count() > 1) {
						for (int i = 1; i <= annotation.count(); i++) {
							methods.add(new MyMethod(method, annotation.columnName() + i, annotation.foreignClass()));
						}
					} else {
						methods.add(new MyMethod(method, annotation.columnName(), annotation.foreignClass()));
					}
				}
			}
		}
		return methods;
	}

	<T> List<String> getColumns(Criteria<T> criteria) throws IOException{
		List<String> columns = new ArrayList<>();
		try {
			db = DatabaseBuilder.open(file);
			Table table = db.getTable(criteria.getTableName());
			List<? extends Column> listColumns = table.getColumns();
			for (Column col : listColumns){
				columns.add(col.getName());
			}
		} catch (IOException e) {
			logger.error("An error occured while reading database", e);
			throw e;
		}finally {
			try {
				if (db != null) {
					logger.debug("Close MS Access db connection");
					db.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}
	
	String parseTableName(Class<?> criteriaClass) throws InvalidCriteriaClassException {
		TableAccess annotation = criteriaClass.getAnnotation(TableAccess.class);
		if (annotation != null) {
			// TODO add checking of primary key
			return annotation.name();
		} else {
			throw new InvalidCriteriaClassException(criteriaClass);
		}
	}
}
