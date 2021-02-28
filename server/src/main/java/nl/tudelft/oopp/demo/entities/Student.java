package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;

@Entity
public class Student extends Users {

    public Student() {
    }

    public Student(String username, String email, String password) {
        super(username, email, password, "student");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student) {
            return super.equals(o);
        }
        return false;
    }
}
