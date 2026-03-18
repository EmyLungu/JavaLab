package ro.uaic.command;

import ro.uaic.resource.Resource;
import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;

/**
 * AddCommand
 */
public class AddCommand implements Command {
    Catalog catalog;
    Resource resource;

    public AddCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void execute() throws CatalogException {
        if (this.resource == null) {
            throw new CatalogException("Null resource");
        }
        this.catalog.getResources().add(resource);
    }
}
