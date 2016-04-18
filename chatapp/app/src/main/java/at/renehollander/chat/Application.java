package at.renehollander.chat;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import at.renehollander.chat.model.ChatRoom;
import at.renehollander.chat.model.Message;
import at.renehollander.chat.util.CallbackWithError;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.socket.client.IO;
import io.socket.client.Socket;

public class Application extends android.app.Application {

    private static final String API_URL = "http://10.0.105.191:8080";
    private static final String SOCKETIO_URL = "http://10.0.105.191:8081";

    private AsyncHttpClient client;
    private Socket socket;

    private ObservableArrayList<ChatRoom> chatrooms = new ObservableArrayList<>();
    private Map<String, ChatRoom> chatRoomMap = new HashMap<>();
    private String username;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new AsyncHttpClient();
    }

    public void connectToChat(String username) {
        // Connect to SocketIO server at the given URL
        this.username = username;
        Log.d("chat", "connecting");
        IO.Options opts = new IO.Options();
        try {
            this.socket = IO.socket(SOCKETIO_URL, opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // Add Listeners for the events
        socket.on(Socket.EVENT_CONNECT, this::onConnect);
        socket.on(Socket.EVENT_DISCONNECT, this::onDisconnect);
        socket.on("message", this::onMessage);
        socket.on("new_room", this::onNewRoom);
        socket.connect();
    }

    private void onNewRoom(Object[] objects) {
        // Trigger update of chatroom list
        Log.d("chat", String.valueOf(objects[0]));
        JSONObject obj = (JSONObject) objects[0];
        try {
            String roomName = obj.getString("room");
            if (!chatRoomMap.containsKey(roomName)) {
                ChatRoom room = new ChatRoom(roomName, new ObservableArrayList<>());
                chatRoomMap.put(room.getName(), room);
                chatrooms.add(room);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onMessage(Object[] objects) {
        // Trigger update on message list
        Log.d("chat", String.valueOf(objects[0]));
        JSONObject obj = (JSONObject) objects[0];
        try {
            Message msg = decode(username, obj);
            if (!chatRoomMap.containsKey(msg.getRoom())) {
                ChatRoom room = new ChatRoom(msg.getRoom(), new ObservableArrayList<>());
                chatRoomMap.put(room.getName(), room);
                chatrooms.add(room);
                room.getMessages().add(msg);
            } else {
                chatRoomMap.get(msg.getRoom()).getMessages().add(msg);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onDisconnect(Object[] objects) {
        Log.d("chat", "disconnected");
    }

    private void onConnect(Object[] objects) {
        // Retrieves the current chatroom list from the server
        Log.d("chat", "connected");
        this.socket.emit("get_rooms", null, args -> {
            JSONObject obj = (JSONObject) args[0];
            Log.d("chat", String.valueOf(obj));
            // TODO fix crash on reconnect
            for (ChatRoom room : chatrooms) {
                room.getMessages().clear();
            }
            chatrooms.clear();
            chatRoomMap.clear();
            for (String key : iterable(obj.keys())) {
                try {
                    ChatRoom room = decodeRoom(username, obj.getJSONObject(key));
                    chatRoomMap.put(room.getName(), room);
                    chatrooms.add(room);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void sendMessage(String room, String msg) {
        // send a message to the given chatroom
        try {
            JSONObject obj = new JSONObject();
            obj.put("date", new Date().getTime());
            obj.put("room", room);
            obj.put("user", username);
            obj.put("text", msg);
            if (this.socket != null) {
                this.socket.emit("message", obj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRoom(String room) {
        // create a chatroom with the given name
        try {
            JSONObject obj = new JSONObject();
            obj.put("room", room);
            if (this.socket != null) {
                this.socket.emit("new_room", obj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static ChatRoom decodeRoom(String thisUser, JSONObject object) throws JSONException {
        // Decode the JSON of a Room
        String name = object.getString("name");
        JSONArray messages = object.getJSONArray("messages");
        ObservableArrayList<Message> messageList = new ObservableArrayList<>();
        for (int i = 0; i < messages.length(); i++) {
            messageList.add(decode(thisUser, messages.getJSONObject(i)));
        }
        return new ChatRoom(name, messageList);
    }

    private static Message decode(String thisUser, JSONObject object) throws JSONException {
        // Decode a message object
        Date date = new Date(object.getLong("date"));
        String room = object.getString("room");
        String user = object.getString("user");
        String text = object.getString("text");
        Message.Flag flag = user.equals(thisUser) ? Message.Flag.SENT : Message.Flag.RECIEVED;
        return new Message(flag, room, user, date, text);
    }

    private static <T> Iterable<T> iterable(final Iterator<T> it) {
        return () -> it;
    }

    public ObservableArrayList<ChatRoom> getChatrooms() {
        return chatrooms;
    }

    public Map<String, ChatRoom> getChatRoomMap() {
        return chatRoomMap;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void register(String email, String username, String password, CallbackWithError<Throwable, Boolean> cb) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("username", username);
            body.put("password", password);
            client.post(this, API_URL + "/register", new StringEntity(body.toString()), ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 201) {
                        cb.call(null, true);
                    } else {
                        cb.call(new Exception("invalid status code for success: " + statusCode), null);
                    }
                }

                public void onFailure(int statusCode, Header[] headers, String string, Throwable throwable) {
                    cb.call(throwable, null);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    if (statusCode == 400) {
                        cb.call(null, false);
                    } else {
                        cb.call(throwable, null);
                    }
                }
            });
        } catch (Exception e) {
            cb.call(e, null);
        }

    }

    public void login(String email, String password, CallbackWithError<Throwable, JSONObject> cb) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            client.post(this, API_URL + "/login", new StringEntity(body.toString()), ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {
                        cb.call(null, response);
                    } else {
                        cb.call(new Exception("invalid status code for success: " + statusCode), null);
                    }
                }

                public void onFailure(int statusCode, Header[] headers, String string, Throwable throwable) {
                    cb.call(throwable, null);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    if (statusCode == 403) {
                        cb.call(null, null);
                    } else {
                        cb.call(throwable, null);
                    }
                }
            });
        } catch (Exception e) {
            cb.call(e, null);
        }
    }
}
