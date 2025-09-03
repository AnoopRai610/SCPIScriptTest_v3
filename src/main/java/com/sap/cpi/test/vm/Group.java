package com.sap.cpi.test.vm;

import java.util.List;

class Group {
    private String id;
    private List<Entry> entries;

    public Group(String id, List<Entry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public Entry findEntry(String agency1, String schema1,String value,String agency2, String schema2) {
        if(agency1.equals(agency2) && schema1.equals(schema2))
        	return null;
    	
        Entry entry1 = findEntry(agency1, schema1, value);
        
        if(entry1!=null)
        	return findEntryByAgencyAndSchema(agency2,schema2);
        
        return null;
        
    }
    
    private Entry findEntry(String agency, String schema, String value) {
        return entries.stream()
                .filter(e -> e.getAgency().equals(agency) &&
                             e.getSchema().equals(schema) &&
                             e.getValue().equals(value))
                .findFirst().orElse(null);
    }

    private Entry findEntryByAgencyAndSchema(String agency, String schema) {
        return entries.stream()
                .filter(e -> e.getAgency().equals(agency) &&
                             e.getSchema().equals(schema))
                .findFirst().orElse(null);
    }
}
