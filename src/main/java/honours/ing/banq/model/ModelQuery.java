package honours.ing.banq.model;

import java.util.HashMap;

/**
 * @author Jeffrey Bakker
 */
public class ModelQuery {

    private static HashMap<Class<? extends Model>, String> insertQueries = new HashMap<>();
    private static HashMap<Class<? extends Model>, String> updateQueries = new HashMap<>();

    public static void init() {

    }

}
