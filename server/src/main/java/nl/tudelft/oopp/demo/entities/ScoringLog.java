package nl.tudelft.oopp.demo.entities;

import javax.persistence.*;

@Entity
public class ScoringLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Users users;

    private int score;

    public ScoringLog() {
    }

    public ScoringLog(Question question, Users users, int score) {
        this.question = question;
        this.users = users;
        this.score = score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
