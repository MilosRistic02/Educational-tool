package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UsersRepository<T extends Users> extends JpaRepository<T, Long> {
}
