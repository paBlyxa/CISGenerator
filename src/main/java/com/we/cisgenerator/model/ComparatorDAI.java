package com.we.cisgenerator.model;

import java.util.Comparator;

public class ComparatorDAI implements Comparator<XR> {

	@Override
	public int compare(XR o1, XR o2) {
		return o1.getExternalAddr() - o2.getExternalAddr();
	}

}
