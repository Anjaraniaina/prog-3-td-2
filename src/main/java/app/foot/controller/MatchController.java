package app.foot.controller;

import app.foot.exception.BadRequestException;
import app.foot.model.Match;
import app.foot.model.PlayerScorer;
import app.foot.model.TeamMatch;
import app.foot.repository.MatchRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.service.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MatchController {
    private final MatchService service;
    private final MatchRepository matchRepository;

    @GetMapping("/matches")
    public List<Match> getMatches() {
        return service.getMatches();
    }

    @PostMapping("/matches/{matchId}/goals")
    public Match addGoals(@PathVariable Integer matchId, @RequestBody List<PlayerScorer> playerScorers) {
        return service.addGoals(matchId, playerScorers);
    }
}
