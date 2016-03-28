package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileList {

    private List<AFile> files;

    public FileList(List<AFile> files) {
        this.files = files;
    }

    @JsonProperty
    public List<AFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "FileList{" +
                "files=" + files +
                '}';
    }
}
