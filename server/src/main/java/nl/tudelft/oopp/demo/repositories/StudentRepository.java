package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentRepository extends UsersRepository<Student> {

    @Query(value="SELECT * FROM Users u WHERE u.role='student' ",
            nativeQuery = true)
    List<Users> getAllStudents();

}
