package org.apache.ambari.views.service.services;

//import org.apache.hadoop.conf.Configuration;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.ambari.views.service.api.AFile;
import org.apache.ambari.views.service.api.FileList;
import org.apache.ambari.views.service.api.Instance;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.web.WebHdfsFileSystem;
import org.apache.hadoop.security.UserGroupInformation;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HDFSServiceImpl implements HDFSService {


    private final static Logger LOG =
            LoggerFactory.getLogger(HDFSServiceImpl.class);


    @Inject
    private AmbariService ambariService;


    public Optional<FileList> listFile(Instance instance, final String listPath) throws HdfsApiException {
        final Configuration conf = new Configuration();

        Optional<String> defaultFs = ambariService.getDefaultFs(instance.getClusterName());
        Preconditions.checkState(defaultFs.isPresent());
        String hdfs = defaultFs.get();
        String hdfsUsername = instance.getWebHDFSUsername();

        dieIfInvalid(hdfs);
        UserGroupInformation proxyUser = setUpConfig(conf, hdfs, hdfsUsername);

        final FileSystem fs = execute(new PrivilegedExceptionAction<FileSystem>() {
            public FileSystem run() throws IOException {
                return FileSystem.get(conf);
            }
        }, proxyUser);


        FileStatus[] fileStatuses = execute(new PrivilegedExceptionAction<FileStatus[]>() {
            public FileStatus[] run() throws FileNotFoundException, Exception {
                return fs.listStatus(new Path(listPath));
            }
        }, proxyUser);

        closeFs(fs);

        FileList fileListFromStatuses = getFileListFromStatuses(fileStatuses, proxyUser);
        return Optional.fromNullable(fileListFromStatuses);
    }

    private UserGroupInformation setUpConfig(Configuration conf, String hdfs, String hdfsUsername) throws HdfsApiException {
        UserGroupInformation currentUser = getCurrentUser();
        UserGroupInformation proxyUser = UserGroupInformation.createProxyUser(hdfsUsername, currentUser);
        conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        conf.set("fs.webhdfs.impl", WebHdfsFileSystem.class.getName());
        conf.set("fs.file.impl", LocalFileSystem.class.getName());
        conf.set("hadoop.security.authentication", "simple");
        conf.set("fs.defaultFS", hdfs);
        return proxyUser;
    }


    public Optional<FileList> mkdir(Instance instance, final String path) throws HdfsApiException {
        final Configuration conf = new Configuration();

        Optional<String> defaultFs = ambariService.getDefaultFs(instance.getClusterName());
        Preconditions.checkState(defaultFs.isPresent());
        String hdfs = defaultFs.get();
        String hdfsUsername = instance.getWebHDFSUsername();

        dieIfInvalid(hdfs);
        UserGroupInformation proxyUser = setUpConfig(conf, hdfs, hdfsUsername);

        final FileSystem fs = execute(new PrivilegedExceptionAction<FileSystem>() {
            public FileSystem run() throws IOException {
                return FileSystem.get(conf);
            }
        }, proxyUser);

        Boolean result = execute(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws FileNotFoundException, Exception {
                return fs.mkdirs(new Path(path));
            }
        }, proxyUser);

        FileStatus fileStatus = getFileStatus(path, fs);
        closeFs(fs);
        FileList fileListFromStatuses = getFileListFromStatuses(new FileStatus[]{fileStatus}, proxyUser);
        return Optional.fromNullable(fileListFromStatuses);
    }

    public boolean uploadFile(Instance instance, InputStream uploadedInputStream,
                              FormDataContentDisposition contentDisposition, String path) throws HdfsApiException, IOException {
        final Configuration conf = new Configuration();
        Optional<String> defaultFs = ambariService.getDefaultFs(instance.getClusterName());
        Preconditions.checkState(defaultFs.isPresent());
        String hdfs = defaultFs.get();
        String hdfsUsername = instance.getWebHDFSUsername();

        dieIfInvalid(hdfs);
        UserGroupInformation proxyUser = setUpConfig(conf, hdfs, hdfsUsername);

        final FileSystem fs = execute(new PrivilegedExceptionAction<FileSystem>() {
            public FileSystem run() throws IOException {
                return FileSystem.get(conf);
            }
        }, proxyUser);

        String filePath = path;
        //construct a file path at the location
        if(!path.endsWith("/")){
            filePath = path +"/";
        }

        String finalFile = filePath + contentDisposition.getFileName();

        int read;
        byte[] chunk = new byte[1024];
        FSDataOutputStream out = null;
        try {
            out = create(fs,finalFile, false,proxyUser);
            while ((read = uploadedInputStream.read(chunk)) != -1) {
                out.write(chunk, 0, read);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return true;

    }

    public FSDataOutputStream create(final FileSystem fs, final String path, final boolean overwrite,UserGroupInformation ugi)
            throws HdfsApiException {
        return execute(new PrivilegedExceptionAction<FSDataOutputStream>() {
            public FSDataOutputStream run() throws Exception {
                return fs.create(new Path(path), overwrite);
            }
        },ugi);
    }

    private FileStatus getFileStatus(String path, FileSystem fs) throws HdfsApiException {
        try {
            return fs.getFileStatus(new Path(path));
        } catch (IOException e) {
            throw new HdfsApiException("Cannot get file status",e);
        }
    }


    private FileList getFileListFromStatuses(FileStatus[] fileStatuses, UserGroupInformation proxyUser) {
        ArrayList<AFile> files = Lists.newArrayList();
        for (FileStatus fileStatus : fileStatuses) {
            files.add(getFileFromStatus(fileStatus,proxyUser));
        }
        return new FileList(files);

    }

    private AFile getFileFromStatus(FileStatus status, UserGroupInformation ugi) {
        AFile aFile = AFile.AFileBuilder.newFile().withPath(Path.getPathWithoutSchemeAndAuthority(status.getPath()).toString())
                .withReplication(status.getReplication())
                .withAccessTime(status.getAccessTime())
                .withModificationTime(status.getModificationTime())
                .withBlockSize(status.getBlockSize())
                .withGroup(status.getGroup())
                .withOwner(status.getOwner())
                .withPermission(permissionToString(status.getPermission()))
                .withReadAcess(checkAccessPermissions(status, FsAction.READ, ugi))
                .withWriteAccess(checkAccessPermissions(status, FsAction.WRITE, ugi))
                .withExecuteAccess(checkAccessPermissions(status, FsAction.EXECUTE, ugi))
                .build();
        aFile.setDirectory(status.isDirectory());
        return aFile;
    }

    public static boolean checkAccessPermissions(FileStatus stat, FsAction mode, UserGroupInformation ugi) {
        FsPermission perm = stat.getPermission();
        String user = ugi.getShortUserName();
        List<String> groups = Arrays.asList(ugi.getGroupNames());
        if (user.equals(stat.getOwner())) {
            if (perm.getUserAction().implies(mode)) {
                return true;
            }
        } else if (groups.contains(stat.getGroup())) {
            if (perm.getGroupAction().implies(mode)) {
                return true;
            }
        } else {
            if (perm.getOtherAction().implies(mode)) {
                return true;
            }
        }
        return false;
    }



    private static String permissionToString(FsPermission p) {
        return (p == null) ? "default" : "-" + p.getUserAction().SYMBOL
                + p.getGroupAction().SYMBOL + p.getOtherAction().SYMBOL;
    }

    private void closeFs(FileSystem fs)  {
        try {
            fs.close();
        } catch (IOException e) {
            LOG.warn("Tried to close the fileSystem but failed",e);
        }
    }


    public <T> T execute(PrivilegedExceptionAction<T> action,UserGroupInformation ugi)
            throws HdfsApiException {
        try {
            T result = null;

            // Retry strategy applied here due to HDFS-1058. HDFS can throw random
            // IOException about retrieving block from DN if concurrent read/write
            // on specific file is performed (see details on HDFS-1058).
            int tryNumber = 0;
            boolean succeeded = false;
            do {
                tryNumber += 1;
                try {
                    result = ugi.doAs(action);
                    succeeded = true;
                } catch (IOException ex) {
                    if (!ex.getMessage().contains("Cannot obtain block length for")) {
                        throw ex;
                    }
                    if (tryNumber >= 3) {
                        throw ex;
                    }
                    LOG.info("HDFS threw 'IOException: Cannot obtain block length' exception. " +
                            "Retrying... Try #" + (tryNumber + 1));
                    Thread.sleep(1000);  //retry after 1 second
                }
            } while (!succeeded);
            return result;
        } catch (Exception e){
            throw new HdfsApiException("Could not connect to HDFS",e);
        }
    }



    private void dieIfInvalid(String hdfs) throws HdfsApiException {
        try {
            new URI(hdfs);
        } catch (URISyntaxException e) {
            throw new HdfsApiException("URI format incorrect for:"+hdfs,e);
        }
    }

    private UserGroupInformation getCurrentUser() throws HdfsApiException {
        try {
            return UserGroupInformation.getCurrentUser();
        } catch (IOException e) {
            LOG.error("Failed to get current user",e);
            throw new HdfsApiException("Cannot get current user",e);
        }
    }
}
