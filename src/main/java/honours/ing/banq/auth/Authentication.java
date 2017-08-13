package honours.ing.banq.auth;

import honours.ing.banq.customer.Customer;
import honours.ing.banq.time.TimeServiceImpl;

import javax.persistence.*;
import java.util.Date;

/**
 * A model that represents an authentication token for a logged in user.
 * @author Jeffrey Bakker
 * @since 14-5-17
 */
@Entity
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Customer.class)
    private Customer customer;

    @Column(unique = true)
    private String token;

    private Date expiration;

    /**
     * @deprecated an empty constructor for the Hibernate ORM
     */
    @Deprecated
    public Authentication() { }

    /**
     * Creates a new {@link Authentication} with the given parameters.
     * @param customer   the customer for who this token is
     * @param token      the token
     * @param expiration the expiration date
     */
    public Authentication(Customer customer, String token, Date expiration) {
        this.customer = customer;
        this.token = token;
        this.expiration = expiration;
    }

    /**
     * Returns the ID of this object.
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the {@link Customer} for this authentication token.
     * @return the {@link Customer}
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the token.
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the expiration date for this authentication token.
     * @return the expiration date
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * Returns whether this token has expired or not.
     * @return {@code true} if it has expired
     */
    public boolean hasExpired() {
        return TimeServiceImpl.currentTimeMillis() >= expiration.getTime();
    }

}
