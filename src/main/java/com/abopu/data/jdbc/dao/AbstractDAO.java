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

package com.abopu.data.jdbc.dao;

import java.security.PrivilegedActionException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import com.abopu.data.accesscontrol.PermissionDeniedException;
import com.abopu.data.accesscontrol.RequestContext;
import com.abopu.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class containing methods that any Repository could make use of.
 *
 * @author Sarah Skanes &lt;agent154@abopu.com&gt;
 */
public class AbstractDAO {

	/***************************************************************************
	 *
	 * Constants
	 *
	 **************************************************************************/

	/** Logger */
	private static final Logger LOG = Logger.getLogger(AbstractDAO.class);



	/***************************************************************************
	 *
	 * Public API
	 *
	 **************************************************************************/

	/**
	 * Given a {@link ResultSet} and a {@link ResultExtractor}, generate an {@link ArrayList} of records.
	 * These records depend on what table, or set of tables the Repository operates on.
	 * <p>
	 * If the ResultSet is not currently pointing at the first result, and if the ResultSet supports
	 * seeking backwards, then the cursor will be reset to the first row in the ResultSet. Otherwise,
	 * it is possible that some rows will be excluded from the collection of results.
	 *
	 * @param rs        the {@link ResultSet} containing table data to read
	 * @param extractor a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param <T>       the type of object that will be returned by the {@link ResultExtractor}
	 * @return an {@link ArrayList} of database objects, one for each row in <code>rs</code>
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 */
	protected <T> List<T> processResults(
			@NotNull ResultSet rs,
			@NotNull ResultExtractor<T> extractor) throws SQLException {

		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");

		return processResults(rs, null, extractor, ArrayList::new, null);
	}

	/**
	 * Given a {@link ResultSet} and a {@link ResultExtractor}, generate an {@link ArrayList} of records.
	 * These records depend on what table, or set of tables the Repository operates on.
	 * <p>
	 * If the ResultSet is not currently pointing at the first result, and if the ResultSet supports
	 * seeking backwards, then the cursor will be reset to the first row in the ResultSet. Otherwise,
	 * it is possible that some rows will be excluded from the collection of results.
	 * <p>
	 * If there are no results to return, and you wish to throw an exception instead of returning
	 * and empty list, then provide a non-null implementation of <code>throwIfEmpty</code>.
	 *
	 * @param rs           the {@link ResultSet} containing table data to read
	 * @param extractor    a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param throwIfEmpty exception to throw if the list is empty
	 * @param <T>          the type of object that will be returned by the {@link ResultExtractor}
	 * @param <X>          a subtype of {@link Throwable} provided by <code>throwIfEmpty</code>
	 * @return an {@link ArrayList} of database objects, one for each row in <code>rs</code>
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 * @throws X                if the ResultSet is empty and <code>throwIfEmpty</code> is not null.
	 */
	protected <T, X extends Throwable> List<T> processResults(
			@NotNull ResultSet rs,
			@NotNull ResultExtractor<T> extractor,
			@NotNull Supplier<X> throwIfEmpty) throws SQLException, X {

		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");
		Objects.requireNonNull(throwIfEmpty, "Parameter 'throwIfEmpty' cannot be null.");

		return processResults(rs, null, extractor, ArrayList::new, throwIfEmpty);
	}

	/**
	 * Given a {@link ResultSet} and a {@link ResultExtractor}, generate an {@link ArrayList} of records.
	 * These records depend on what table, or set of tables the Repository operates on.
	 * <p>
	 * If <code>ctx</code> is non-null, then it will be made available in order to determine if
	 * a user is permitted to view the record.
	 * <p>
	 * If the ResultSet is not currently pointing at the first result, and if the ResultSet supports
	 * seeking backwards, then the cursor will be reset to the first row in the ResultSet. Otherwise,
	 * it is possible that some rows will be excluded from the collection of results.
	 *
	 * @param rs        the {@link ResultSet} containing table data to read
	 * @param ctx       the requesting user's {@link RequestContext}, used for permission checking (optional)
	 * @param extractor a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param <T>       the type of object that will be returned by the {@link ResultExtractor}
	 * @return an {@link ArrayList} of database objects, one for each row in <code>rs</code>
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 */
	protected <T> List<T> processResults(
			@NotNull ResultSet rs,
			@Nullable RequestContext ctx,
			@NotNull ResultExtractor<T> extractor) throws SQLException {

		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");

		return processResults(rs, ctx, extractor, ArrayList::new, null);
	}

