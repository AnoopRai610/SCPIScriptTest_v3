package com.sap.cpi.test.securestore;

import com.sap.it.api.ITApi;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.spi.ITApiHandler;

class SecureStoreServiceHandler implements ITApiHandler<SecureStoreService> {

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ITApi> T getApi(Object arg0) throws InvalidContextException {
		SecureStoreService secure = new SecureStoreServiceImpl();
		return (T) secure;
	}

}
