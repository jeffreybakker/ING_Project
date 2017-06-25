package honours.ing.banq.util;

import java.util.Random;

/**
 * @author jeffrey
 * @since 9-6-17
 */
public class StringUtil {

    public static String generate(int length) {
        Random random = new Random();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            res.append((char) ((int) 'a') - 1 + random.nextInt(26));
        }

        return res.toString();
    }

}
