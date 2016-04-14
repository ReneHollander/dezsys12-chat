package at.renehollander.mobileapp.handler;

import at.renehollander.mobileapp.annotation.Event;
import at.renehollander.mobileapp.annotation.SocketIO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SocketIO(port = 8081)
public class ChatHandler implements ConnectListener, DisconnectListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChatHandler.class);

    private SocketIOServer server;

    private void init() {
    }

    @Override
    public void onConnect(SocketIOClient client) {
        LOG.info("Client " + client + " connected!");
    }

    @Event("chat")
    public void onChat(SocketIOClient client, String data, AckRequest ackSender) {
        LOG.info("Client " + client + " sent " + data);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        LOG.info("Client " + client + " disconnected!");
    }
}