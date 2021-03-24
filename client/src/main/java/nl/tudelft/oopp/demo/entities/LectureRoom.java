package nl.tudelft.oopp.demo.entities;

import java.util.Date;
import java.util.Objects;


public class LectureRoom {
    private String lecturePin;
    private String lectureName;
    private String lecturerID;
    private String courseId;
    private Date creationDate;
    private Date startingTime;
    private boolean isOpen;

    public LectureRoom() {
    }

    /**
     * Constructs a new LectureRoom.
     *
     * @param lecturerID - Identifier of the lecturer that created the LectureRoom
     * @param lectureName - Name of the lectureRoom
     * @param courseId - Identifier of the course
     */
    public LectureRoom(String lecturerID, String lectureName, String courseId) {
        this.lecturerID = lecturerID;
        this.lectureName = lectureName;
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
    public LectureRoom(String lecturerID, String lectureName, String courseId, Date startingTime) {
        this.lecturerID = lecturerID;
        this.lectureName = lectureName;
        this.courseId = courseId;
        this.isOpen = true;
        this.startingTime = startingTime;
    }

    /**
     * Getter for the lecturerID.
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

    /**
     * Getter for the lectureName.
     * @return the name of the lectureRoom
     */
    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    /**
     * Getter for the courseID.
     * @return the identifier of the course
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Setter for the courseID.
     * @param courseId - the identifier of the course
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * Getter for the lecture pin.
     * @return the uniquely generated pin of the LectureRoom
     */
    public String getLecturePin() {
        return lecturePin;
    }

    /**
     * Used whenever the pin needs to be reset for a LectureRoom.
     * @param lecturePin - the uniquely generated pin of the LectureRoom,
     *      constructed of a timestamp, courseID and LecturerID
     */
    public void setLecturePin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    /**
     * Getter for isOpen.
     * @return if the lectureRoom is opened.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Setter for isOpen.
     * @param isOpen - boolean that shows if a lectureRoom is open or not.
     */
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Getter for the startingTime.
     * @return the starting date and time of the lectureRoom.
     */
    public Date getStartingTime() {
        return startingTime;
    }

    /**
     * Setter for the startingTime.
     * @param startingTime - the date and time at which a lectureRoom can be entered.
     */
    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
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