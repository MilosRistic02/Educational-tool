package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

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

    /** Constructor to make instance of Question.
     *
     * @param question      The string containing the question
     * @param lecturePin    The pin for the lecture
     * @param author        The string containing the author
     */
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

    public void setAnswer(String answer) {
        this.answer = answer;
    }

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

        Question question = (Question) o;

        return id == question.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
