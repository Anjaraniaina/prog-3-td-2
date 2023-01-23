package app.foot.repository.mapper;

import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.PlayerEntityRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerMapper {

    private final PlayerEntityRepository repository;

    public Player toDomain(PlayerEntity entity) {
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isGuardian(entity.isGuardian())
                .build();
    }

    public PlayerScorer toDomain(PlayerScoreEntity entity) {
        return PlayerScorer.builder()
                .player(toDomain(entity.getPlayer()))
                .minute(entity.getMinute())
                .isOwnGoal(entity.isOwnGoal())
                .build();
    }

    public PlayerScoreEntity toDomain(PlayerScorer playerScorer, MatchEntity matchEntity) {
        return PlayerScoreEntity.builder()
                .match(matchEntity)
                .ownGoal(playerScorer.getIsOwnGoal())
                .player(repository.getById(playerScorer.getPlayer().getId()))
                .minute(playerScorer.getMinute())
                .id(playerScorer.getPlayer().getId())
                .build();
    }
}
