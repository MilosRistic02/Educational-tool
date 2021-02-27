package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;

@Entity
public class Lecturer extends User {
    public Lecturer() {
    }

    public Lecturer(String username, String email, String password) {
        super(username, email, password, "lecturer");
    }
}
