import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.PlayerMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

//TODO-2: complete these tests
public class PlayerMapperTest {

    MatchRepository matchRepository = mock(MatchRepository.class);
    PlayerRepository playerRepository = mock(PlayerRepository.class);
    PlayerMapper subject = new PlayerMapper(matchRepository, playerRepository);
    @Test
    void player_to_domain_ok() {

        Player expected = Player.builder()
                .id(4)
                .name("Rakoto")
                .isGuardian(false)
                .teamName("Barea")
                .build();

        Player actual = subject.toDomain(playerEntityRakoto(teamBarea()));
        assertEquals(expected, actual);
    }

    @Test
    void player_scorer_to_domain_ok() {

        PlayerScorer expected = PlayerScorer.builder()
                .player(Player.builder()
                        .id(4)
                        .name("Rakoto")
                        .isGuardian(false)
                        .teamName("Barea")
                        .build())
                .isOwnGoal(true)
                .minute(43)
                .build();

        PlayerScorer actual = subject.toDomain(scorerRakoto(playerEntityRakoto(teamBarea())));
        assertEquals(expected, actual);
    }

    @Test
    void player_scorer_to_entity_ok() {
        when(playerRepository.findById(4)).thenReturn(Optional.ofNullable(playerEntityRakoto(teamBarea())));
        when(matchRepository.findById(1)).thenReturn(Optional.ofNullable(matchEntityOne()));

        PlayerScoreEntity expected = PlayerScoreEntity.builder()
                .match(matchEntityOne())
                .player(playerEntityRakoto(teamBarea()))
                .minute(43)
                .ownGoal(true)
                .build();
        PlayerScoreEntity actual = subject.toEntity(1, playerScorerRakoto());
        assertEquals(expected, actual);
    }

    private static PlayerScorer playerScorerRakoto(){
        return PlayerScorer.builder()
                .player(Player.builder()
                        .id(4)
                        .name("Rakoto")
                        .isGuardian(false)
                        .teamName("Barea")
                        .build())
                .isOwnGoal(true)
                .minute(43)
                .build();
    }

    private static PlayerEntity playerEntityRakoto(TeamEntity teamEntityBarea) {
        return PlayerEntity.builder()
                .id(4)
                .name("Rakoto")
                .guardian(false)
                .team(teamEntityBarea)
                .build();
    }

    private static PlayerScoreEntity scorerRakoto(PlayerEntity playerEntityRakoto) {
        return PlayerScoreEntity.builder()
                .id(1)
                .player(playerEntityRakoto)
                .minute(43)
                .ownGoal(true)
                .build();
    }
    private static TeamEntity teamBarea() {
        return TeamEntity.builder()
                .id(1)
                .name("Barea")
                .build();
    }

    private static MatchEntity matchEntityOne(){
        return MatchEntity.builder()
                .teamA(teamBarea())
                .teamB(teamGhana())
                .scorers(List.of(scorerRakoto(playerEntityRakoto(teamBarea()))))
                .build();
    }

    private static TeamEntity teamGhana() {
        return TeamEntity.builder()
                .id(2)
                .name("Ghana")
                .build();
    }

    }
