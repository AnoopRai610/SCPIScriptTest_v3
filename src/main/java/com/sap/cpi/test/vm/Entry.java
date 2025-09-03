package com.sap.cpi.test.vm;

class Entry {
    private String agency;
    private String schema;
    private String value;
    private boolean isDefault;

    public Entry(String agency, String schema, String value) {
        this.agency = agency;
        this.schema = schema;
        this.value = value;
    }

    public String getAgency() {
        return agency;
    }

    public String getSchema() {
        return schema;
    }

    public String getValue() {
        return value;
    }

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}