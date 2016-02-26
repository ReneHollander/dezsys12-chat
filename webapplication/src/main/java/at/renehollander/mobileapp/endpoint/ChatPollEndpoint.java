package at.renehollander.mobileapp.endpoint;

import at.renehollander.mobileapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Named
@Path("/login")
@Produces({MediaType.APPLICATION_JSON})
public class ChatPollEndpoint {


    @Autowired
    private UserRepository userRepository;

    @POST
    public DeferredResult<List<Map<String, Object>>> get() {
        return null;
    }
}
