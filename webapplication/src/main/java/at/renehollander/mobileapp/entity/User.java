package at.renehollander.mobileapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.MessageDigest;
import java.util.Arrays;

@Entity
public class User {

    @Id
    private String email;

    private byte[] passwordHash;

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this(email, hash(password));
    }

    public User(String email, byte[] passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", passwordHash=" + Arrays.toString(passwordHash) +
                '}';
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
        result = 31 * result + Arrays.hashCode(passwordHash);
        return result;
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
