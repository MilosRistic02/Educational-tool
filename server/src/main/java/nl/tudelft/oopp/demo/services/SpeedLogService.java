package nl.tudelft.oopp.demo.services;

import java.util.List;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.repositories.SpeedLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpeedLogService {

    @Autowired
    private SpeedLogRepository speedLogRepository;

    /** method to save/update the speedlog.
     *
     * @param speedLog  the speedlog to save
     * @return          returns succes
     */
    public String saveSpeedLog(SpeedLog speedLog) {
        SpeedLog newSpeedLog = null;
        if (speedLogRepository
                .existsByUsersAndLectureRoom(speedLog.getUsers(), speedLog.getLectureRoom())) {
            newSpeedLog = speedLogRepository
                    .getByUsersAndLectureRoom(speedLog.getUsers(), speedLog.getLectureRoom());
            newSpeedLog.setSpeed(speedLog.getSpeed());
        } else {
            newSpeedLog = new SpeedLog(
                    speedLog.getUsers(),
                    speedLog.getLectureRoom(),
                    speedLog.getSpeed()
            );
        }
        speedLogRepository.save(newSpeedLog);
        return "succes";
    }

    public double getSpeedVotes(String lecturePin) {
        return speedLogRepository.getSpeedAverageByLecturePin(lecturePin);
    }
}
