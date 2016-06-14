package org.apache.ambari.views.service.services;

import com.google.common.base.Optional;
import org.apache.ambari.views.service.conf.AmbariConfiguration;


public class AmbariServiceImpl implements AmbariService {

    private AmbariConfiguration ambariConfiguration;

    public AmbariServiceImpl(AmbariConfiguration ambariConfiguration) {
        this.ambariConfiguration = ambariConfiguration;
    }

    public Optional<String> getDefaultFs(String cluster) {

        return Optional.of(ambariConfiguration.getDefaultFs());
    }







}
