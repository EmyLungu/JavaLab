package ro.uaic.command;

import ro.uaic.catalog.CatalogException;

/**
 * Command
 */
public interface Command {
    public void execute() throws CatalogException;
}
