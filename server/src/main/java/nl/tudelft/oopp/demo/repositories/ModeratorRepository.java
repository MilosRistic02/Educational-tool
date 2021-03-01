package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ModeratorRepository extends UsersRepository<Moderator> {

    @Query(value="SELECT * FROM Users u WHERE u.role='moderator' ",
            nativeQuery = true)
    List<Users> getAllModerators();

}
