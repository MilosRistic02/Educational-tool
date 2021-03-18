package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import nl.tudelft.oopp.demo.converters.IntegerArrayConverter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Poll {

    @Id
    @GeneratedValue
    long id;
    String lecturePin;
    int size;
    boolean isOpen;
    char rightAnswer;
    String question;
    @Column
    @Convert(converter = IntegerArrayConverter.class)
    int[] votes;
    @CreationTimestamp
    private Date creationDate;


    public Poll() {
    }

    /**
     * Constrcutor for the Poll class.
     * @param lecturePin LecturePin this Poll is associated with
     * @param size Size of this Poll
     * @param rightAnswer The correct answer of this poll
     * @param question The question for this poll
     */
    public Poll(@JsonProperty("lecturePin") String lecturePin,
                @JsonProperty("size") int size,
                @JsonProperty("rightAnswer") char rightAnswer,
                @JsonProperty("question") String question) {
        this.lecturePin = lecturePin;
        this.size = size;
        this.rightAnswer = rightAnswer;
        this.question = question;
        this.votes = new int[10];
        this.isOpen = true;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLecturePin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setRightAnswer(char rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public String getLecturePin() {
        return lecturePin;
    }

    public int getSize() {
        return size;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public char getRightAnswer() {
        return rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public int[] getVotes() {
        return votes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Method used to add a vote to this poll.
     * @param vote Character representing the vote of this poll
     * @return A String informing whether the vote was successful or not
     */
    public String vote(Character vote) {
        if (vote - 65 < 0 || vote - 65 > size - 1) {
            return "Vote invalid";
        }
        if (!isOpen) {
            return "Poll closed";
        }
        votes[vote - 65]++;
        return "Success";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poll poll = (Poll) o;
        return id == poll.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}