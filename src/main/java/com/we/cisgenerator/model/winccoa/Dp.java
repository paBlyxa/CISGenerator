package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;

public interface Dp {

	public String getExportField(AsciiExportField field);
	
	public String getDpName();
	
	public String getSDSType();
	
	public boolean isCorrect();
}
