package honours.ing.banq.model;

import honours.ing.banq.log.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kevin Witlox on 23-2-2017.
 */
public class ModelQueryTest {

    private static final String TAG = "ModelQueryTest";

    @Before
    public void setUp() throws Exception {
        Log.start();
    }

    @Test
    public void parseFields() throws Exception {
        Map<Field, ModelQuery.Constraints[]> map = ModelQuery.parseFields(Account.class);
        for (Map.Entry<Field, ModelQuery.Constraints[]> entry : map.entrySet()) {
            Log.d(TAG, entry.getKey().getName() + String.format(" (%s)", entry.getKey().getType()
                    .toString()));
            ModelQuery.Constraints[] constraintsArray = entry.getValue();
            for (int i = 0; i < constraintsArray.length; i++) {
                Log.d(TAG, "\t\tAnnotation: " + constraintsArray[i].toString());
            }
        }
    }

    @Test
    public void genInsertQuery() throws Exception {
        ModelQuery.genInsertQuery(SavingsAccount.class);
        Map.Entry<String, String[]> map = ModelQuery.getInsertQuery(new SavingsAccount("KHD Witlox", new User("Kevin", "KHD", "Witlox"), new Money(Currency.getInstance(Locale.GERMAN))));
        Log.d(TAG, map.getKey());
        Log.d(TAG, Arrays.toString(map.getValue()));
    }

    @Test
    public void genUpdateQuery() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Log.stop();
    }
}