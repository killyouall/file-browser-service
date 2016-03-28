package org.apache.ambari.views.service.conf;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.apache.ambari.views.service.db.DbService;
import org.apache.ambari.views.service.db.DbServiceImpl;
import org.apache.ambari.views.service.db.InstanceDao;
import org.apache.ambari.views.service.services.AmbariService;
import org.apache.ambari.views.service.services.AmbariServiceImpl;
import org.apache.ambari.views.service.services.HDFSService;
import org.apache.ambari.views.service.services.HDFSServiceImpl;
import org.skife.jdbi.v2.DBI;


public class FilesServiceModule extends AbstractModule {


    private DBI jdbi;
    private AmbariServiceImpl ambariService;

    public void configure() {

        bind(DbService.class).to(DbServiceImpl.class);
        bind(HDFSService.class).to(HDFSServiceImpl.class);
    }

    @Provides
    public DBI provideDBI(Environment environment, FilesServiceConfiguration filesServiceConfiguration) {
        if (jdbi == null) {
            final DBIFactory factory = new DBIFactory();
            DataSourceFactory dataSourceFactory = filesServiceConfiguration.getDataSourceFactory();
            dataSourceFactory.setLogValidationErrors(true);
            jdbi = factory.build(environment, dataSourceFactory, "postgresql");
        }
        return jdbi;
    }

    @Provides
    public InstanceDao instanceDao(DBI dbi) {
        return dbi.onDemand(InstanceDao.class);
    }

    @Provides
    public AmbariService providesSomethingThatNeedsConfiguration(FilesServiceConfiguration configuration) {
        if (ambariService == null)
            ambariService = new AmbariServiceImpl(configuration.getAmbariConfiguration());
        return ambariService;
    }


}
