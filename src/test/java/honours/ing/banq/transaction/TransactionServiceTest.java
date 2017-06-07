package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import honours.ing.banq.Application;
import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.config.JsonRpcConfig;
import honours.ing.banq.config.TestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Kevin Witlox
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@Transactional
@ActiveProfiles("test")
public class TransactionServiceTest {

    // Services
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Before
    public void setUp() throws Exception {
        bankAccountService.openAccount("Jan", "Jansen", "J.", new Date(1996, 1, 1), "1234567890",
                "Klaverstraat 1", "0612345678", "janjansen@gmail.com", "jantje96", "1234");
    }

    @Test
    public void depositIntoAccount() throws Exception {

    }

    @Test
    public void payFromAccount() throws Exception {
    }

    @Test
    public void transferMoney() throws Exception {
    }

}