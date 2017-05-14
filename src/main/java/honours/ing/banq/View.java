package honours.ing.banq;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class View {

    // BankAccount
    public interface BankAccountView {}
    public interface OpenAccountView extends BankAccountView, CardView {}

    // Card
    public interface CardView {}

}
