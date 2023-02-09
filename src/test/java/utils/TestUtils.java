package utils;

import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.TeamMatch;
import app.foot.model.Team;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import org.junit.jupiter.api.function.Executable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtils {
    public static Player playerRest1() {
        return Player.builder()
                .teamName("E1")
                .id(1)
                .name("J1")
                .isGuardian(false)
                .build();
    }

    public static Player playerRest2() {
        return Player.builder()
                .id(2)
                .teamName("E1")
                .name("J2")
                .isGuardian(false)
                .build();
    }

    public static Player playerRest3() {
        return Player.builder()
                .id(3)
                .teamName("E2")
                .name("J3")
                .isGuardian(false)
                .build();
    }

    public static Player playerRest4() {
        return Player.builder()
                .isGuardian(false)
                .id(4)
                .name("J4")
                .teamName("E2")
                .build();
    }



    public static PlayerScorer playerScorerAt30(){
        return PlayerScorer.builder()
                .player(playerRest1())
                .scoreTime(30)
                .isOG(false)
                .build();
    }

    public static PlayerScorer playerScorerAt20(){
        return PlayerScorer.builder()
                .player(playerRest1())
                .scoreTime(20)
                .isOG(false)
                .build();
    }


    public static PlayerScorer playerScorerAt10(){
        return
                PlayerScorer.builder()
                        .player(playerRest1())
                        .scoreTime(10)
                        .isOG(false)
                        .build();
    }

    public static PlayerScorer playerScorerAt40(){
        return PlayerScorer.builder()
                .player(playerRest2())
                .scoreTime(40)
                .isOG(true)
                .build();
    }


    public static PlayerScorer playerScorerAt60(){
        return PlayerScorer.builder()
                .player(playerRest4())
                .scoreTime(60)
                .isOG(true)
                .build();
    }


    public static PlayerScorer playerScorerAt50(){
        return
                PlayerScorer.builder()
                        .player(playerRest3())
                        .scoreTime(50)
                        .isOG(false)
                        .build();
    }

    public static app.foot.controller.rest.Team teamRest2(){
        return app.foot.controller.rest.Team.builder()
                .id(2)
                .name("E2")
                .build();
    }


    public static app.foot.controller.rest.Team teamRest1(){
        return app.foot.controller.rest.Team.builder()
                .id(1)
                .name("E1")
                .build();
    }

    // E1 (4) VS E2 (2)
    public static Match expectedMatch1() {
        return Match.builder()
                .id(1)
                .teamA(TeamMatch.builder()
                        .score(4)
                        .scorers(List.of(
                                playerScorerAt30(),
                                playerScorerAt20(),
                                playerScorerAt10(),
                                playerScorerAt60()
                                ))
                        .team(teamRest1())
                        .build())
                .teamB(TeamMatch.builder()
                        .score(2)
                        .scorers(List.of(
                                playerScorerAt40(),
                                playerScorerAt50()))
                        .team(teamRest2())
                        .build())
                .stadium("S1")
                .datetime(Instant.parse("2023-01-01T10:00:00Z"))
                .build();
    }

    public static PlayerScorer scorer1() {
        return PlayerScorer.builder()
                .player(player1())
                .isOG(false)
                .scoreTime(10)
                .build();
    }

    public static PlayerScorer nullScoreTimeScorer() {
        return scorer1().toBuilder()
                .scoreTime(null)
                .build();
    }

    public static Player player1() {
        return Player.builder()
                .id(1)
                .name("Rakoto")
                .isGuardian(false)
                .build();
    }

    public static app.foot.model.PlayerScorer rakotoModelScorer(app.foot.model.Player playerModelRakoto, PlayerScoreEntity scorerRakoto) {
        return app.foot.model.PlayerScorer.builder()
                .player(playerModelRakoto)
                .isOwnGoal(false)
                .minute(scorerRakoto.getMinute())
                .build();
    }

    public static Team teamModelGhana(TeamEntity teamEntityGhana) {
        return Team.builder()
                .id(teamEntityGhana.getId())
                .name(teamEntityGhana.getName())
                .build();
    }

    public static Team teamModelBarea(TeamEntity teamEntityBarea) {
        return Team.builder()
                .id(teamEntityBarea.getId())
                .name(teamEntityBarea.getName())
                .build();
    }

    public static PlayerScoreEntity scorerRakoto(PlayerEntity playerEntityRakoto) {
        return PlayerScoreEntity.builder()
                .id(1)
                .player(playerEntityRakoto)
                .minute(10)
                .build();
    }

    public static app.foot.model.Player playerModelRakoto(PlayerEntity playerEntityRakoto) {
        return app.foot.model.Player.builder()
                .id(playerEntityRakoto.getId())
                .name(playerEntityRakoto.getName())
                .isGuardian(false)
                .teamName(teamBarea().getName())
                .build();
    }

    public static PlayerEntity playerEntityRakoto(TeamEntity teamEntityBarea) {
        return PlayerEntity.builder()
                .id(1)
                .name("Rakoto")
                .guardian(false)
                .team(teamEntityBarea)
                .build();
    }

    public static TeamEntity teamGhana() {
        return TeamEntity.builder()
                .id(2)
                .name("Ghana")
                .build();
    }

    public static TeamEntity teamBarea() {
        return TeamEntity.builder()
                .id(1)
                .name("Barea")
                .build();
    }

    public static void assertThrowsExceptionMessage(String message, Class exceptionClass, Executable executable) {
        Throwable exception = assertThrows(exceptionClass, executable);
        assertEquals(message, exception.getMessage());
    }
}
