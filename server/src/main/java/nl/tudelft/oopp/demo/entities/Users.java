package nl.tudelft.oopp.demo.entities;

import com.sun.istack.NotNull;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

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

    @OneToMany(mappedBy = "users")
    Set<ScoringLog> scoringLogs;

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

    /**
     * username getter.
     * @return username of the object
     */
    public String getUsername() {
        return username;
    }

    /**
     * username setter, sets the username of the users object that calls the method.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * email getter
     * @return email of the object
     */
    public String getEmail() {
        return email;
    }

    /**
     * email setter, sets the email of the users object that calls the method.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * password getter
     * @return password of the object
     */
    public String getPassword() {
        return password;
    }

    /**
     * password setter, sets the password of the users object that calls the method.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * role getter
     * @return role of the object
     */
    public String getRole() {
        return role;
    }

    /**
     * role setter, sets the role of the users object that calls the method.
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * equals method that checks whether two users are identical.
     * @param o any object that is used for comparison
     * @return true iff two objects are identical, false otherwise
     */
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

    /**
     * hashCode function for the username.
     * @return hashed username of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
