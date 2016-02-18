package at.renehollander.webservices;

import at.renehollander.webservices.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserEndpoint {

    @Named
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON})
    public static class UserRegisterEndpoint {

        @Autowired
        private UserRepository userRepository;

        @POST
        public Response post(User user) {
            if (userRepository.exists(user.getEmail())) {
                return Response.status(400).entity(Maps.of("success", false)).build();
            } else {
                userRepository.save(user);
                return Response.status(201).entity(Maps.of("success", true)).build();
            }
        }
    }

    @Named
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public static class UserLoginEndpoint {

        @Autowired
        private UserRepository userRepository;

        @POST
        public Response post(User user) {
            User other = userRepository.findOne(user.getEmail());
            return Response.status(user.equals(other) ? 200 : 403).entity(Maps.of("success", user.equals(other))).build();
        }
    }
}
