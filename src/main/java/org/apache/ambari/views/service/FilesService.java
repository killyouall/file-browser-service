package org.apache.ambari.views.service;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.apache.ambari.views.service.conf.FilesServiceConfiguration;
import org.apache.ambari.views.service.conf.FilesServiceModule;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

import static org.eclipse.jetty.servlets.CrossOriginFilter.*;

public class FilesService extends Application<FilesServiceConfiguration> {


    private static final String GOOD_ORIGIN = "*";
    private static final String BAD_ORIGIN = "denied_host";

    public static final String FILES_SERVICE = "files-service";
    private GuiceBundle<FilesServiceConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
        new FilesService().run(args);
    }

    @Override
    public String getName() {
        return FILES_SERVICE;
    }


    @Override
    public void initialize(Bootstrap<FilesServiceConfiguration> bootstrap) {

        guiceBundle = GuiceBundle.<FilesServiceConfiguration>newBuilder()
                .addModule(new FilesServiceModule())
                .setConfigClass(FilesServiceConfiguration.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new SwaggerBundle<FilesServiceConfiguration>() {

            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(FilesServiceConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }

    });
    }

    public void run(FilesServiceConfiguration filesServiceConfiguration, Environment environment) throws Exception {

        environment.jersey().register(MultiPartFeature.class);
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);

        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
        filter.setInitParameter(ALLOWED_METHODS_PARAM, "GET,PUT,POST,OPTIONS");
        filter.setInitParameter(ALLOWED_ORIGINS_PARAM, GOOD_ORIGIN);
        filter.setInitParameter(ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept, last-file, target-path");
        filter.setInitParameter(ALLOW_CREDENTIALS_PARAM, "true");

    }

}
