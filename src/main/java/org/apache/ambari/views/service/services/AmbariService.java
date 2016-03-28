package org.apache.ambari.views.service.services;


import com.google.common.base.Optional;

/**
 * Super quick and dirty way to get the data needed for this file view
 * Eventualy get this off a client API
 */
public interface AmbariService {

    Optional<String> getDefaultFs(String cluster);






}
