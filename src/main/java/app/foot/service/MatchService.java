package app.foot.service;

import app.foot.exception.BadRequestException;
import app.foot.model.Match;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.mapper.MatchMapper;
import app.foot.repository.mapper.PlayerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchService {
    private final MatchRepository repository;
    private final MatchMapper mapper;
    private final PlayerMapper playerMapper;

    public List<Match> getMatches() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public MatchEntity findMatchById(Integer id) {
        return repository.getById(id);
    }

    public Match addGoals(Integer matchId, List<PlayerScorer> playerScorers){
        for (PlayerScorer playerscorer : playerScorers) {
            if(playerscorer.getPlayer().getIsGuardian()){
                throw new BadRequestException("Le joueur est un gardien, but non accordée");
            }
            if(playerscorer.getMinute() < 0 || playerscorer.getMinute() > 90){
                throw new BadRequestException("Temps non accepté");
            }
        }
        MatchEntity matchEntity = repository.getById(matchId);
        matchEntity.setScorers(playerScorers.stream().map(
                (PlayerScorer playerScorer) ->
                playerMapper.toDomain(playerScorer, matchEntity)).toList());
        repository.save(matchEntity);

        return mapper.toDomain(matchEntity);

    }
}
