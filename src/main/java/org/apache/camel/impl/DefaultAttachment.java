package org.apache.camel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import org.apache.camel.Attachment;
import org.apache.camel.util.CollectionHelper;

@SuppressWarnings("unchecked")
public class DefaultAttachment implements Attachment {
    private Map<String, Object> headers;
    private DataHandler dataHandler;

    public DefaultAttachment(DataHandler dh) {
        this.dataHandler = dh;
    }

    public DefaultAttachment(DataSource ds) {
        this.dataHandler = new DataHandler(ds);
    }

    public DataHandler getDataHandler() {
        return this.dataHandler;
    }

    public String getHeader(String name) {
        if (this.headers != null) {
            Object headerObject = this.headers.get(name);
            if (headerObject instanceof String) {
                return (String) headerObject;
            }

            if (headerObject instanceof Collection) {
                return CollectionHelper.collectionAsCommaDelimitedString((Collection<String>) headerObject);
            }
        }

        return null;
    }

    public List<String> getHeaderAsList(String name) {
        if (this.headers != null) {
            Object headerObject = this.headers.get(name);
            if (headerObject instanceof List) {
                return (List<String>) headerObject;
            }

            if (headerObject instanceof String) {
                return Collections.singletonList((String) headerObject);
            }
        }

        return null;
    }

    public void addHeader(String headerName, String headerValue) {
        if (this.headers == null) {
            this.headers = this.createHeaders();
        }

        CollectionHelper.appendValue(this.headers, headerName, headerValue);
    }

    public void setHeader(String headerName, String headerValue) {
        if (this.headers == null) {
            this.headers = this.createHeaders();
        }

        this.headers.put(headerName, headerValue);
    }

    public void removeHeader(String headerName) {
        if (this.headers != null) {
            this.headers.remove(headerName);
        }

    }

    public Collection<String> getHeaderNames() {
        if (this.headers == null) {
            this.headers = this.createHeaders();
        }

        return this.headers.keySet();
    }

    public void clearHeaders() {
        this.headers = null;
    }

    private Map<String, Object> createHeaders() {
        return new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
    }

    public boolean equals(Object other) {
        if (other instanceof Attachment) {
            DataHandler otherDh = ((Attachment) other).getDataHandler();
            return this.dataHandler.equals(otherDh);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.dataHandler.hashCode();
    }
}