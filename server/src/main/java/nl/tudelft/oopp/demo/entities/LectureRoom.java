package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class LectureRoom {

    @Id
    private String lecturePin;

    @NotNull
    private String lecturerID;
    private int courseId;

    @CreationTimestamp
    private Date creationDate;
    private Date startingTime;

    private boolean isOpen;

    private int frequency;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureRoom")
    Set<SpeedLog> speedLogs;

    public LectureRoom() {
    }

    public LectureRoom(@JsonProperty("lecturerID")@NotNull String lecturerID) {
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
        this.courseId = courseId;
        this.isOpen = true;
    }

    /**
     * Constructs a new LectureRoom.
     *
     * @param lecturerID - Identifier of the lecturer that created the LectureRoom
     * @param courseId - Identifier of the course
     * @param startingTime - The date and time when the lecture will open
     */
    public LectureRoom(String lecturerID, int courseId, Date startingTime) {
        this.lecturerID = lecturerID;
        this.courseId = courseId;
        this.isOpen = true;
        this.startingTime = startingTime;
        this.frequency = 0;
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


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

