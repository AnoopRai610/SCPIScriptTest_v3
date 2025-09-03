package com.sap.cpi.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class Utility {

	public static String readFilesToString(File file) {
		FileInputStream io = null;
		String var5;
		try {
			io = new FileInputStream(file);
			byte[] byt = new byte[io.available()];
			io.read(byt);
			var5 = new String(byt);
		} catch (Exception var12) {
			throw new RuntimeException(var12.getMessage(), var12);
		} finally {
			if (io != null) {
				try {
					io.close();
				} catch (IOException var11) {
					throw new RuntimeException(var11.getMessage(), var11);
				}
			}
		}
		return var5;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAccessToProtectedClass(String className, Class<T> type) throws Exception {
		Class<?> clazz = Class.forName(className);

		Constructor<?> constructor = clazz.getDeclaredConstructor();
		constructor.setAccessible(true);

		return (T) constructor.newInstance();

	}

}
