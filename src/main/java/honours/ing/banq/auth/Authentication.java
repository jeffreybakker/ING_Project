package honours.ing.banq.auth;

import honours.ing.banq.customer.Customer;
import honours.ing.banq.time.TimeUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jeffrey
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

    public Authentication() { }

    public Authentication(Customer customer, String token, Date expiration) {
        this.customer = customer;
        this.token = token;
        this.expiration = expiration;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public boolean hasExpired() {
        return TimeUtil.currentTimeMillis() >= expiration.getTime();
    }

}
