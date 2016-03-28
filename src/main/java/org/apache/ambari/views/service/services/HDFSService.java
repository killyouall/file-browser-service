package org.apache.ambari.views.service.services;

import com.google.common.base.Optional;
import org.apache.ambari.views.service.api.FileList;
import org.apache.ambari.views.service.api.Instance;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.IOException;
import java.io.InputStream;

public interface HDFSService {
    Optional<FileList> listFile(Instance instance, String listPath) throws HdfsApiException;

    Optional<FileList> mkdir(Instance instance, String path) throws HdfsApiException;

    boolean uploadFile(Instance instance, InputStream uploadedInputStream, FormDataContentDisposition contentDisposition, String path) throws HdfsApiException, IOException;
}
