package com.abopu.data.bind;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sarah Skanes
 * @created May 14, 2017.
 */
public class BindObjects {
	
	private Map<String, Object> bindingMap;
	
	public void setObject(String key, Object value) {
		bindingMap.put(key, value);
	}
	
	public String toParameterString() {
		return String.join(",", bindingMap.keySet().stream().map(key -> key + " = ?").collect(Collectors.toList()));
	}
}
