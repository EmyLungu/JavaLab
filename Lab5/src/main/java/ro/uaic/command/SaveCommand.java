package ro.uaic.command;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;

/**
 * SaveCommand
 */
public class SaveCommand implements Command{
    Catalog catalog;
    String path;

    public SaveCommand(Catalog catalog, String path) {
        this.catalog = catalog;
        this.path = path;
    }

    @Override
    public void execute() throws CatalogException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(this.catalog);
            oos.close();
        } catch (Exception e) {
            throw new CatalogException("Could not save file to " + this.path + ": " + e.getMessage());
        }
    }
}
