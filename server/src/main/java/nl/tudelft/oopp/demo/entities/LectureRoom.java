package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class LectureRoom {

    @Id
    private String lecturePin;

    @NotNull
    private String lecturerID;
    private int courseId;

    @CreationTimestamp
    private Timestamp timestamp;

    public LectureRoom() {
    }

    public LectureRoom(@JsonProperty("lecturer")@NotNull String lecturerID) {
        this.lecturerID = lecturerID;
    }

    /**
     * Constructs a new LectureRoom.
     *
     * @param lecturerID - Identifier of the lecturer that created the LectureRoom
     * @param courseId - Identifier of the course
     */
    public LectureRoom(String lecturerID, int courseId) {
        this.lecturerID = lecturerID;
    }

    /** Getter for the lecturerID.
     *
     * @return the identifier of the responsible lecturer
     */
    public String getLecturerID() {
        return lecturerID;
    }

    /**
     * Set the responsible lecturer for the room by changing the ID.
     * @param lecturerID - The Identifier of the responsible lecturer
     */
    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    /**Getter for the courseID.
     *
     * @return the identifier of the course
     */
    public int getCourseId() {
        return courseId;
    }

    /**Setter for the courseID.
     *
     * @param courseId - the identifier of the course
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**Getter for the lecture pin.
     *
     * @return the uniquely generated pin of the LectureRoom
     */
    public String getLecturePin() {
        return lecturePin;
    }

    /**
     * Used whenever the pin needs to be reset for a LectureRoom.
     *
     * @param lecturePin - the uniquely generated pin of the LectureRoom,
     *      constructed of a timestamp, courseID and LecturerID
     */
    public void setLecturePin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LectureRoom that = (LectureRoom) o;
        return Objects.equals(lecturePin, that.lecturePin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturePin);
    }
}

