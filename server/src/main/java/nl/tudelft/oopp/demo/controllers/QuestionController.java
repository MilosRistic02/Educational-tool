package nl.tudelft.oopp.demo.controllers;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @ResponseBody
    public String changeUpvote(@RequestBody Question question) {
        return questionService.updateScoreQuestion(question);
    }

    @PutMapping("/update-answer")
    @ResponseBody
    public String updateAnswerQuestion(@RequestBody Question question) {
        return questionService.updateAnswerQuestion(question);
    }

    @PutMapping("update-content")
    @ResponseBody
    public String updateContentQuestion(@RequestBody Question question) {
        return questionService.updateContentQuestion(question);
    }


    @GetMapping("/get-all/{id}")
    @ResponseBody
    public List<Question> getAllQuestions(@PathVariable String id) {
        return questionService.getAllQuestions(id);
    }

    @GetMapping("/get-all/answered/{lecturePin}")
    @ResponseBody
    public List<Question> getAllAnsweredQuestions(@PathVariable String lecturePin) {
        return questionService.getAllAnsweredQuestions(lecturePin);
    }

    @GetMapping("/get-all/non-answered/{lecturePin}")
    @ResponseBody
    public List<Question> getAllNonAnsweredQuestions(@PathVariable String lecturePin) {
        return questionService.getAllNonAnsweredQuestions(lecturePin);
    }

    /**
     * Controller for saving a question in the database.
     * @param question the question to save
     * @return the question String
     */
    @PostMapping("/save-question")
    @ResponseBody
    public String addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    /**
     * Controller for deleting a question.
     * @param id the id of the question to delete
     * @return true if the question was deleted successfully, false otherwise
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public boolean deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }

}
