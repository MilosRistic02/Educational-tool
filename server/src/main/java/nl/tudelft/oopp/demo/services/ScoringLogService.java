package nl.tudelft.oopp.demo.services;

import java.util.List;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.repositories.ScoringLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ScoringLogService {

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
        if (scoringLogRepository
                .existsByQuestionAndUsers(scoringLog.getQuestion(), scoringLog.getUsers())) {
            newScoringLog = scoringLogRepository
                    .getByQuestionAndUsers(scoringLog.getQuestion(), scoringLog.getUsers());
            // update score.
            newScoringLog.setScore(scoringLog.getScore());

        } else {
            newScoringLog = new ScoringLog(scoringLog.getQuestion(),
                    scoringLog.getUsers(),
                    scoringLog.getScore());
        }

        scoringLogRepository.save(newScoringLog);
        return "Success";
    }

    public List<ScoringLog> getVotes() {
        return scoringLogRepository.findAll();
    }
}
