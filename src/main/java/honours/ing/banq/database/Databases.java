package honours.ing.banq.database;

/**
 * @author Jeffrey Bakker
 */
public enum Databases {

    ING                 ("ing.sqlite");

    private Database db;

    private String fileName;

    Databases(String fileName) {
        this.fileName = fileName;

        db = Database.getDatabase(fileName);
    }

    public Database getDatabase() {
        return db;
    }

    public String getFileName() {
        return fileName;
    }

}
