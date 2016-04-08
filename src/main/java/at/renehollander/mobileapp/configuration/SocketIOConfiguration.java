package at.renehollander.mobileapp.configuration;

import at.renehollander.mobileapp.annotation.Event;
import at.renehollander.mobileapp.annotation.SocketIO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class SocketIOConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SocketIOConfiguration.class);

    @Autowired
    private ApplicationContext context;

    @Bean(name = "socketIOServers")
    public List<SocketIOServer> socketIOServers() {
        Map<String, Object> beans = context.getBeansWithAnnotation(SocketIO.class);
        List<SocketIOServer> servers = new ArrayList<>();
        beans.forEach((name, o) -> {
            SocketIO annotationConfig = o.getClass().getAnnotation(SocketIO.class);
            LOG.info("Starting SocketIO server for bean " + name + " on port " + annotationConfig.port());
            servers.add(createFrom(annotationConfig, o));
        });
        return servers;
    }

    public SocketIOServer createFrom(SocketIO annotationConfig, Object object) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(8081);
        config.getSocketConfig().setReuseAddress(true);

        SocketIOServer server = new SocketIOServer(config);
        if (object instanceof ConnectListener)
            server.addConnectListener((ConnectListener) object);
        if (object instanceof DisconnectListener)
            server.addDisconnectListener((DisconnectListener) object);

        for (Method method : object.getClass().getMethods()) {
            Event event = method.getAnnotation(Event.class);
            if (event != null) {
                if (event.value().isEmpty()) {
                    throw new IllegalArgumentException("Invalid event name for method " + stringifyMethod(method));
                }
                if (method.getParameterCount() == 2) {
                    checkParameterType(method, 0, SocketIOClient.class);
                    LOG.info("Mapped event \"" + event.value() + "\" onto " + stringifyMethod(method));
                    server.addEventListener(event.value(), method.getParameterTypes()[1], (DataListener) (client, data, ackSender) -> {
                        if (ackSender.isAckRequested())
                            throw new IllegalStateException("Event " + event.value() + " wants an acknowledgement, but it's not implemented in method " + stringifyMethod(method));
                        method.invoke(object, client, data);
                    });
                } else if (method.getParameterCount() == 3) {
                    checkParameterType(method, 0, SocketIOClient.class);
                    checkParameterType(method, 2, AckRequest.class);
                    LOG.info("Mapped event \"" + event.value() + "\" onto " + stringifyMethod(method));
                    server.addEventListener(event.value(), method.getParameterTypes()[1], (DataListener) (client, data, ackSender) -> {
                        method.invoke(object, client, data, ackSender);
                    });
                } else {
                    throw new IllegalArgumentException("Invalid signature for method " + stringifyMethod(method));
                }
            }
        }
        server.start();
        return server;
    }

    private static String stringifyMethod(Method method) {
        return method.getDeclaringClass() + "." + method.getName() + "()";
    }

    private static void checkParameterType(Method method, int idx, Class<?> expected) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (idx > paramTypes.length)
            throw new IllegalArgumentException("Parameter " + idx + " of method " + stringifyMethod(method) + " is not " + expected);
        if (paramTypes[idx] != expected)
            throw new IllegalArgumentException("Parameter " + idx + " of method " + stringifyMethod(method) + " is not " + expected);
    }

}
