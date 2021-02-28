package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;

@Entity
public class Moderator extends Users {

    public Moderator() {}

    public Moderator(String username, String email, String password) {
        super(username, email, password, "moderator");
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Moderator) {
            return super.equals(o);
        }
        return false;
    }
}
