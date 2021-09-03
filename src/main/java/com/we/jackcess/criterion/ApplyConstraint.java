package com.we.jackcess.criterion;

import com.healthmarketscience.jackcess.Row;

public interface ApplyConstraint {

	public boolean constraint(Row row);
}