	/**
	 * @param rs                 the {@link ResultSet} containing table data to read
	 * @param ctx                the requesting user's {@link RequestContext}, used for permission checking (optional)
	 * @param extractor          a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param collectionSupplier a function that returns a specific implementation of {@link Collection} to use for
	 *                           storing the database objects.
	 * @param <T>                the type of object that will be returned by the {@link ResultExtractor}
	 * @param <C>								 the implementation of {@link Collection} that will be returned
	 * @return a {@link Collection} of database objects, one for each row in <code>rs</code>. The collection
	 * returned depends on what is supplied by <code>collectionSupplier</code>.
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 */
	protected <T, C extends Collection<T>> C processResults(
			@NotNull ResultSet rs,
			@Nullable RequestContext ctx,
			@NotNull ResultExtractor<T> extractor,
			@NotNull Supplier<C> collectionSupplier) throws SQLException {

		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");
		Objects.requireNonNull(collectionSupplier, "Parameter 'collectionSupplier' cannot be null.");

		return processResults(rs, ctx, extractor, collectionSupplier, null);
	}

	/**
	 * Return the first result from the {@link ResultSet}, or <code>null</code>
	 * if no such result exists.
	 *
	 * @param rs        the {@link ResultSet} containing table data to read
	 * @param ctx       the requesting user's {@link RequestContext}, used for permission checking (optional)
	 * @param extractor a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param <T>       the type of object that will be returned by the {@link ResultExtractor}
	 * @return an object representing the first row of results in the {@link ResultSet}, or <code>null</code> if no results found.
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 */
	protected <T> Optional<T> firstResult(
			@NotNull ResultSet rs,
			@NotNull RequestContext ctx,
			@NotNull ResultExtractor<T> extractor) throws SQLException
	{
		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(ctx, "Parameter 'ctx' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");

		return processResults(rs, ctx, extractor).stream().findFirst();
	}

	/**
	 * Return the first result from the {@link ResultSet}. If no results were
	 * found, then the exception given by <code>throwIfEmpty</code> will be
	 * thrown.
	 *
	 * @param rs           the {@link ResultSet} containing table data to read
	 * @param extractor    a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param <T>          the type of object that will be returned by the {@link ResultExtractor}
	 * @return an object representing the first row of results in the {@link ResultSet}, or <code>null</code> if no results found.
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 */
	protected <T> Optional<T> firstResult(
			@NotNull ResultSet rs,
			@NotNull ResultExtractor<T> extractor) throws SQLException
	{
		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");

		return processResults(rs, extractor).stream().findFirst();
	}


	/***************************************************************************
	 *
	 * Private API
	 *
	 **************************************************************************/

