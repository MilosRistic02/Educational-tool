package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Student;
import org.apache.catalina.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentRepository extends UsersRepository<Student> {
}
