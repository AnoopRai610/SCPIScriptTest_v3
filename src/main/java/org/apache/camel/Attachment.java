package org.apache.camel;

import java.util.Collection;
import java.util.List;
import javax.activation.DataHandler;

/**
 * Compatibility shim for Camel 2.x org.apache.camel.Attachment.
 * Allows old code to compile/run in Camel 3.x.
 */

public interface Attachment {
	DataHandler getDataHandler();

	String getHeader(String var1);

	List<String> getHeaderAsList(String var1);

	Collection<String> getHeaderNames();

	void setHeader(String var1, String var2);

	void addHeader(String var1, String var2);

	void removeHeader(String var1);
}