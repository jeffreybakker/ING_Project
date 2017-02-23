package honours.ing.banq.model;

import honours.ing.banq.database.DatabaseQuery;
import honours.ing.banq.database.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * @author Jeffrey Bakker
 */
public class ModelQuery {

    public enum Constraints {
        IGNORED, NOTNULL, PRIMARYKEY, UNIQUE, DEFAULT
    }

    private static HashMap<Class<? extends Model>, String> insertQueries = new HashMap<>();
    private static HashMap<Class<? extends Model>, String> updateQueries = new HashMap<>();

    private static Class<? extends Model>[] modelClasses = new Class[]{
            User.class,
            CheckingsAccount.class,
            SavingsAccount.class
    };

    public static Map.Entry<String, String[]> getInsertQuery(Model model) {
        List<String> result = new ArrayList<>();

        Map<Field, Constraints[]> fields = parseFields(model.getClass());
        for (Field field : fields.keySet()) {
            if (field.getName().equals("id")) { // TODO: Watch out wen changing field name
                continue;
            }

            try {
                Object object = field.get(model);
                if (object instanceof Model) {
                    result.add(String.valueOf(((Model) object).id));
                } else {
                    result.add(String.valueOf(object));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String[] resValue = new String[result.size()];
        resValue[0] = model.getClass().getName();
        System.arraycopy(result.toArray(new String[] {}), 0, resValue, 1, result.size());
        return new Entry<>(insertQueries.get(model), resValue);
    }

    public static String getUpdateQuery(Model model) {
        return updateQueries.get(model);
    }

    public static void init() {
        for (Class<? extends Model> modelClass : modelClasses) {
            genInsertQuery(modelClass);
            genUpdateQuery(modelClass);
        }
    }

    public static void genInsertQuery(Class<? extends Model> modelClass) {
        Map<Field, Constraints[]> fieldConstraints = parseFields(modelClass);
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ? VALUES (?");
        for (int i = 1; i < fieldConstraints.size(); i++) {
            builder.append(", ?");
        }
        builder.append(")");
        insertQueries.put(modelClass, builder.toString());
    }

    public static void genUpdateQuery(Class<? extends Model> modelClass) {
        Map<Field, Constraints[]> fieldConstraints = parseFields(modelClass);
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ? SET ");

        boolean first = false;
        for (Field field : fieldConstraints.keySet()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(field.getName()).append("='?'");
            first = true;
        }
        builder.append(" WHERE id=?");
        updateQueries.put(modelClass, builder.toString());
    }

    public static Map<Field, Constraints[]> parseFields(Class<? extends Model> modelClass) {
        Map<Field, Constraints[]> fieldConstraints = new HashMap<>();
        Field[] subfields = modelClass.getDeclaredFields();
        Field[] superfields = modelClass.getSuperclass().getDeclaredFields();
        Field[] fields = new Field[subfields.length + superfields.length];
        System.arraycopy(subfields, 0, fields, 0, subfields.length);
        System.arraycopy(superfields, 0, fields, subfields.length, superfields.length);

        fields:
        for (Field field : fields) {
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

            fieldConstraints.put(field, constraints.toArray(new Constraints[]{}));
        }

        return fieldConstraints;
    }

}
