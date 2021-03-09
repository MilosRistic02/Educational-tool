package nl.tudelft.oopp.demo.services;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    /** Update score of a question iff it already exists.
     *
     * @param question  question with the updated score
     * @return          Result of the operation
     */
    public String updateScoreQuestion(Question question) {
        if (!questionRepository.existsByLecturePin(
                question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question prev = questionRepository.getByLecturePin(
                question.getLecturePin());
        prev.setScore(question.getScore());
        questionRepository.save(prev);
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
        q.setAnswered(question.isAnswered());
        questionRepository.save(q);
        return "The answered status of the question has been updated.";

    }

    /**
     * Check the answered status of a given question in the database. Assumes that
     * the given question is in the database.
     * @param question for which to check the answered status.
     * @return true if the question has been answered, false otherwise.
     */
    public Boolean isQuestionAnswered(Question question) {
        Question q = questionRepository.getByLecturePin(question.getLecturePin());
        return q.isAnswered();
    }


    /**
     * Gets all questions.
     *
     * @return all questions found in the database.
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Adds a question to the database.
     *
     * @param question question to add to the database.
     */
    public String addQuestion(Question question) {
        questionRepository.save(question);
        return question.getQuestion();
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
