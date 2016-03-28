package org.apache.ambari.views.service.db;

import com.google.common.base.Optional;
import org.apache.ambari.views.service.api.Instance;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InstanceMapper implements ResultSetMapper<Instance> {
    public Instance map(int i, ResultSet r, StatementContext statementContext) throws SQLException {

        Instance instance = new Instance(r.getString("i_name"), r.getString("i_display_name"), r.getString("i_description"));
        instance.setId(Optional.of(r.getInt("i_id")));
        instance.setClusterName(r.getString("i_cluster_name"));
        instance.setTempDir(r.getString("i_tempDir"));
        instance.setWebHDFSAuth(r.getString("i_webHDFSAuth"));
        instance.setWebHDFSUsername(r.getString("i_webHDFSUsername"));
        return instance;
    }
}
