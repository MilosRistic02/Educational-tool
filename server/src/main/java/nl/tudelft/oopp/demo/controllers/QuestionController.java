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



@Controller
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private transient QuestionService questionService;

    @PutMapping("/upvote")
    public String changeUpvote(@RequestBody Question question) {
        return questionService.updateScoreQuestion(question);
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @PostMapping
    public void addQuestion(@RequestBody Question question) {
        questionService.addQuestion(question);
    }

    @DeleteMapping("/{id}")
    public boolean deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }

}
