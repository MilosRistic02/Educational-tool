package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;

@Entity
public class Moderator extends User{

    public Moderator() {}

    public Moderator(String username, String email, String password) {
        super(username, email, password, "moderator");
    }
}
