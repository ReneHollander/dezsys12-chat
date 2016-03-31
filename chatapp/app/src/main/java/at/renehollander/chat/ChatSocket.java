package at.renehollander.chat;

import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import at.renehollander.chat.util.Callback;

public class ChatSocket {

    private String url;
    private String session;
    private WebSocket socket;

    private Callback<ChatMessage> onMessage;

    public void setOnMessage(Callback<ChatMessage> onMessage) {
        this.onMessage = onMessage;
    }

    public void sendMessage(String msg) {
        check();
        JSONObject obj = new JSONObject();
        try {
            obj.put("message", msg);
            this.socket.send(obj.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onString(String str) {
        Log.d("chat", "Recieved: " + str);
        socket.send("hello world!");
        try {
            JSONObject obj = new JSONObject(str);
            ChatMessage message = new ChatMessage(new Date(obj.getString("date")), obj.getString("username"), obj.getString("message"));
            onMessage.call(message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(String url, String session, Callback<Exception> cb) {
        if (isConnected()) throw new IllegalStateException("already connected");

        this.url = url;
        this.session = session;

        AsyncHttpClient.getDefaultInstance().websocket(this.url, null, (ex, socket) -> {
            if (ex != null) {
                cb.call(ex);
                return;
            }
            if (socket == null) {
                throw new RuntimeException("unknown error");
            }
            this.socket = socket;
            socket.setClosedCallback((ex1) -> {
                Log.d("chat", "Closed Connection");
            });
            socket.setStringCallback(this::onString);
            cb.call(null);
        });
    }

    public void close() {
        check();
        socket.close();
        socket = null;
    }

    private void check() {
        if (!isConnected()) throw new IllegalStateException("not yet connected");
    }

    public boolean isConnected() {
        return socket != null && socket.isOpen();
    }

}
