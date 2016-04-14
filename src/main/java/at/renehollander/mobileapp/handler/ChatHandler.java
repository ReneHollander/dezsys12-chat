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

import java.util.*;

@SocketIO(port = 8081)
public class ChatHandler implements ConnectListener, DisconnectListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChatHandler.class);

    private SocketIOServer server;

    private Map<String, Map<String, Object>> chatrooms = new HashMap<>();

    private void init() {
        {
            List<Map<String, Object>> messageList = new ArrayList<>();
            messageList.add(Maps.of("room", "Room 1", "user", "Rene8888", "date", new Date(), "text", "Hello World!"));
            messageList.add(Maps.of("room", "Room 1", "user", "Paul1032", "date", new Date(), "text", "Hi!"));
            messageList.add(Maps.of("room", "Room 1", "user", "Rene8888", "date", new Date(), "text", "fock off m8"));
            messageList.add(Maps.of("room", "Room 1", "user", "Rene8888", "date", new Date(), "text", "you gucci fam?"));
            messageList.add(Maps.of("room", "Room 1", "user", "Paul1032", "date", new Date(), "text", "sure you faggot"));
            chatrooms.put("Room 1", Maps.of("name", "Room 1", "messages", messageList));
        }
        {
            List<Map<String, Object>> messageList = new ArrayList<>();
            messageList.add(Maps.of("room", "Room 2", "user", "Paul1032", "date", new Date(), "text", "itz workening!"));
            messageList.add(Maps.of("room", "Room 2", "user", "Rene8888", "date", new Date(), "text", "yiss!"));
            messageList.add(Maps.of("room", "Room 2", "user", "Rene8888", "date", new Date(), "text", "but its shit m9"));
            messageList.add(Maps.of("room", "Room 2", "user", "Paul1032", "date", new Date(), "text", "it is\nmultiline"));
            messageList.add(Maps.of("room", "Room 2", "user", "Rene8888", "date", new Date(), "text", "thats nice"));
            chatrooms.put("Room 2", Maps.of("name", "Room 2", "messages", messageList));
        }
    }

    @Override
    public void onConnect(SocketIOClient client) {
        LOG.info("Client " + client + " connected!");
    }

    @Event("message")
    public void onChat(SocketIOClient client, Map<String, Object> data) {
        String roomid = (String) data.get("room");
        List<Object> messages;
        if (!chatrooms.containsKey(roomid)) {
            messages = new ArrayList<>();
            Map<String, Object> roomObj = new HashMap<>();
            roomObj.put("name", roomid);
            roomObj.put("messages", messages);
            chatrooms.put("room", roomObj);
        } else {
            messages = (List<Object>) chatrooms.get(roomid).get("messages");
        }
        messages.add(data);
        server.getBroadcastOperations().sendEvent("message", data);
        LOG.info(String.valueOf(data));
    }

    @Event("get_rooms")
    public void onGetRooms(SocketIOClient client, Object ignore, AckRequest ackSender) {
        ackSender.sendAckData(chatrooms);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        LOG.info("Client " + client + " disconnected!");
    }
}