package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private transient QuestionService questionService;

    @PutMapping("/upvote")
    public String changeUpvote(@RequestBody Question question) {
        return questionService.updateScoreQuestion(question);
    }

    /**
     * Controller for getting all questions from the database.
     * @return list with all questions
     */
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    /**
     * Controller for saving a question in the database.
     * @param question the question to save
     * @return the question String
     */
    @PostMapping("/save-question")
    @ResponseBody
    public String addQuestion(@RequestBody Question question) {
        questionService.addQuestion(question);
        return question.getQuestion();
    }

    /**
     * Controller for deleting a question.
     * @param id the id of the question to delete
     * @return true if the question was deleted successfully, false otherwise
     */
    @DeleteMapping("/{id}")
    public boolean deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }

}
