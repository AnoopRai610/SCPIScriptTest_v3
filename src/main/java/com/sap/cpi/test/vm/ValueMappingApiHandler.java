package com.sap.cpi.test.vm;

import com.sap.it.api.ITApi;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.it.spi.ITApiHandler;

class ValueMappingApiHandler implements ITApiHandler<ValueMappingApi> {

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ITApi> T getApi(Object var1) throws InvalidContextException {
		ValueMappingApi api = new ValueMappingApiImpl();
        return (T) api;
	}    
	
}