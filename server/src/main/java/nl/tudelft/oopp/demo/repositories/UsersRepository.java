package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UsersRepository extends JpaRepository<Users, Long> {


    boolean existsByUsername(String username);

    boolean existsByUsernameAndPassword(String username, String password);

    Users getByUsernameAndPassword(String username, String password);

    boolean existsByEmail(String email);

    Users getByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE role = 'student' AND username ILIKE %?1% "
            + "AND is_banned = ?2 ", nativeQuery = true)
    List<Users> findStudents(String search, boolean view);

    @Query(value = "SELECT * FROM users WHERE role = 'student' "
            + "AND is_banned = false", nativeQuery = true)
    List<Users> getNotBannedStudents();

    @Query(value = "SELECT * FROM users WHERE role = 'student' "
           + "AND is_banned = true",  nativeQuery = true)
    List<Users> getBannedStudents();

    @Query(value = "SELECT is_banned FROM users WHERE username = ?1 ", nativeQuery = true)
    boolean isUserBanned(String username);


}
