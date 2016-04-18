package at.renehollander.mobileapp.handler;

import at.renehollander.mobileapp.annotation.Event;
import at.renehollander.mobileapp.annotation.SocketIO;
import at.renehollander.mobileapp.util.Maps;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SocketIO(port = 8081)
public class ChatHandler implements ConnectListener, DisconnectListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChatHandler.class);

    private SocketIOServer server;

    private Map<String, Map<String, Object>> chatrooms = new HashMap<>();

    @Override
    public void onConnect(SocketIOClient client) {
        LOG.info("Client " + client + " connected!");
    }

    @Event("message")
    @SuppressWarnings("unchecked")
    public void onChat(SocketIOClient client, Map<String, Object> data) {
        // Get room id from request
        String roomid = (String) data.get("room");
        List<Object> messages;
        // if a room with the given id exists, append message,otherwise create new room
        if (!chatrooms.containsKey(roomid)) {
            // creates a new room
            messages = new ArrayList<>();
            Map<String, Object> roomObj = new HashMap<>();
            roomObj.put("name", roomid);
            roomObj.put("messages", messages);
            chatrooms.put("room", roomObj);
        } else {
            messages = (List<Object>) chatrooms.get(roomid).get("messages");
        }
        // append message and inform clients
        messages.add(data);
        server.getBroadcastOperations().sendEvent("message", data);
    }

    @Event("get_rooms")
    public void onGetRooms(SocketIOClient client, Object ignore, AckRequest ackSender) {
        // send all chatrooms to the client
        ackSender.sendAckData(chatrooms);
    }

    @Event("new_room")
    public void onNewRoom(SocketIOClient client, Map<String, Object> data, AckRequest ackSender) {
        // create a new room with the given name and inform clients
        chatrooms.put((String) data.get("room"), Maps.of("name", data.get("room"), "messages", new ArrayList<>()));
        server.getBroadcastOperations().sendEvent("new_room", data);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        LOG.info("Client " + client + " disconnected!");
    }
}