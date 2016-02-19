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
@Path("/login")
@Produces({MediaType.APPLICATION_JSON})
public class UserLoginEndpoint {

    @Autowired
    private UserRepository userRepository;

    @POST
    public Response post(User user) {
        User other = userRepository.findOne(user.getEmail());
        return Response.status(user.equals(other) ? 200 : 403).entity(Maps.of("success", user.equals(other))).build();
    }
}
