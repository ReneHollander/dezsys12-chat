package at.renehollander.mobileapp.session;

import at.renehollander.mobileapp.entity.User;
import at.renehollander.mobileapp.repository.UserRepository;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
@Component
public class UserSessionStore {

    @Autowired
    private UserRepository userRepository;

    private Map<String, String> sessionStore;

    public UserSessionStore() {
        sessionStore = ExpiringMap.builder().expiration(1, TimeUnit.DAYS).expirationPolicy(ExpirationPolicy.CREATED).build();
    }

    public String createSession(User user) {
        String token = nextSessionId();
        sessionStore.put(token, user.getEmail());
        return token;
    }

    public User getFromSession(String token) {
        String email = sessionStore.get(token);
        if (email == null) return null;
        return userRepository.findOne(email);
    }

    private SecureRandom random = new SecureRandom();

    private String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

}
