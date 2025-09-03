package com.sap.cpi.test.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.sap.it.api.impl.ITApiFactoryRegistry;
import com.sap.it.spi.ITApiHandler;

public class RegisterService {
	public static void register(String serviceName, ITApiHandler<?> vmApi) throws Exception {
		Class<?> registryClass = ITApiFactoryRegistry.class;
		Field field = registryClass.getDeclaredField("factories");
		field.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, ITApiHandler<?>> factories =  (Map<String, ITApiHandler<?>>) field.get(HashMap.class);
		if(!factories.containsKey(serviceName))
			factories.put(serviceName, vmApi);
	}
}
