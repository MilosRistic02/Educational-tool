package nl.tudelft.oopp.demo.entities;

import com.sun.istack.NotNull;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Entity
public class Users {

    @Id
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;

    public Users(){
    }

    /** Constructor to make instance of Users.
     *
     * @param username  String containing the userId
     * @param email     String containing the users email
     * @param password  String with the password
     * @param role      String with the role
     */
    public Users(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        Users users = (Users) o;
        return getUsername().equals(users.getUsername())
                && getRole().equals(users.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
