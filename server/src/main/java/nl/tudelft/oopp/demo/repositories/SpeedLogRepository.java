package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.SpeedLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeedLogRepository  extends JpaRepository<SpeedLog, Long> {
}
