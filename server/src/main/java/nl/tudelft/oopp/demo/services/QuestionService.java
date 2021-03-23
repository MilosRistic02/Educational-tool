package nl.tudelft.oopp.demo.services;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
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

    /** Update score of a question iff it already exists.
     *
     * @param question  question with the updated score
     * @return          Result of the operation
     */
    public String updateScoreQuestion(Question question) {
        if (!questionRepository.existsByLecturePin(
                question.getLecturePin())) {
            return "Question does not yet exist";
        }
        Question prev = questionRepository.getByLecturePin(
                question.getLecturePin());
        prev.setScore(question.getScore());
        questionRepository.save(prev);
        return "Updated score of Question";
    }

    /**
     * Update the answer and answered status of a question, iff
     * it already exists in the database.
     * @param question - question with the updated answer and answered status.
     * @return - String informing about the result of the operation.
     */
    public String updateAnswerQuestion(Question question) {
        if (!questionRepository.existsByIdAndAndLecturePin(
                question.getId(), question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question old = questionRepository.getByIdAndLecturePin(
                question.getId(), question.getLecturePin());
        old.setAnswered(question.getAnswered());
        old.setAnswer(question.getAnswer());
        questionRepository.save(old);
        return "Updated score of Question";
    }

    /**
     * Update the content of a question, iff the question
     * exists in the database.
     * @param question - question with the updated content.
     * @return String informing about the result of the operation.
     */
    public String updateContentQuestion(Question question) {
        if (!questionRepository.existsByIdAndAndLecturePin(
                question.getId(), question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question old = questionRepository.getByIdAndLecturePin(
                question.getId(), question.getLecturePin());

        old.setQuestion(question.getQuestion());
        questionRepository.save(old);
        return "Updated score of Question";
    }


    /**
     * Update the answered status of a question, given that the question exists in
     * the database.
     * @param question with the updated answered status.
     * @return a String informing if the update was successful or not.
     */
    public String updateQuestionAnsweredStatus(Question question) {
        if (!questionRepository.existsByLecturePin(question.getLecturePin())) {
            return "The question is not in the database.";
        }
        Question q = questionRepository.getByLecturePin(question.getLecturePin());
        q.setAnswered(question.getAnswered());
        questionRepository.save(q);
        return "The answered status of the question has been updated.";

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
        return questionRepository.getAllByLecturePin(id);
    }

    /**
     * Get all answered (either written or verbally) questions.
     * @param lecturePin of the desired room.
     * @return a list of all the answered questions.
     */
    public List<Question> getAllAnsweredQuestions(String lecturePin) {
        List<Question> answered = new ArrayList<>();
        answered.addAll(questionRepository.getAllByAnsweredAndLecturePin(1, lecturePin));
        answered.addAll(questionRepository.getAllByAnsweredAndLecturePin(2, lecturePin));

        return answered;
    }

    public List<Question> getAllNonAnsweredQuestions(String lecturePin) {
        return questionRepository.getAllByAnsweredAndLecturePin(0, lecturePin);
    }

    /**
     * Adds a question to the database.
     *
     * @param question question to add to the database.
     */
    public String addQuestion(Question question) {
        Question lastQuestion = questionRepository
                .findTopByLecturePinAndAuthorOrderByCreationDateDesc(
                        question.getLecturePin(), question.getAuthor());

        if (lastQuestion == null) {
            questionRepository.save(question);
            return "Success";
        }


        Long difference = (System.currentTimeMillis()
                - lastQuestion.getCreationDate().getTime()) / 1000;
        LectureRoom room = lectureRoomRepository
                .getLectureRoomByLecturePin(question.getLecturePin());

        if (room.getFrequency() <= difference) {
            questionRepository.save(question);
            return "Success";
        } else {
            return "Need to wait " + room.getFrequency() + " seconds per question";
        }

    }

    /**
     * Remove a question from the database iff the question exists.
     *
     * @param id the id that uniquely identifies each question.
     * @return boolean that is true iff a question was deleted.
     */
    public boolean deleteQuestion(long id) {
        if (!questionRepository.existsById(id)) {
            return false;
        }

        questionRepository.deleteById(id);
        return true;
    }
}
