package honours.ing.banq.model;

import honours.ing.banq.database.DatabaseQuery;
import honours.ing.banq.database.annotation.DBIgnored;
import honours.ing.banq.database.annotation.DBNotNull;
import honours.ing.banq.database.annotation.DBPrimaryKey;
import honours.ing.banq.database.annotation.DBUnique;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public class ModelQuery {

    private enum Constraints {
        IGNORED, NOTNULL, PRIMARYKEY, UNIQUE, DEFAULT
    }

    private static HashMap<Class<? extends Model>, String> insertQueries = new HashMap<>();
    private static HashMap<Class<? extends Model>, String> updateQueries = new HashMap<>();

    private static Class<? extends Model>[] modelClasses = new Class[]{
            Person.class
    };

    public static DatabaseQuery getInsertQuery(Model model) {
        String baseQuery = insertQueries.get(model);
        throw new UnsupportedOperationException("This method has not been implemented yet");
    }

    public static DatabaseQuery getUpdateQuery(Model model) {
        String baseQuery = updateQueries.get(model);
        throw new UnsupportedOperationException("This method has not been implemented yet");
    }

    public static void init() {
        for (Class<? extends Model> modelClass : modelClasses) {
            genInsertQuery(modelClass);
            genUpdateQuery(modelClass);
        }
    }

    private static void genInsertQuery(Class<? extends Model> modelClass) {
        throw new UnsupportedOperationException("This method has not been implemented yet");
    }

    private static void genUpdateQuery(Class<? extends Model> modelClass) {
        throw new UnsupportedOperationException("This method has not been implemented yet");

    }

    private HashMap<Object, Constraints[]> parseFields(Class<? extends Model> modelClass) {
        HashMap<Object, Constraints[]> fieldConstraints = new HashMap<>();
        Field[] fields = modelClass.getDeclaredFields();

        fields: for (Field field : fields) {
            List<Constraints> constraints = new ArrayList<>();
            Annotation[] annotations = field.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(DBPrimaryKey.class)) {
                    constraints.add(Constraints.PRIMARYKEY);
                } else if (annotation.annotationType().equals(DBNotNull.class)) {
                    constraints.add(Constraints.NOTNULL);
                } else if (annotation.annotationType().equals(DBUnique.class)) {
                    constraints.add(Constraints.UNIQUE);
                } else if (annotation.annotationType().equals(DBIgnored.class)) {
                    continue fields;
                }
            }

            if (constraints.size() == 0) {
                constraints.add(Constraints.DEFAULT);
            }

            fieldConstraints.put(field, constraints.toArray(new Constraints[] {}));
        }

        return fieldConstraints;
    }

}
