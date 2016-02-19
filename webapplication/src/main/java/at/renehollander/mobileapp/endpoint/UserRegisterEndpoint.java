package at.renehollander.mobileapp.endpoint;

import at.renehollander.mobileapp.entity.User;
import at.renehollander.mobileapp.repository.UserRepository;
import at.renehollander.mobileapp.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named
@Path("/register")
@Produces({MediaType.APPLICATION_JSON})
public class UserRegisterEndpoint {

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