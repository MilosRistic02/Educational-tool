package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LecturerRepository extends UsersRepository<Lecturer> {

    @Query(value="SELECT * FROM Users u WHERE u.role='lecturer' ",
            nativeQuery = true)
    List<Users> getAllLecturers();

}
