package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InstanceList {

    private List<Instance> instances;

    public InstanceList(List<Instance> instances) {
        this.instances = instances;
    }

    @JsonProperty
    public List<Instance> getInstances() {
        return instances;
    }

    @Override
    public String toString() {
        return "InstanceList{" +
                "instances=" + instances +
                '}';
    }
}
