import com.mimteam.mimclient.client.HTTPClient;
import com.mimteam.mimclient.client.UserInfo;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpPostUnitTest {

    @Test
    public void httpClient_correctGetPost() {
        UserInfo userInfo = new UserInfo(0);
        String url = "http://maxim12321-test.herokuapp.com";
        HTTPClient httpClient = new HTTPClient(userInfo, url);
        HashMap<String, String> loginData = new HashMap<String, String>() {{
            put("username", "Kuzia");
            put("password", "kuzia");
        }};

        String token = httpClient.post("/login", new HashMap<>(), loginData, false);
        assertNotNull(token);
        System.out.println(token);
        userInfo.setToken(token);

        String username = httpClient.get("/username");
        assertNotNull(username);
        assertEquals(username, "Kuzia");
    }
}
