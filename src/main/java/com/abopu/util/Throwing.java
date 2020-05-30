package com.abopu.util;

/**
 * @author Sarah Skanes &lt;agent154@abopu.com&gt;
 */
public class Throwing {

	/**
	 * Taken from https://www.baeldung.com/java-sneaky-throws
	 */
	public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
		throw (E) e;
	}
}
