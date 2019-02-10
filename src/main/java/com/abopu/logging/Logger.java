package com.abopu.logging;

import java.lang.reflect.Method;

/**
 * A class for using slf4j as an optional dependency.
 *
 * @author Sarah Skanes &lt;agent154@abopu.com&gt;
 */
public class Logger {
	
	/***************************************************************************
	 *
	 * Static API
	 *
	 **************************************************************************/

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz.getName());
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Logger getLogger(String name) {
		return new Logger(name);
	}
	
	
	
	/***************************************************************************
	 *
	 * Local Variables
	 *
	 **************************************************************************/
	
	private final Object		LOG;
	private final Class<?>	loggerClass;
	
	
	
	/***************************************************************************
	 *
	 * Constructors
	 *
	 **************************************************************************/

	/**
	 * 
	 * @param name
	 */
	private Logger(String name) {
		Class<?> loggerFactory;
		Method getLogger;
		Class<?> loggerClass = null;

		Object logger = null;

		try {
			loggerFactory = Class.forName("org.slf4j.LoggerFactory");
			getLogger = loggerFactory.getMethod("getLogger", String.class);
			loggerClass = Class.forName("org.slf4j.Logger");

			logger = getLogger.invoke(null, name);
		} catch (Throwable t) {
			// SLF4J is not available...
		}

		this.loggerClass = loggerClass;
		LOG = logger;
	}
	
	
	
	/***************************************************************************
	 *
	 * Public API
	 *
	 **************************************************************************/

	/**
	 * 
	 * @param message
	 */
	public void debug(String message) {
		try {
			Method debugMethod = loggerClass.getMethod("debug", String.class);
			debugMethod.invoke(LOG, message);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void debug(String message, Throwable throwable) {
		try {
			Method debugMethod = loggerClass.getMethod("debug", String.class, Throwable.class);
			debugMethod.invoke(LOG, message, throwable);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void error(String message) {
		try {
			Method errorMethod = loggerClass.getMethod("error", String.class);
			errorMethod.invoke(LOG, message);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void error(String message, Throwable throwable) {
		try {
			Method errorMethod = loggerClass.getMethod("error", String.class, Throwable.class);
			errorMethod.invoke(LOG, message, throwable);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void info(String message) {
		try {
			Method infoMethod = loggerClass.getMethod("info", String.class);
			infoMethod.invoke(LOG, message);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void info(String message, Throwable throwable) {
		try {
			Method infoMethod = loggerClass.getMethod("info", String.class, Throwable.class);
			infoMethod.invoke(LOG, message, throwable);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public boolean isDebugEnabled() {
		try {
			Method isDebugEnabled = loggerClass.getMethod("isDebugEnabled");
			return (Boolean) isDebugEnabled.invoke(LOG);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
		return false;
	}

	public boolean isErrorEnabled() {
		try {
			Method isErrorEnabled = loggerClass.getMethod("isErrorEnabled");
			return (Boolean) isErrorEnabled.invoke(LOG);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
		return false;
	}

	public boolean isInfoEnabled() {
		try {
			Method isInfoEnabled = loggerClass.getMethod("isInfoEnabled");
			return (Boolean) isInfoEnabled.invoke(LOG);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
		return false;
	}

	public boolean isTraceEnabled() {
		try {
			Method isTraceEnabled = loggerClass.getMethod("isTraceEnabled");
			return (Boolean) isTraceEnabled.invoke(LOG);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
		return false;
	}

	public boolean isWarnEnabled() {
		try {
			Method isWarnEnabled = loggerClass.getMethod("isWarnEnabled");
			return (Boolean) isWarnEnabled.invoke(LOG);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
		return false;
	}

	public void trace(String message) {
		try {
			Method traceMethod = loggerClass.getMethod("trace", String.class);
			traceMethod.invoke(LOG, message);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void trace(String message, Throwable throwable) {
		try {
			Method traceMethod = loggerClass.getMethod("trace", String.class, Throwable.class);
			traceMethod.invoke(LOG, message, throwable);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void warn(String message) {
		try {
			Method warnMethod = loggerClass.getMethod("warn", String.class);
			warnMethod.invoke(LOG, message);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}

	public void warn(String message, Throwable throwable) {
		try {
			Method warnMethod = loggerClass.getMethod("warn", String.class, Throwable.class);
			warnMethod.invoke(LOG, message, throwable);
		} catch (Throwable t) {
			// SLF4J is not available...
		}
	}
}