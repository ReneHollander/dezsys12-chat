package at.renehollander.webservices;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Named;

@Named
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(UserEndpoint.UserRegisterEndpoint.class);
        this.register(UserEndpoint.UserLoginEndpoint.class);
        this.register(JacksonFeature.class);
    }
}
