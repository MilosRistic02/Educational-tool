package nl.tudelft.oopp.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class LectureRoom {

    @NotNull
    private long lecturerID;
    private int courseId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String lecturePin;

    private Timestamp timestamp;

    public LectureRoom() {
    }

    /**
     * Constructs a new LectureRoom
     *
     * @param lecturerID - Identifier of the lecturer that created the LectureRoom
     * @param courseId - Identifier of the course
     */
    public LectureRoom(long lecturerID, int courseId) {
        this.lecturerID = lecturerID;
        this.courseId = courseId;
    }

    /**
     *
     * @return the identifier of the responsible lecturer
     */
    public long getLecturerID() {
        return lecturerID;
    }

    /**
     * Set the responsible lecturer for the room by changing the ID
     * @param lecturerID - The Identifier of the responsible lecturer
     */
    public void setLecturerID(long lecturerID) {
        this.lecturerID = lecturerID;
    }

    /**
     *
     * @return the identifier of the course
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     *
     * @param courseId - the identifier of the course
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     *
     * @return the uniquely generated pin of the LectureRoom
     */
    public String getLecturePin() {
        return lecturePin;
    }

    /**
     * Used whenever the pin needs to be reset for a LectureRoom
     *
     * @param lecturePin - the uniquely generated pin of the LectureRoom,
     * constructed of a timestamp, courseID and LecturerID
     */
    public void setLecturePin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    /**
     *
     * @return the timestamp that was generated at the creation of the LectureRoom
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureRoom that = (LectureRoom) o;
        return Objects.equals(lecturePin, that.lecturePin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturePin);
    }
}

