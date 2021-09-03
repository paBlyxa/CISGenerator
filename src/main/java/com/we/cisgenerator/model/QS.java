package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.TableAccess;

import java.util.ArrayList;
import java.util.List;

@TableAccess(name = "Состояния механизмов (QS)")
public class QS {
	
	private String ident;
	private String name;
	private final List<String> parameters;
	private final List<String> messages;
	
	public QS(){
		parameters = new ArrayList<String>();
		messages = new ArrayList<String>();
	}
	
	public QS(String ident){
		this();
		this.ident = ident;
	}	
	
	public String getIdent(){
		return ident;
	}
	@ColumnAccess(columnName = "Идент")
	public void setIdent(String ident){
		this.ident = ident;
	}
	
	public String getName(){
		return name;
	}
	@ColumnAccess(columnName = "НАИМЕНОВАНИЕ")
	public void setName(String name){
		this.name = name;
	}
	
	public List<String> getParameters(){
		return parameters;
	}
	@ColumnAccess(columnName = "ZINPNT0", count = 2)
	public void addParameter(String parameter){
		parameters.add(parameter);
	}
	
	public List<String> getMessages(){
		return messages;
	}
	@ColumnAccess(columnName = "ZQSMSGS0", count = 4)
	public void addMessage(String message){
		messages.add(message);
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("QS: ");
		str.append(ident);
		str.append(" - ");
		str.append(name);
		str.append(", parameters: ");
		for (String parameter : parameters){
			str.append(parameter);
			str.append(" ");
		}
		str.append(", messages: ");
		for (String message : messages){
			str.append(message);
			str.append(" ");
		}
		return str.toString();
	}
}
