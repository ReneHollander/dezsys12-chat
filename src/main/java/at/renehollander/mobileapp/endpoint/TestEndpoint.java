package at.renehollander.mobileapp.endpoint;

import at.renehollander.mobileapp.handler.ChatHandler;
import at.renehollander.mobileapp.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;

@Named
@Path("/test")
public class TestEndpoint {

    @Autowired
    private ChatHandler chatHandler;

    @GET
    public Response get() {
        System.out.println("here");
        chatHandler.onChat(null, Maps.of("room", "Room 1", "user", "Rene8888", "date", new Date(), "text", "hellooooooo"));
        chatHandler.onChat(null, Maps.of("room", "Room 2", "user", "Rene8888", "date", new Date(), "text", "hellooooooo"));
        return Response.status(200).build();
    }

}
