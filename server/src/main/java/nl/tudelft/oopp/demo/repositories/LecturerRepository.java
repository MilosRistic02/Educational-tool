package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Lecturer;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LecturerRepository extends UsersRepository<Lecturer>{
}
