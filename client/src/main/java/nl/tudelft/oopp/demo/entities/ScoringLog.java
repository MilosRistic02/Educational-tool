package nl.tudelft.oopp.demo.entities;


public class ScoringLog {

    private Long id;
    private Question question;
    private Users users;
    private int score;

    public ScoringLog() {
    }

    /** Constructor for scoring log.
     * The id is set to -1 since the real id is set on the server.
     *
     * @param question  the question that is being upvoted
     * @param users     the user that upvotes
     * @param score     the score of the vote
     */
    public ScoringLog(Question question, Users users, int score) {
        this.id = -1L;
        this.question = question;
        this.users = users;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
