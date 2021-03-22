package nl.tudelft.oopp.demo.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectureRoomService {

    private LectureRoomRepository lectureRoomRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public LectureRoomService(LectureRoomRepository lectureRoomRepository,
                              QuestionRepository questionRepository) {
        this.lectureRoomRepository = lectureRoomRepository;
        this.questionRepository = questionRepository;
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
     * Writes all questions of a specific lectureRoom to the export file.
     * @param file - The file that is exported
     * @param lecturePin - The pin of the archived lectureRoom.
     * @return the file with all of the questions and corresponding answers.
     */
    public File exportRoom(File file, String lecturePin) {
        List<Question> questions = questionRepository.getAllByLecturePin(lecturePin);
        LectureRoom room = lectureRoomRepository.getByLecturePin(lecturePin);

        try {
            FileWriter fileWriter = new FileWriter(file);
            String output = "";

            if (questions.isEmpty()) {
                output = "This archive did not contain questions, therefore this file is empty.";
            } else {
                output += room.getLectureName()
                        + " ("
                        + room.getCreationDate().toString().substring(0, 10)
                        + ")\n\n";
                for (Question question : questions) {
                    output += "Q: " + question.getQuestion() + "\n";

                    String answer = (question.getAnswer() == null) ? "[Insert answer here]"
                                    : question.getAnswer();
                    output += "A: " + answer + "\n\n";
                }
            }

            fileWriter.write(output);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;
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

    /**
     * Getter for the LectureRoom class.
     * @param pin String of the pin that we want to identify the LectureRoom with
     * @return A LectureRoom that is associated with the pin
     */
    public LectureRoom getLectureRoom(String pin) {
        return lectureRoomRepository.getLectureRoomByLecturePin(pin);
    }

    /**
     * Method to change a lectureRoom in the database.
     * @param lectureRoom the lecture room to change to
     * @return whether the room is updated or didn't exist
     */
    public String putLectureRoom(LectureRoom lectureRoom) {
        if (!lectureRoomRepository.existsByLecturePin(
                lectureRoom.getLecturePin())) {
            return "Room does not yet exist";
        }
        LectureRoom prev = lectureRoomRepository.getByLecturePin(
                lectureRoom.getLecturePin());
        prev.setOpen(lectureRoom.isOpen());
        lectureRoomRepository.save(prev);
        return "Updated room";
    }

    /**
     * Method to get all lecturePins from closed rooms of a specific lecturer.
     * @return list of lecturePins
     */
    public List<LectureRoom> getClosedLecturePins() {
        return lectureRoomRepository.getAllByIsOpenIsFalse();
    }
}
