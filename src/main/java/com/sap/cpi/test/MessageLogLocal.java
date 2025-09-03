package com.sap.cpi.test;

import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.msglog.MessageLog;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

class MessageLogLocal implements MessageLog {
	private Object message;

	public MessageLogLocal(Object message) {
		this.message = message;
	}

	public void addAttachmentAsString(String name, String text, String mediaType) {
		System.out.println("This is test message..");
		System.out.println("Attachment Name : " + name);
		System.out.println("********************************************************************");
		System.out.println(text);

		try {
			Message message = (Message) this.message;
			if (message.getProperty("attachmentPath") != null
					&& !message.getProperty("attachmentPath").toString().isEmpty()) {
				OutputStream os = Files.newOutputStream(
						Paths.get(message.getProperty("attachmentPath").toString(), name), StandardOpenOption.CREATE);
				os.write(text.getBytes());
				os.flush();
				os.close();
				System.out.println(
						"Log file created at " + Paths.get(message.getProperty("attachmentPath").toString(), name));
			}

		} catch (Exception var6) {
			throw new RuntimeException(var6.getMessage(), var6);
		}
	}

	public void addCustomHeaderProperty(String arg0, String arg1) {
	}

	public void setBooleanProperty(String arg0, Boolean arg1) {
	}

	public void setDateProperty(String arg0, Date arg1) {
	}

	public void setDoubleProperty(String arg0, Double arg1) {
	}

	public void setFloatProperty(String arg0, Float arg1) {
	}

	public void setIntegerProperty(String arg0, Integer arg1) {
	}

	public void setLongProperty(String arg0, Long arg1) {
	}

	public void setStringProperty(String arg0, String arg1) {
	}
}