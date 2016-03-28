package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {

    private int code;
    private String description;

    public ApiError(int code) {
        this.code = code;
    }

    @JsonProperty
    public int getCode() {
        return code;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "ApiError{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
