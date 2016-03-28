package org.apache.ambari.views.service.db;

import org.apache.ambari.views.service.api.Instance;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface InstanceDao {

    @SqlUpdate("insert into fileservice.instance (i_name,i_display_name,i_description," +
            "i_cluster_name,i_tempDir,i_webHDFSUsername,i_webHDFSAuth) " +
            "values (:name,:displayName,:description,:clusterName,:tempDir,:webHDFSUsername,:webHDFSAuth)")
    void insert(@Bind("name") String name, @Bind("displayName") String displayName, @Bind("description") String description,
                @Bind("clusterName") String clusterName, @Bind("tempDir") String tempDir,
                @Bind("webHDFSUsername") String webHDFSUsername, @Bind("webHDFSAuth") String webHDFSAuth);


    @SqlQuery("select * from fileservice.instance where i_name = :name")
    @Mapper(InstanceMapper.class)
    Instance select(@Bind("name") String instanceId);
}
