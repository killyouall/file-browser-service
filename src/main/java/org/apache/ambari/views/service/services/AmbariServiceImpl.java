package org.apache.ambari.views.service.services;

import com.google.common.base.Optional;
import com.google.inject.Singleton;
import org.apache.ambari.api.AmbariApiClientBuilder;
import org.apache.ambari.api.AmbariApiClientImpl;
import org.apache.ambari.api.models.ApiCredentials;
import org.apache.ambari.api.models.ConfigurationType;
import org.apache.ambari.views.service.conf.AmbariConfiguration;


public class AmbariServiceImpl implements AmbariService {

    private AmbariConfiguration ambariConfiguration;

    public AmbariServiceImpl(AmbariConfiguration ambariConfiguration) {
        this.ambariConfiguration = ambariConfiguration;
    }

    public Optional<String> getDefaultFs(String cluster) {
        AmbariApiClientImpl apiClient = makeApiClient(cluster);
        return apiClient.getActiveServiceConfigurationValue("HDFS", ConfigurationType.CORE_SITE,"fs.defaultFS");

    }

    private AmbariApiClientImpl makeApiClient(String cluster) {
        return AmbariApiClientBuilder.anAmbariApiClient().withCluster(cluster).withAmbariPort(ambariConfiguration.getPort())
                .withAmbariHost(ambariConfiguration.getHost()).
                        withCredentials(new ApiCredentials(ambariConfiguration.getUser(), ambariConfiguration.getPassword())).build();
    }





}
