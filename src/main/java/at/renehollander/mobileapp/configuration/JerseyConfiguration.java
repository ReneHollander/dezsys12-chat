package at.renehollander.mobileapp.configuration;

import at.renehollander.mobileapp.endpoint.UserLoginEndpoint;
import at.renehollander.mobileapp.endpoint.UserRegisterEndpoint;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Named;

@Named
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        this.register(UserRegisterEndpoint.class);
        this.register(UserLoginEndpoint.class);
        this.register(JacksonFeature.class);
    }
}
