package com.abopu.data.dao;

import java.sql.SQLType;

/**
 * @author Sarah Skanes
 * @created July 13, 2017.
 */
public interface ColumnDefinition<T> {
	String name();
	SQLType sqlType();
}