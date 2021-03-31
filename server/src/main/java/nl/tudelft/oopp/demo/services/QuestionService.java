package nl.tudelft.oopp.demo.services;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.logger.FileLogger;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LectureRoomRepository lectureRoomRepository;

    /**
     * Update the answer and answered status of a question, iff
     * it already exists in the database.
     * @param question - question with the updated answer and answered status.
     * @return - String informing about the result of the operation.
     */
    public String updateAnswerQuestion(Question question, String username) {
        if (!questionRepository.existsByIdAndAndLecturePin(
                question.getId(), question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question old = questionRepository.getByIdAndLecturePin(
                question.getId(), question.getLecturePin());
        String from = old.getAnswered() > 0 ? (old.getAnswered() > 1
                ?  "answered verbally": "answered") : "unanswered";
        String to = question.getAnswered() > 0 ? (question.getAnswered() > 1
                ?  "answered verbally": "answered") : "unanswered";
        FileLogger.addMessage(username + " updated answer of question "
                + question.getId() + " from " + from
                + " to " + to);
        old.setAnswered(question.getAnswered());
        old.setAnswer(question.getAnswer());
        questionRepository.save(old);
        return "Updated answer of Question";
    }

    /**
     * Update the content of a question, iff the question
     * exists in the database.
     * @param question - question with the updated content.
     * @return String informing about the result of the operation.
     */
    public String updateContentQuestion(Question question, String username) {
        if (!questionRepository.existsByIdAndAndLecturePin(
                question.getId(), question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question old = questionRepository.getByIdAndLecturePin(
                question.getId(), question.getLecturePin());
        FileLogger.addMessage(username + " updated content of question "
                + question.getId() + " from " + old.getQuestion()
                + " to " + question.getQuestion());
        old.setQuestion(question.getQuestion());
        questionRepository.save(old);
        return "Updated content of Question";
    }

    /**
     * Check the answered status of a given question in the database. Assumes that
     * the given question is in the database.
     * @param question for which to check the answered status.
     * @return true if the question has been answered, false otherwise.
     */
    public int isQuestionAnswered(Question question) {
        Question q = questionRepository.getByLecturePin(question.getLecturePin());
        return q.getAnswered();
    }


    /**
     * Gets all questions.
     *
     * @return all questions found in the database.
     */
    public List<Question> getAllQuestions(String id) {
        return questionRepository.getAllByLecturePinOrderByScoreDescCreationDateDesc(id);
    }

    /**
     * Get all answered (either written or verbally) questions.
     * @param lecturePin of the desired room.
     * @return a list of all the answered questions.
     */
    public List<Question> getAllAnsweredQuestions(String lecturePin) {
        List<Question> answered = new ArrayList<>();
        answered.addAll(questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(1, lecturePin));
        answered.addAll(questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(2, lecturePin));

        return answered;
    }

    public List<Question> getAllNonAnsweredQuestions(String lecturePin) {
        return questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(0, lecturePin);
    }

    /**
     * Adds a question to the database.
     *
     * @param question question to add to the database.
     */
    public String addQuestion(Question question, String username) {
        Question lastQuestion = questionRepository
                .findTopByLecturePinAndAuthorOrderByCreationDateDesc(
                        question.getLecturePin(), question.getAuthor());

        if (lastQuestion == null) {
            questionRepository.save(question);
            FileLogger.addMessage(username + " asked the question with id " + question.getId());
            return "Success";
        }


        Long difference = (System.currentTimeMillis()
                - lastQuestion.getCreationDate().getTime()) / 1000;
        LectureRoom room = lectureRoomRepository
                .getByLecturePin(question.getLecturePin());

        if (room.getQuestionFrequency() <= difference) {
            questionRepository.save(question);
            FileLogger.addMessage(username + " asked the question with id " + question.getId());
            return "Success";
        } else {
            FileLogger.addMessage(username + " tried to save a question but needs to wait "
                    + difference + " seconds longer");
            return "Need to wait " + difference + " more seconds to ask a new question.";
        }

    }

    /**
     * Remove a question from the database iff the question exists.
     *
     * @param id the id that uniquely identifies each question.
     * @return boolean that is true iff a question was deleted.
     */
    public boolean deleteQuestion(long id, String username) {
        if (!questionRepository.existsById(id)) {
            return false;
        }

        questionRepository.deleteById(id);
        FileLogger.addMessage(username + " deleted question with id " + id);
        return true;
    }
}
