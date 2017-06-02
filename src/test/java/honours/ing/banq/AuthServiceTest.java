package honours.ing.banq;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

/**
 * Created by jeffrey on 31-5-17.
 */
public class AuthServiceTest {

    @Before
    public void setUp() {
        SpringApplication.run(Application.class);
    }

    @Test
    public void testToken() {

    }

    @Test
    public void testInvalidToken() {

    }

    @Test
    public void testExpiredToken() {

    }

    @Test
    public void testInvalidCredentials() {

    }

    @Test
    public void testDeletedCustomer() {

    }

}
