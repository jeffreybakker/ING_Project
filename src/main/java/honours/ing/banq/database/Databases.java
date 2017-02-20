package honours.ing.banq.database;

import java.io.File;

/**
 * @author Jeffrey Bakker
 */
public enum Databases {

    ING                 ("ing.sqlite");

    private Database db;

    private String fileName;

    Databases(String fileName) {
        this.fileName = fileName;
    }

    public Database getDatabase() {
        return db;
    }

    public String getFileName() {
        return "jdbc:sqlite:" + new File("./" + fileName).getAbsolutePath();
    }

}
