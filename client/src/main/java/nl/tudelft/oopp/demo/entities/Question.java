package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import java.util.Date;

public class Question {

    private long id;
    private String question;
    private String answer;
    private int score;
    private Date creationDate;
    private int isAnswered;
    private String lecturePin;
    private String author;

    public Question() {
    }

    /**
     * Constructor to make instance of Question.
     *
     * @param question   The string containing the question
     * @param lecturePin The pin for the lecture
     * @param author     The string containing the author
     */
    public Question(String question, String lecturePin, String author) {
        this.question = question;
        this.lecturePin = lecturePin;
        this.author = author;
        this.isAnswered = 0;
    }

    /**
     * Gets id.
     *
     * @return the autogenerated id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets question.
     *
     * @return the question asked by a student
     *
     */

    public String getQuestion() {
        return question;
    }

    /**
     * Gets answer.
     *
     * @return the answer that is given to the question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets score.
     *
     * @return the score of upvotes substracted by amount of downvotes
     */
    public int getScore() {
        return score;
    }

    /**
     * Get answered status.
     * @return: an integer that represents if
     * the question has been answered or not.
     */
    public int isAnswered() {
        return isAnswered;
    }

    /**
     * Gets lecture pin.
     *
     * @return the pin of the lecture
     */
    public String getLecturePin() {
        return lecturePin;
    }

    /**
     * Gets author.
     *
     * @return the author of the question
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the questionId.
     *
     * @param id the id of the question
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets question.
     *
     * @param question the question String
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets score.
     *
     * @param score the score of upvotes substracted by downvotes
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the boolean whether the question is answered.
     *
     * @param answered boolean to indicate if a question is answered
     */
    public void setAnswered(int answered) {
        isAnswered = answered;
    }

    /**
     * Sets answer.
     *
     * @param answer the answer String
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Sets lecture pin.
     *
     * @param lecturePin the lecture pin
     */
    public void setLecturePin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Equals method for Question.
     *
     * @param o the object to compare to
     * @return true if the question is also of class Question and has the same Id, false otherwise
     */
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
