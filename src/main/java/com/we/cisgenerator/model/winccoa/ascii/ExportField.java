package com.we.cisgenerator.model.winccoa.ascii;

public class ExportField<T extends Enum<T>> {

	private final String[] elements;
	
	public ExportField(int countElements){
		elements = new String[countElements];
		elements[0] = "ASC (1)/0";
	}
	
	public String getElement(int index){
		return elements[index];
	}
	
	public void setElement(int index, String value){
		elements[index] = value;
	}
	
	public String toExportString(){
		StringBuilder result = new StringBuilder();
		for (String str : elements){
			result.append(str != null ? str : "");
			result.append("\t");
		}
		result.deleteCharAt(result.length() - 1);
		result.append("\r\n");
		return result.toString();
	}
	
	public ExportField<T> setElement(T element, String value){
		setElement(element.ordinal(), value);
		return this;
	}
}
