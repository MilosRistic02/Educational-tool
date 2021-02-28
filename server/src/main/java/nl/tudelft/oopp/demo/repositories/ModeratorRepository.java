package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ModeratorRepository extends UsersRepository<Moderator> {
}
