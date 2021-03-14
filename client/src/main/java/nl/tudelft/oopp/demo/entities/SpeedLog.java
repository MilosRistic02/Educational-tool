package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

public class SpeedLog {

    private Long id;
    private Users users;
    private LectureRoom lectureRoom;
    private int speed;

    public SpeedLog() {
    }

    /**
     * Constructor for SpeedLog.
     * @param users         the user that sets the speed
     * @param lectureRoom   the lecture room where the speed is set
     * @param speed         the speed
     */
    public SpeedLog(Users users, LectureRoom lectureRoom, int speed) {
        this.id = -1L;
        this.users = users;
        this.lectureRoom = lectureRoom;
        this.speed = speed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public LectureRoom getLectureRoom() {
        return lectureRoom;
    }

    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int score) {
        this.speed = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpeedLog speedLog = (SpeedLog) o;
        return speed == speedLog.speed
                && Objects.equals(id, speedLog.id)
                && Objects.equals(users, speedLog.users)
                && Objects.equals(lectureRoom, speedLog.lectureRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, lectureRoom, speed);
    }
}
