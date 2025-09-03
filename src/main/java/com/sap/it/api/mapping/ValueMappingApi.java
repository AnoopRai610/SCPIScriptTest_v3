package com.sap.it.api.mapping;

import com.sap.it.api.ITApi;

/**
 * ValueMapping Api will be used to execute value mapping with the given parameters
 * 
 */

public interface ValueMappingApi extends ITApi {
	/** Reads/fetches the mapping with the given parameters
	 * 
	 * @param sourceAgency - the agency value for mapping to value mapping source
	 * @param sourceIdentifier - the identifier value which is uniquely represents the source of value mapping
	 * @param sourceValue - the value which is needed to identify the exact value mapping
	 * @param targetAgency - the agency value for mapping to value mapping target
	 * @param targetIdentifier - the identifier value which is uniquely represents the target of value mapping
	 */
	public String getMappedValue(String sourceAgency, String sourceSchema, String sourceValue, String targetAgency,
			String targetSchema);
}
