package nl.tudelft.oopp.demo.services;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.ScoringLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoringLogService {

    @Autowired
    private QuestionRepository questionRepository;

    private ScoringLogRepository scoringLogRepository;

    public ScoringLogService(ScoringLogRepository scoringLogRepository) {
        this.scoringLogRepository = scoringLogRepository;
    }

    /** method to store/update vote of user for a certain question.
     *
     * @param scoringLog    the scoringlog to save
     * @return              returns succes
     */
    public String vote(ScoringLog scoringLog) {
        ScoringLog newScoringLog = null;
        Question question = null;
        if (scoringLogRepository
                .existsByQuestionAndUsers(scoringLog.getQuestion(), scoringLog.getUsers())) {
            question = questionRepository.getByIdAndLecturePin(scoringLog.getQuestion().getId(),
                    scoringLog.getQuestion().getLecturePin());
            newScoringLog = scoringLogRepository
                    .getByQuestionAndUsers(scoringLog.getQuestion(), scoringLog.getUsers());
            // update score.
            question.setScore(question.getScore()
                    - newScoringLog.getScore()
                    + scoringLog.getScore());
            newScoringLog.setScore(scoringLog.getScore());

        } else {
            question = questionRepository.getByIdAndLecturePin(scoringLog.getQuestion().getId(),
                    scoringLog.getQuestion().getLecturePin());
            newScoringLog = new ScoringLog(scoringLog.getQuestion(),
                    scoringLog.getUsers(),
                    scoringLog.getScore());
            question.setScore(question.getScore() + scoringLog.getScore());
        }

        questionRepository.save(question);
        scoringLogRepository.save(newScoringLog);
        return "Success";
    }

    public List<ScoringLog> getVotes(Users users) {
        return scoringLogRepository.findAllByUsers(users);
    }
}
