package at.renehollander.mobileapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.util.Arrays;

@Entity
public class User {

    @Id
    private String email;

    @Column(unique = true)
    private String username;

    @NotNull
    private byte[] passwordHash;

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("username") String username, @JsonProperty("password") String password) {
        this(email, username, hash(password));
    }

    public User(String email, String username, byte[] passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.username = username;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return Arrays.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(passwordHash);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash=" + Arrays.toString(passwordHash) +
                '}';
    }

    private static byte[] hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
