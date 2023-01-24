package app.foot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FootApiTests {

    @Test
    public void checkResult(){
        assertEquals(2, (1 + 1));
    }
}
