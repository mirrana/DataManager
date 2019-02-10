/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Sarah Skanes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.abopu.data;

import com.abopu.data.jdbc.dao.exception.RepositoryException;

import java.util.Collection;

/**
 * @param <T>
 * @author Sarah Skanes &lt;agent154@abopu.com&gt;
 */
public interface Repository<T, R> {

	/**
	 * Persist a new object to the backing store.
	 * Any id in the given object will be ignored,
	 * and a new one will be generated during the persist operation.
	 *
	 * @param record the record to persist
	 * @return copy of object saved with generated values included
	 */
	T create(T record) throws RepositoryException;

	/**
	 * Get data from the backing store.
	 * Returns null if no record is found with the given id.
	 *
	 * @param id primary key of record to retrieve
	 * @return a Repository representing the object requested, or null if the requested record does not exist.
	 */
	T read(R id) throws RepositoryException;

	Collection<T> read(Criteria criteria) throws RepositoryException;

	/**
	 * 
	 * @return
	 * @throws RepositoryException
	 */
	Collection<T> readAll() throws RepositoryException;

	/**
	 * Persist an updated version of a record to the backing store.
	 * The id stored in the object will be used to locate the existing
	 * record to update. If no id is given, or if the id cannot be found,
	 * then nothing happens.
	 *
	 * @param record record to persist
	 * @return true if an update was performed, false otherwise.
	 */
	boolean update(T record) throws RepositoryException;

	/**
	 * Remove a record from the backing store.
	 *
	 * @param record the record to remove
	 * @return true if a record was deleted, false otherwise.
	 */
	boolean delete(T record) throws RepositoryException;
}