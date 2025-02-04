package integration;

import app.foot.FootApi;
import app.foot.controller.rest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.expectedMatch1;
import static utils.TestUtils.playerRest1;
import static utils.TestUtils.teamRest1;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
@Slf4j
class MatchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMatch2(), actual);
    }

    @Test
    void read_matches_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<Match> actual = convertFromHttpResponseToList(response);

        assertEquals(3, actual.size());
        assertTrue(actual.contains(expectedMatch2()));
        assertTrue(actual.contains(expectedMatch1()));
        assertTrue(actual.contains(expectedMatch3()));
    }

    @Transactional
    @Test
    void add_goals_into_match_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(post("/matches/{matchId}/goals", 3)
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .scoreTime(40)
                                .isOG(false)
                                .player(playerRest1())
                                .build())))
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andReturn()
                .getResponse();
        Match actual = convertFromHttpResponse(response);

        // "E1" a marqué un but avec "J1"
        assertEquals(1, actual.getTeamA().getScore());
        assertEquals(playerRest1(), actual.getTeamA().getScorers().get(0).getPlayer());
    }

    @Transactional
    @Test
    void add_goals_into_match_ko() throws Exception {
        assertThrows(JsonProcessingException.class, () -> mockMvc.perform(post("/matches/{matchId}/goals", 3)
                .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                        .scoreTime(-10)
                        .isOG(false)
                        .player(playerRest1().toBuilder()
                                .isGuardian(true).build())
                        .build())))
                .contentType("application/json")
                .accept("application/json")
        ));
    }

    private static Match expectedMatch2() {
        return Match.builder()
                .id(2)
                .teamA(teamMatchA())
                .teamB(teamMatchB())
                .stadium("S2")
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .build();
    }

    private static Match expectedMatch3() {
        return Match.builder()
                .id(3)
                .teamA(TeamMatch.builder()
                        .team(teamRest1())
                        .score(0)
                        .scorers(List.of())
                        .build())
                .teamB(TeamMatch.builder()
                        .team(team3())
                        .score(0)
                        .scorers(List.of())
                        .build())
                .stadium("S3")
                .datetime(Instant.parse("2023-01-01T18:00:00Z"))
                .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(70)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(80)
                                .isOG(true)
                                .build()))
                .build();
    }

    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .teamName("E3")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .id(2)
                .name("E2")
                .build();
    }

    private Match convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(
                response.getContentAsString(),
                Match.class);
    }

    private List<Match> convertFromHttpResponseToList(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType matchListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Match.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                matchListType);
    }
}
