package org.apache.ambari.views.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AFile {

    private String name;
    private Date lastModified;
    private String path;
    private short replication;
    private boolean isDirectory;
    private String owner;
    private String group;
    private String permission;
    private long accessTime;
    private long modificationTime;
    private long blockSize;
    private boolean readAcess;
    private boolean writeAccess;
    private boolean executeAccess;

    @JsonProperty
    public long getAccessTime() {
        return accessTime;
    }

    @JsonProperty
    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    @JsonProperty
    public long getBlockSize() {
        return blockSize;
    }

    @JsonProperty
    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    @JsonProperty
    public boolean isExecuteAccess() {
        return executeAccess;
    }

    @JsonProperty
    public void setExecuteAccess(boolean executeAccess) {
        this.executeAccess = executeAccess;
    }

    @JsonProperty
    public String getGroup() {
        return group;
    }

    @JsonProperty
    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty
    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    @JsonProperty
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @JsonProperty
    public boolean isReadAcess() {
        return readAcess;
    }

    public void setReadAcess(boolean readAcess) {
        this.readAcess = readAcess;
    }

    @JsonProperty
    public short getReplication() {
        return replication;
    }

    public void setReplication(short replication) {
        this.replication = replication;
    }

    @JsonProperty
    public boolean isWriteAccess() {
        return writeAccess;
    }

    public void setWriteAccess(boolean writeAccess) {
        this.writeAccess = writeAccess;
    }

    @Override
    public String toString() {
        return "AFile{" +
                "accessTime='" + accessTime + '\'' +
                ", path='" + path + '\'' +
                ", replication='" + replication + '\'' +
                ", isDirectory=" + isDirectory +
                ", owner='" + owner + '\'' +
                ", group='" + group + '\'' +
                ", permission='" + permission + '\'' +
                ", modificationTime='" + modificationTime + '\'' +
                ", blockSize='" + blockSize + '\'' +
                ", readAcess=" + readAcess +
                ", writeAccess=" + writeAccess +
                ", executeAccess=" + executeAccess +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return path.substring(path.lastIndexOf("/")+1);
    }

    @JsonProperty
    public Date getLastModified() {
        return new Date(modificationTime);
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }


    public static class AFileBuilder {
        private long accessTime;
        private String path;
        private short replication;
        private String owner;
        private String group;
        private String permission;
        private long modificationTime;
        private long blockSize;
        private boolean readAcess;
        private boolean writeAccess;
        private boolean executeAccess;

        private AFileBuilder() {
        }

        public static AFileBuilder newFile() {
            return new AFileBuilder();
        }

        public AFileBuilder withAccessTime(long accessTime) {
            this.accessTime = accessTime;
            return this;
        }

        public AFileBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public AFileBuilder withReplication(short replication) {
            this.replication = replication;
            return this;
        }

        public AFileBuilder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public AFileBuilder withGroup(String group) {
            this.group = group;
            return this;
        }

        public AFileBuilder withPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public AFileBuilder withModificationTime(long modificationTime) {
            this.modificationTime = modificationTime;
            return this;
        }

        public AFileBuilder withBlockSize(long blockSize) {
            this.blockSize = blockSize;
            return this;
        }

        public AFileBuilder withReadAcess(boolean readAcess) {
            this.readAcess = readAcess;
            return this;
        }

        public AFileBuilder withWriteAccess(boolean writeAccess) {
            this.writeAccess = writeAccess;
            return this;
        }

        public AFileBuilder withExecuteAccess(boolean executeAccess) {
            this.executeAccess = executeAccess;
            return this;
        }

        public AFileBuilder but() {
            return newFile().withAccessTime(accessTime).withPath(path).withReplication(replication).withOwner(owner).withGroup(group).withPermission(permission).withModificationTime(modificationTime).withBlockSize(blockSize).withReadAcess(readAcess).withWriteAccess(writeAccess).withExecuteAccess(executeAccess);
        }

        public AFile build() {
            AFile aFile = new AFile();
            aFile.setAccessTime(accessTime);
            aFile.setPath(path);
            aFile.setReplication(replication);
            aFile.setOwner(owner);
            aFile.setGroup(group);
            aFile.setPermission(permission);
            aFile.setModificationTime(modificationTime);
            aFile.setBlockSize(blockSize);
            aFile.setReadAcess(readAcess);
            aFile.setWriteAccess(writeAccess);
            aFile.setExecuteAccess(executeAccess);
            return aFile;
        }
    }
}
