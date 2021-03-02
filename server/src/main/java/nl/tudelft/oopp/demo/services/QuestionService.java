package nl.tudelft.oopp.demo.services;

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
}
