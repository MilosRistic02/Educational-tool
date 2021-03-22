package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users, Long> {


    boolean existsByUsername(String username);

    boolean existsByUsernameAndPassword(String username, String password);

    Users getByUsernameAndPassword(String username, String password);

    boolean existsByEmail(String email);

    Users getByUsername(String username);


}
