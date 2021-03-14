package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.repositories.SpeedLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpeedLogService {

    @Autowired
    private SpeedLogRepository speedLogRepository;
}
