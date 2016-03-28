package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

public class Instance {

    // Used for persitance, and not a part of exposed model
    private Optional<Integer> id;

    private String name;
    private String displayName;
    private String description;

    private String tempDir;
    private String webHDFSUsername;
    private String webHDFSAuth = "auth=SIMPLE";
    private String clusterName;

    public Instance() {
    }

    public Instance(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }




    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    @JsonProperty
    public String getWebHDFSUsername() {
        return webHDFSUsername;
    }

    @JsonProperty
    public void setWebHDFSUsername(String webHDFSUsername) {
        this.webHDFSUsername = webHDFSUsername;
    }

    @JsonProperty
    public String getWebHDFSAuth() {
        return webHDFSAuth;
    }

    public void setWebHDFSAuth(String webHDFSAuth) {
        this.webHDFSAuth = webHDFSAuth;
    }

    @JsonProperty
    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    @Override
    public String toString() {
        return "Instance{" +
                "clusterName='" + clusterName + '\'' +
                ", webHDFSAuth='" + webHDFSAuth + '\'' +
                ", webHDFSUsername='" + webHDFSUsername + '\'' +
                ", tempDir='" + tempDir + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @JsonIgnore
    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }
}
