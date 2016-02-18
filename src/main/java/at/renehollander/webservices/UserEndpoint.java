package at.renehollander.webservices;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

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
                Map<String, Object> retMap = new HashMap<>();
                retMap.put("success", false);
                return Response.status(400).entity(retMap).build();
            } else {
                userRepository.save(user);
                Map<String, Object> retMap = new HashMap<>();
                retMap.put("success", true);
                return Response.status(201).entity(retMap).build();
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
            boolean val = user.equals(other);
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("success", user.equals(other));
            return Response.status(val ? 200 : 403).entity(retMap).build();
        }
    }
}
