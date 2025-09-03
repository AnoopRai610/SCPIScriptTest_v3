package com.sap.cpi.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.camel.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;

import com.sap.cpi.test.util.Utility;
import com.sap.gateway.ip.core.customdev.util.AttachmentWrapper;
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.gateway.ip.core.customdev.util.SoapHeader;

class MessageImpl implements Message {
	private Object body;
	private Map<String, Object> headers;
	private Map<String, Object> properties;
	private Map<String, DataHandler> attachments;
	private Map<String, AttachmentWrapper> attachmentWrapperObjects;
	private Map<String, Attachment> attachmentObjects;
	private Exchange exchange;
	private List<SoapHeader> soapHeaders;

	protected MessageImpl(File file) {
		this();
		try {
			if (file.isFile()) {
				byte[] b = exchange.getContext().getTypeConverter().convertTo(byte[].class, new FileInputStream(file));
				InputStreamCache inputStreamCache = new InputStreamCache(b);
				this.setBody(inputStreamCache);
				this.setProperty("attachmentPath", file.getParent());
			} else {
				this.setProperty("attachmentPath", file.getPath());
				File[] var5;
				int var4 = (var5 = file.listFiles()).length;

				for (int var3 = 0; var3 < var4; ++var3) {
					File f = var5[var3];
					if (!f.isDirectory()) {
						if (f.getName().matches("(?i).*body.*")) {
							byte[] b = exchange.getContext().getTypeConverter().convertTo(byte[].class,
									new FileInputStream(f));
							InputStreamCache inputStreamCache = new InputStreamCache(b);
							this.setBody(inputStreamCache);
						} else {
							int var7;
							int var8;
							String[] var9;
							String property;
							if (f.getName().matches("(?i).*header.*")) {
								var8 = (var9 = Utility.readFilesToString(f).split(System.lineSeparator())).length;

								for (var7 = 0; var7 < var8; ++var7) {
									property = var9[var7];
									this.setHeader(property.split("/t")[0], property.split("/t")[1]);
								}
							} else if (f.getName().matches("(?i).*property.*")) {
								var8 = (var9 = Utility.readFilesToString(f).split(System.lineSeparator())).length;

								for (var7 = 0; var7 < var8; ++var7) {
									property = var9[var7];
									this.setProperty(property.split("/t")[0], property.split("/t")[1]);
								}
							} else if (f.getName().matches("(?).*attachment.*")) {
								DataSource ds = new FileDataSource(f);
								this.addAttachmentObject(f.getName(), new AttachmentWrapper(ds));
							}
						}
					}
				}
			}
		} catch (Exception var10) {
			throw new RuntimeException(var10.getMessage(), var10);
		}
	}

	MessageImpl() {
		DefaultCamelContext context = new DefaultCamelContext();
		context.setStreamCaching(true);
		this.exchange = new DefaultExchange(context);

		this.body = null;
		this.attachments = new HashMap<String, DataHandler>();
		this.attachmentWrapperObjects = new HashMap<String, AttachmentWrapper>();
	}

	protected void loadDataToExchange() {
		if (this.getBody() != null)
			exchange.getIn().setBody(this.getBody());
		if (this.getHeaders() != null)
			exchange.getIn().setHeaders(this.getHeaders());
		if (this.getProperties() != null)
			this.getProperties().forEach((K, V) -> {
				exchange.setProperty(K, V);
			});
	}

	public <T> T getBody(Class<T> type) {
		try {
			return this.exchange.getIn().getBody(type);
		} catch (TypeConversionException e) {
			throw e;
		}
	}

	public Object getBody() {
		return this.body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public long getBodySize() {
		return ((InputStreamCache) this.body).length();
	}

	public Map<String, Object> getHeaders() {
		return this.headers;
	}

	public <T> T getHeader(String var1, Class<T> var2) {
		try {
			if (this.exchange.getIn().getHeader(var1) == null) {
				return null;
			} else {
				T obj = this.exchange.getIn().getHeader(var1, var2);
				return obj;
			}
		} catch (TypeConversionException e) {
			throw e;
		}
	}

	public void setHeaders(Map<String, Object> var1) {
		this.headers = var1;
	}

	public void setHeader(String var1, Object var2) {
		if (this.headers == null)
			this.headers = new HashMap<>();

		this.headers.put(var1, var2);
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> exchangeProperties) {
		this.properties = exchangeProperties;
	}

	public void setProperty(String name, Object value) {
		if (this.properties == null) {
			this.properties = new HashMap<>();
		}

		this.properties.put(name, value);
	}

	public Object getProperty(String name) {
		return this.properties != null ? this.properties.get(name) : null;
	}

	public long getAttachmentsSize() {
		return (long) this.getAttachmentWrapperObjects().size();
	}

	public void addAttachmentHeader(String headerName, String headerValue, AttachmentWrapper attachment) {
		attachment.addHeader(headerName, headerValue);
	}

	public void setAttachmentHeader(String headerName, String headerValue, AttachmentWrapper attachment) {
		attachment.setHeader(headerName, headerValue);
	}

	public String getAttachmentHeader(String headerName, AttachmentWrapper attachment) {
		return attachment.getHeader(headerName);
	}

	public void removeAttachmentHeader(String headerName, AttachmentWrapper attachment) {
		attachment.removeHeader(headerName);
	}

	public Map<String, AttachmentWrapper> getAttachmentWrapperObjects() {
		return this.attachmentWrapperObjects;
	}

	public void setAttachmentWrapperObjects(Map<String, AttachmentWrapper> attachmentObjects) {
		this.attachmentWrapperObjects = attachmentObjects;
	}

	public void addAttachmentObject(String id, AttachmentWrapper content) {
		this.attachmentWrapperObjects.put(id, content);
	}

	@Deprecated
	public void addAttachmentHeader(String headerName, String headerValue, Attachment attachment) {
		attachment.addHeader(headerName, headerValue);
	}

	@Deprecated
	public void setAttachmentHeader(String headerName, String headerValue, Attachment attachment) {
		attachment.setHeader(headerName, headerValue);
	}

	@Deprecated
	public String getAttachmentHeader(String headerName, Attachment attachment) {
		return attachment.getHeader(headerName);
	}

	@Deprecated
	public void removeAttachmentHeader(String headerName, Attachment attachment) {
		attachment.removeHeader(headerName);
	}

	@Deprecated
	public Map<String, Attachment> getAttachmentObjects() {
		return this.attachmentObjects;
	}

	@Deprecated
	public void setAttachmentObjects(Map<String, Attachment> attachmentObjects) {
		this.attachmentObjects = attachmentObjects;
	}

	@Deprecated
	public void addAttachmentObject(String id, Attachment content) {
		this.attachmentObjects.put(id, content);
	}

	public Map<String, DataHandler> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Map<String, DataHandler> attachments) {
		this.attachments = attachments;
	}

	@Override
	public List<SoapHeader> getSoapHeaders() {
		return this.soapHeaders;
	}

	@Override
	public void setSoapHeaders(List<SoapHeader> soapHeaders) {
		this.soapHeaders = soapHeaders;
	}

	@Override
	public void clearSoapHeaders() {
		this.soapHeaders.clear();
	}
}