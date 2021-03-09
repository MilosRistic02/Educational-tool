package nl.tudelft.oopp.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LectureRoomService {

    private LectureRoomRepository lectureRoomRepository;

    @Autowired
    public LectureRoomService(LectureRoomRepository lectureRoomRepository) {
        this.lectureRoomRepository = lectureRoomRepository;
    }

    /**
     * Method for generating a lecturePin that is used to join the lecture.
     * @param lectureHost String containing the host of this LectureRoom.
     * @return String containing a lecturePin.
     */
    public static String createPin(String lectureHost) {
        return (int) Math.floor(10000 + Math.random() * 90000)
                + String.valueOf(LocalDateTime.now().getYear()).substring(2, 4)
                + lectureHost;
    }

    /**
     * Method for adding a new LectureRoom to the database.
     * @param lectureRoom LectureRoom we want to add.
     * @return String containing the pin that can be used to join this LectureRoom.
     */
    public String addLectureRoom(LectureRoom lectureRoom) {
        if (lectureRoomRepository.getAllByLecturerID(lectureRoom.getLecturerID()).size() > 50000) {
            return "Too many rooms created under this host";
        }

        String pin = createPin(lectureRoom.getLecturerID());
        if (lectureRoomRepository.existsByLecturePin(pin)) {
            addLectureRoom(lectureRoom);
        }

        lectureRoom.setLecturePin(pin);
        lectureRoomRepository.save(lectureRoom);
        return pin;
    }

    /**
     * Method for removing a LectureRoom from the database iff the LectureRoom exists.
     * @param lectureRoomPin Pin that uniquely identifies each LectureRoom.
     * @return Boolean that is true iff a LectureRoom was deleted.
     */
    public boolean deleteLectureRoom(String lectureRoomPin) {
        if (!lectureRoomRepository.existsByLecturePin(lectureRoomPin)) {
            return false;
        }

        LectureRoom room = lectureRoomRepository.getLectureRoomByLecturePin(lectureRoomPin);
        lectureRoomRepository.delete(room);
        return true;
    }

    /**
     * Method for deleting all the lecture rooms.
     * @return Boolean that is true iff all rooms were deleted
     */
    public boolean deleteAllLectureRooms() {
        List<LectureRoom> lectureRooms = lectureRoomRepository.getAll();
        for (LectureRoom lectureRoom : lectureRooms) {
            lectureRoomRepository.delete(lectureRoom);
        }
        return true;
    }

    /**
     * Method for getting all the lecture rooms.
     * @return List containing all the lecture rooms.
     */
    public List<LectureRoom> getAllLectureRooms() {
        return lectureRoomRepository.getAll();
    }

    /**
     * Method for checking if a room exists.
     * @param pin the pin to check for.
     * @return true if the room exists, false otherwise.
     */
    public boolean existsByPin(String pin) {
        return lectureRoomRepository.existsByLecturePin(pin);
    }
}
