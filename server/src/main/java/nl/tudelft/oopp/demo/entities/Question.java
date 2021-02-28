package nl.tudelft.oopp.demo.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @NotBlank
    private String question;

    private String answer;

    private int score;

    private Timestamp timestamp;

    private boolean isAnswered;

    private String lecturePin;

    private String author;

    public Question() {
    }

    public Question(@NonNull @NotBlank String question, String lecturePin, String author) {
        this.question = question;
        this.lecturePin = lecturePin;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public String getLecturePin() {
        return lecturePin;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id == question.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
