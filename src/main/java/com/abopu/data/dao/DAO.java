/*
 * Copyright (c) 2017, Sarah Skanes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *     3. Neither the name of the copyright holder nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package com.abopu.data.dao;

import com.abopu.data.bind.BindObjects;
import com.abopu.data.dao.exception.DaoException;

import java.sql.Connection;
import java.util.Collection;

/**
 * 
 * @param <RecordType> The type of object that the DAO will read from and write to for CRUD operations.
 * @param <KeyType> The type of the primary key for the data that this DAO operates on.
 */
public interface DAO<RecordType, KeyType> {

	/**
	 * <p>Persist a new object to the backing store.</p>
	 * <p>Any id in the given object will be ignored,
	 * and a new one will be generated during the persist operation.</p>
	 *
	 * @param object record to persist
	 * @return copy of object saved with generated values included
	 */
	RecordType create(Connection conn, RecordType object) throws DaoException;

	/**
	 * <p>Remove a record from the backing store.</p>
	 *
	 * @param id id of record to remove
	 * @return true if a record was deleted, false otherwise.
	 */
	boolean delete(Connection conn, KeyType id) throws DaoException;

	Collection<RecordType> get(Connection conn, DaoFilter<RecordType> filter) throws DaoException;

	/**
	 * <p>Get data from the backing store.</p>
	 * <p>Returns null if no record is found with the given id.</p>
	 *
	 * @param id primary key of record to retrieve
	 * @return a DAO representing the object requested, or null if the requested record does not exist.
	 */
	RecordType get(Connection conn, KeyType id) throws DaoException;

	/**
	 * <p>Persist an updated version of a record to the backing store.</p>
	 * <p>The id stored in the object will be used to locate the existing
	 * record to update. If no id is given, or if the id cannot be found,
	 * then nothing happens.</p>
	 *
	 * @param object record to persist
	 * @param bindObjects
	 * @return true if an update was performed, false otherwise.
	 */
	boolean update(Connection conn, RecordType object, BindObjects bindObjects) throws DaoException;
}