package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;

@Entity
public class Student extends User {

    public Student() {
    }

    public Student(String username, String email, String password) {
        super(username, email, password, "student");
    }
}
