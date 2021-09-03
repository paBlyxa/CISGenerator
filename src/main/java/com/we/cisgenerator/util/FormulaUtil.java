package com.we.cisgenerator.util;

public class FormulaUtil {

	public static String changeParameters(String formula, int sizeParameters){
		String newFormula = formula;
		for (int i = sizeParameters; i >= 2 ; i--){
			newFormula = newFormula.replaceAll("p" + i, "a" + (2 * i - 1));
		}
		newFormula = newFormula.replaceAll("a", "p");
		return newFormula;
	}
	
	public static String changeOperatorToFunction(String formula, String operator, String function){
		int index = 0;
		while ((index = formula.indexOf(operator, index)) != -1){
			// Get left parameter
			int leftIndex = index - 1;
			while ((leftIndex > 0) && isSymbol(formula, leftIndex)){
				leftIndex--;
			}
			if (formula.charAt(leftIndex) == ')'){
				leftIndex = getCloseBracket(formula, leftIndex, true);
			} else if (!isSymbol(formula, leftIndex)){
				++leftIndex;
			}
			String leftParameter = formula.substring(leftIndex, index);
			// Get right parameter
			int rightIndex = index + 1;
			while ((rightIndex < (formula.length() - 1)) && isSymbol(formula, rightIndex)){
				rightIndex++;
			}
			if (formula.charAt(rightIndex) == '('){
				rightIndex = getCloseBracket(formula, rightIndex, false);
			} else if (!isSymbol(formula, rightIndex)){
				--rightIndex;
			}
			String rightParameter = formula.substring(index + 1,rightIndex + 1);
			String subStr = formula.substring(leftIndex, rightIndex + 1);
			formula = formula.replace(subStr, function + "(" + leftParameter + ", " + rightParameter + ")");
		}
		return formula;
	}
	
	private static boolean isSymbol(String str, int index){
		char ch = str.charAt(index);
		if ((ch == '-') || (ch == '+')){
			if (index == 0){
				return true;
			} else {
				return !isSymbol(str, index - 1); 
			}
		} else {
			return ((ch >= '0') && (ch <= '9')) || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z') || (ch == '.'));
		}
	}
	
	private static int getCloseBracket(String str, int index, boolean left){
		int countBracket = left ? 1 : -1;
		while ((countBracket != 0) && (index > 0) && (index < (str.length() - 1))){
			if (left){
				index--;
			} else {
				index++;
			}
			if (str.charAt(index) == '('){
				countBracket--;
			}
			if (str.charAt(index) == ')'){
				countBracket++;
			}
		}
		return index;
	}
}
