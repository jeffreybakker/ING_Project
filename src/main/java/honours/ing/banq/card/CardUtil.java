package honours.ing.banq.card;

import java.security.SecureRandom;

/**
 * @author Kevin Witlox
 */
public class CardUtil {

    private static final String SEED = "SecureRandomSeed";

    public static Integer generateCardNumber(CardRepository repository) {
        SecureRandom random = new SecureRandom(SEED.getBytes());

        int i = 0;
        while (i < Integer.MAX_VALUE / 2) {
            Integer cardNumber = random.nextInt();
            if (repository.findByCardNumber(cardNumber) == null) {
                return cardNumber;
            }

            i++;
        }

        return null;
    }

}
