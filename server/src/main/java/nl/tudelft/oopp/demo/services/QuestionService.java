package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (!questionRepository.existsByQuestionAndAuthorAndLecturePin(
                question.getQuestion(), question.getAuthor(), question.getLecturePin())) {
            return "Question does not yet exists";
        }
        Question prev = questionRepository.getByQuestionAndAuthorAndLecturePin(
                question.getQuestion(), question.getAuthor(), question.getLecturePin());
        prev.setScore(question.getScore());
        questionRepository.save(prev);
        return "Updated score of Question";
    }


    /**
     * Gets all questions.
     *
     * @return all questions found in the database
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Adds a question to the database
     *
     * @param question question to add to the database
     */
    public void addQuestion(Question question) {
        questionRepository.save(question);
    }

    /**
     * Remove a question from the database iff the question exists
     *
     * @param id the id that uniquely identifies each question
     * @return boolean that is true iff a question was deleted
     */
    public boolean deleteQuestion(long id) {
        if(!questionRepository.existsById(id))
            return false;

        questionRepository.deleteById(id);
        return true;
    }
}
