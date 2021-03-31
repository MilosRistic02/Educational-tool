package nl.tudelft.oopp.demo.services;

import java.util.List;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.logger.FileLogger;
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
    public String saveSpeedLog(SpeedLog speedLog, String username) {
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
        FileLogger.addMessage(username + " set the pace of lecture room "
                + speedLog.getLectureRoom().getLectureName() + " to "
                + speedLog.getSpeed());
        speedLogRepository.save(newSpeedLog);
        return "succes";
    }

    /**
     * Returns the average speed of the lecture.
     * @param lecturePin    The pin to calculate the average of
     * @return              Returns the average speed
     */
    public double getSpeedVotes(String lecturePin) {
        if (speedLogRepository.existsRoom(lecturePin) > 0) {
            return speedLogRepository.getSpeedAverageByLecturePin(lecturePin);
        }
        return 50.0;
    }
}
