import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.controller.rest.mapper.PlayerScorerRestMapper;
import app.foot.controller.validator.GoalValidator;
import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO-1: complete these tests
public class GoalValidatorTest {
    GoalValidator subject = new GoalValidator();
    PlayerRestMapper playerRestMapper = mock(PlayerRestMapper.class);
    PlayerScorerRestMapper playerScorerRestMapper = new PlayerScorerRestMapper(playerRestMapper);

    @Test
    void accept_ok() {
        when(playerRestMapper.toRest(any())).thenReturn(playerRestRakoto());
       assertDoesNotThrow(() -> subject.accept(playerScorerRestMapper.toRest(playerScorerRakoto())));
    }

    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {
        assertThrows(RuntimeException.class, () -> subject.accept(playerScorerRestRakoto()));
    }

    @Test
    void when_guardian_throws_exception() {
        assertThrows(RuntimeException.class, () -> subject.accept(
                app.foot.controller.rest.PlayerScorer.builder()
                        .player(app.foot.controller.rest.Player.builder()
                                .isGuardian(true)
                                .build())
                .build()));
    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {
        assertThrows(RuntimeException.class, () -> subject.accept(app.foot.controller.rest.PlayerScorer.builder()
                .player(app.foot.controller.rest.Player.builder()
                        .build())
                        .scoreTime(91)
                .build()));
    }

    @Test
    void when_score_time_less_than_0_throws_exception() {
        assertThrows(RuntimeException.class, () -> subject.accept(app.foot.controller.rest.PlayerScorer.builder()
                .player(playerRestRabe())
                        .scoreTime(-43)
                .build()));
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

    private static app.foot.controller.rest.Player playerRestRakoto(){
        return app.foot.controller.rest.Player.builder()
                .name("Rakoto")
                .id(4)
                .isGuardian(false)
                .build();
    }

    private static app.foot.controller.rest.PlayerScorer playerScorerRestRakoto(){
        return app.foot.controller.rest.PlayerScorer.builder()
                .scoreTime(null)
                .isOG(true)
                .player(playerRestRakoto())
                .build();
    }

    private static app.foot.controller.rest.Player playerRestRabe(){
        return app.foot.controller.rest.Player.builder()
                .name("Rabe")
                .id(1)
                .isGuardian(true)
                .build();
    }
}
