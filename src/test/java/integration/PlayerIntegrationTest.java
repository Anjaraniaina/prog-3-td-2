package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static utils.TestUtils.playerRest1;
import static utils.TestUtils.playerRest2;
import static utils.TestUtils.playerRest3;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
@Transactional
class PlayerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void read_players_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/players"))
                .andReturn()
                .getResponse();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(9, actual.size());
        assertTrue(actual.containsAll(List.of(
                playerRest1(),
                playerRest2(),
                playerRest3())));
    }

    @Test
    void create_players_ok() throws Exception {
        Player toCreate = Player.builder()
                .name("Joe Doe")
                .isGuardian(false)
                .teamName("E1")
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(post("/players")
                        .content(objectMapper.writeValueAsString(List.of(toCreate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(1, actual.size());
        assertEquals(toCreate, actual.get(0).toBuilder().id(null).build());
    }

    @Test
    void update_players_ok() throws Exception {
        Player toUpdate = Player.builder()
                .id(1)
                .name("J1")
                .isGuardian(true)
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(put("/players")
                        .content(objectMapper.writeValueAsString(List.of(toUpdate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();

        Player expected = Player.builder()
                .id(1)
                .name("J1")
                .teamName("E1")
                .isGuardian(true)
                .build();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0).toBuilder().teamName("E1").build());
    }

    @Test
    void update_players_ko() throws Exception {
        Player toUpdate = Player.builder()
                .id(1)
                .isGuardian(true)
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(put("/players")
                        .content(objectMapper.writeValueAsString(List.of(toUpdate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();

        List<Player> actual = convertFromHttpResponse(response);

        assertNull(actual.get(0).getName());
    }

    private List<Player> convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType playerListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Player.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                playerListType);
    }
}
