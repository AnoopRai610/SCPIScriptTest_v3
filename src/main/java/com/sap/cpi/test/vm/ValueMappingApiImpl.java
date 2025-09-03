package com.sap.cpi.test.vm;

import java.util.ArrayList;
import java.util.List;

import com.sap.it.api.mapping.ValueMappingApi;

class ValueMappingApiImpl implements ValueMappingApi {
	private static List<Group> groups = new ArrayList<>();

	protected static void addGroup(Group group) {
		groups.add(group);
	}

	@Override
	public String getMappedValue(String sourceAgency, String sourceSchema, String sourceValue, String targetAgency,
			String targetSchema) {
		Entry result = null;

		for (Group group : groups) {
			Entry targetEntry = group.findEntry(sourceAgency, sourceSchema, sourceValue, targetAgency, targetSchema);
			if (targetEntry != null) {
				result = targetEntry;
				if (targetEntry.isDefault())
					return targetEntry.getValue();

			}
		}
		return ((result == null)) ? null : result.getValue();
	}
}
