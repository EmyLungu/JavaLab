package ro.uaic.command;

import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;
import ro.uaic.resource.Resource;

/**
 * ViewCommand
 */
public class ViewCommand implements Command {
    Catalog catalog;
    String resourceID;

    public ViewCommand(Catalog catalog) {
        this.catalog = catalog;
        this.resourceID = "";
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    @Override
    public void execute() throws CatalogException {
        if (this.resourceID == null) {
            throw new CatalogException("Null resource");
        }

        try {
            Resource r = this.catalog.getResource(resourceID);
            r.view();
        } catch (Exception e) {
            throw new CatalogException(e.getMessage());
        }
    }
}
