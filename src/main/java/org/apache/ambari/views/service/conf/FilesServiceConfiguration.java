package org.apache.ambari.views.service.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Singleton;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class FilesServiceConfiguration extends Configuration {


    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }


    @Valid
    @NotNull
    @JsonProperty("ambari")
    private AmbariConfiguration ambariConfiguration = new AmbariConfiguration();

    public void setAmbariConfiguration(AmbariConfiguration ambariConfiguration) {
        this.ambariConfiguration = ambariConfiguration;
    }

    public AmbariConfiguration getAmbariConfiguration() {
        return ambariConfiguration;
    }

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

}
