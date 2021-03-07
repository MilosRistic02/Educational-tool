package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

public class Users {


    private String username;
    private String email;
    private String password;
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

    /**
     * username getter.
     * @return username of the object
     */
    public String getUsername() {
        return username;
    }

    /**
     * username setter, sets the username of the users object that calls the method.
     * @param username String containing the new username of this User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the Email.
     * @return email of the object
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the Email, sets the email of the users object that calls the method.
     * @param email String containing the new Email of this User
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the password.
     * @return password of the object
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password, sets the password of the users object that calls the method.
     * @param password String containing the new Password of this User
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the role.
     * @return role of the object
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter for the role, sets the role of the users object that calls the method.
     * @param role String containing the new Role of this User
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

