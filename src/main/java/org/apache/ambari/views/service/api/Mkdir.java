package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mkdir {

    private String path;

    @JsonProperty(required = true)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Mkdir{" +
                "path='" + path + '\'' +
                '}';
    }
}
