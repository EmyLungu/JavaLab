package ro.uaic.command;

import ro.uaic.catalog.Catalog;
import ro.uaic.resource.Resource;

/**
 * ListCommand
 */
public class ListCommand implements Command {
    Catalog catalog;

    public ListCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        for (Resource r : this.catalog.getResources()) {
            System.out.println(r);
        }
    }
}
