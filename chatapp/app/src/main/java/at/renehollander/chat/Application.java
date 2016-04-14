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

    private static final String API_URL = "http://10.0.0.62:8080";
    private static final String SOCKETIO_URL = "http://10.0.0.62:8081";

    private AsyncHttpClient client;
    private Socket socket;

    private ObservableArrayList<ChatRoom> chatrooms = new ObservableArrayList<>();
    private Map<String, ChatRoom> chatRoomMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        client = new AsyncHttpClient();

        connectToChat("sdg");
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

    public void login(String email, String password, CallbackWithError<Throwable, String> cb) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            client.post(this, API_URL + "/login", new StringEntity(body.toString()), ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {
                        try {
                            cb.call(null, response.getString("token"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    public void connectToChat(String token) {
        Log.d("chat", "connecting");
        IO.Options opts = new IO.Options();
        opts.query = "token=" + token;
        try {
            this.socket = IO.socket(SOCKETIO_URL, opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, this::onConnect);
        socket.on(Socket.EVENT_DISCONNECT, this::onDisconnect);
        socket.on("chat", this::onChat);
    }

    private void onChat(Object[] objects) {
        Log.d("chat", String.valueOf(objects[0]));
        JSONObject obj = (JSONObject) objects[0];
        try {
            Message msg = decode("Rene8888", obj);
            if (!chatRoomMap.containsKey(msg.getRoom())) {
                ChatRoom room = new ChatRoom(msg.getRoom(), new ObservableArrayList<>());
                room.getMessages().add(msg);
                chatRoomMap.put(room.getName(), room);
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
        Log.d("chat", "connected");
        this.socket.emit("get_rooms", null, args -> {
            JSONObject obj = (JSONObject) args[0];
            Log.d("chat", String.valueOf(obj));
            // TODO fix crash on reconnect
            chatrooms.clear();
            chatRoomMap.clear();
            for (String key : iterable(obj.keys())) {
                try {
                    ChatRoom room = decodeRoom("Rene8888", obj.getJSONObject(key));
                    chatrooms.add(room);
                    chatRoomMap.put(room.getName(), room);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static ChatRoom decodeRoom(String thisUser, JSONObject object) throws JSONException {
        String name = object.getString("name");
        JSONArray messages = object.getJSONArray("messages");
        ObservableArrayList<Message> messageList = new ObservableArrayList<>();
        for (int i = 0; i < messages.length(); i++) {
            messageList.add(decode(thisUser, messages.getJSONObject(i)));
        }
        return new ChatRoom(name, messageList);
    }

    private static Message decode(String thisUser, JSONObject object) throws JSONException {
        Date date = new Date(object.getInt("date"));
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
}
