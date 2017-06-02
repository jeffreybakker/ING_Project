package honours.ing.banq;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffrey on 31-5-17.
 */
public class BankAccountServiceTest {

    private List<BankAccount> accounts;
    private List<Card> cards;
    private List<Customer> customers;

    @Before
    public void setUp() {
        SpringApplication.run(Application.class);

        accounts = new ArrayList<>();
        cards = new ArrayList<>();
        customers = new ArrayList<>();
    }

    @After
    public void cleanUp() {
        // TODO: Remove all created accounts, cards and customers
    }

    @Test
    public void testOpenAccount() {

    }

    @Test
    public void testOpenAccountEmptyParameters() {

    }

    @Test
    public void testOpenAccountInvalidParameters() {

    }

    @Test
    public void testOpenAccountExistingCustomer() {

    }

    @Test
    public void testOpenAdditionalAccount() {

    }

    @Test
    public void testOpenAdditionalAccountInvalidToken() {

    }

    @Test
    public void testCloseAccount() {

    }

    @Test
    public void testCloseAccountInvalidToken() {

    }

    @Test
    public void testCloseAccountInvalidIBAN() {

    }

    @Test
    public void testCloseAccountNotAuthorized() {

    }

}
