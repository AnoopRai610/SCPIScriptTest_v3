package com.sap.cpi.test;

import com.sap.it.api.msglog.MessageLog;

class MessageLogFactory implements com.sap.it.api.msglog.MessageLogFactory {
	public MessageLog getMessageLog(Object obj) {
		return new MessageLogLocal(obj);
	}
}