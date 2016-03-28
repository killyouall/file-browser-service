package org.apache.ambari.views.service.db;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.ambari.views.service.api.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbServiceImpl implements DbService {

    private final static Logger LOG =
            LoggerFactory.getLogger(DbServiceImpl.class);
    @Inject
    private InstanceDao instanceDao;

    public Instance persistInstance(Instance toPersist){
          LOG.debug("persisting instance:"+toPersist);
          instanceDao.insert(toPersist.getName(),toPersist.getDisplayName(),toPersist.getDescription(),
                              toPersist.getClusterName(),toPersist.getTempDir(),toPersist.getWebHDFSUsername(),toPersist.getWebHDFSAuth());
          return toPersist;
    }

    public Optional<Instance> loadInstance(String instanceID) {
        LOG.debug("Loading instance with name: "+instanceID);
        try {
            Instance instance = instanceDao.select(instanceID);
            Preconditions.checkNotNull(instance);
            LOG.debug("Loaded instance");
            return Optional.of(instance);
        } catch (Exception e){
            LOG.error("Error loading instance",e);
            return Optional.absent();
        }

    }

    ;






}
