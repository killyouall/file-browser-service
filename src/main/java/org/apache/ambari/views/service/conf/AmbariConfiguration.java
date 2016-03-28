package org.apache.ambari.views.service.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AmbariConfiguration {

    @NotEmpty
    @JsonProperty
    private String host;

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port = 80;

    @JsonProperty
    private String user = "admin";

    @JsonProperty
    private String password = "admin";


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