	/**
	 * Given a {@link ResultSet} and a {@link ResultExtractor}, generate a collection of records
	 * using the given {@link Supplier} <code>collectionSupplier</code> to determine which
	 * underlying implementation of {@link Collection} to use. These records depend on what table,
	 * or set of tables the Repository operates on.
	 * <p>
	 * If <code>ctx</code> is non-null, then it will be made available in order to determine if
	 * a user is permitted to view the record.
	 * <p>
	 * If the ResultSet is not currently pointing at the first result, and if the ResultSet supports
	 * seeking backwards, then the cursor will be reset to the first row in the ResultSet. Otherwise,
	 * it is possible that some rows will be excluded from the collection of results.
	 * <p>
	 * If there are no results to return, and you wish to throw an exception instead of returning
	 * and empty list, then provide a non-null implementation of <code>throwIfEmpty</code>.
	 *
	 * @param rs                 the {@link ResultSet} containing table data to read
	 * @param ctx                the requesting user's {@link RequestContext}, used for permission checking (optional)
	 * @param extractor          a function that converts a single row in the given {@link ResultSet} to an object representation
	 * @param collectionSupplier a function that returns a specific implementation of {@link Collection} to use for
	 *                           storing the database objects.
	 * @param throwIfEmpty       exception to throw if the list is empty
	 * @param <T>                the type of object that will be returned by the {@link ResultExtractor}
	 * @param <C>								 the implementation of {@link Collection} that will be returned
	 * @param <X>                a subtype of {@link Throwable} provided by <code>throwIfEmpty</code>
	 * @return a {@link Collection} of database objects, one for each row in <code>rs</code>
	 * @throws SQLException     in the event of any issues iterating over the {@link ResultSet}
	 * @throws X                if the ResultSet is empty and <code>throwIfEmpty</code> is not null.
	 */
	private <T, C extends Collection<T>, X extends Throwable> C processResults(
			@NotNull ResultSet rs,
			@Nullable RequestContext ctx,
			@NotNull ResultExtractor<T> extractor,
			@NotNull Supplier<C> collectionSupplier,
			@Nullable Supplier<? extends X> throwIfEmpty) throws SQLException, X {

		Objects.requireNonNull(rs, "Parameter 'rs' cannot be null.");
		Objects.requireNonNull(extractor, "Parameter 'extractor' cannot be null.");
		Objects.requireNonNull(collectionSupplier, "Parameter 'collectionSupplier' cannot be null.");

		// Ensure that the cursor of the ResultSet is at the start. This method is intended
		// to be used as the exclusive processor of the ResultSet. This operation is only
		// applicable to certain ResultSet objects, so this cannot fix all issues where
		// rs.next() is called before this method.
		if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY && !rs.isFirst()) {
			rs.first();
		}

		C results = collectionSupplier.get();
		while (rs.next()) {
			try {
				results.add(extractor.extract(rs, ctx));
			} catch (PermissionDeniedException e) {
				LOG.info("Skipping record due to lack of permissions.");
			}
		}

		if (results.isEmpty() && throwIfEmpty != null) throw throwIfEmpty.get();

		return results;
	}


	/***************************************************************************
	 *
	 * Classes
	 *
	 **************************************************************************/

	/**
	 * A functional interface used to extract a row from the given {@link ResultSet}
	 * and convert it to an object representation, usable within the application.
	 *
	 * @param <T> the type of object that will hold the given row data.
	 */
	@FunctionalInterface
	protected interface ResultExtractor<T> {

		/**
		 * Given a {@link ResultSet}, read the next row and store its column data into
		 * an instance of <code>T</code>.
		 * <p>
		 * It is assumed that the cursor of the {@link ResultSet} is pointing at the record
		 * to extract.
		 * <p>
		 * If <code>ctx</code> is non-null, then it may optionally be used to perform
		 * permission checking for the user making the request. If the user does not
		 * have permission to view the record, then a {@link PermissionDeniedException}
		 * will be thrown, and the result will be skipped.
		 *
		 * @param rs  the {@link ResultSet} containing table data to read
		 * @param ctx the requesting user's {@link RequestContext}, used for permission checking (optional)
		 * @return an object containing data about the next row in <code>rs</code>
		 * @throws SQLException              if there is any problem reading from the {@link ResultSet}
		 * @throws PermissionDeniedException if the requesting user is not allowed to view the data
		 * @see AbstractDAO#processResults(ResultSet, RequestContext, ResultExtractor, Supplier, Supplier)
		 */
		T extract(@NotNull ResultSet rs, @Nullable RequestContext ctx) throws SQLException, PermissionDeniedException;
	}
}
