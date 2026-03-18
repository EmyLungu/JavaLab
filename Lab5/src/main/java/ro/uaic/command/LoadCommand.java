package ro.uaic.command;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;

/**
 * LoadCommand
 */
public class LoadCommand implements Command {
    Catalog catalog;
    String path;

    public LoadCommand(Catalog catalog, String path) {
        this.catalog = catalog;
        this.path = path;
    }

    @Override
    public void execute() throws CatalogException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

            Catalog loadedCatalog = (Catalog) ois.readObject();
            this.catalog.setResources(loadedCatalog.getResources());

            ois.close();
        } catch (Exception e) {
            throw new CatalogException("Could not load file from " + this.path + ": " + e.getMessage());
        }
    }
}
