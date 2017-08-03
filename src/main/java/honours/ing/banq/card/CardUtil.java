package honours.ing.banq.card;

import java.security.SecureRandom;

/**
 * @author Kevin Witlox
 */
public class CardUtil {

    private static final String SEED = "SecureRandomSeed";

    public static String generateCardNumber(CardRepository repository) {
        SecureRandom random = new SecureRandom(SEED.getBytes());

        int i = 0;
        while (i < 10000) {
            StringBuilder res = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                res.append(random.nextInt(10));
            }
            if (repository.findByCardNumber(res.toString()) == null) {
                return res.toString();
            }

            i++;
        }

        return null;
    }

}
