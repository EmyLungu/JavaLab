package ro.uaic.resource;

import java.awt.Desktop;
import java.io.File;

/**
 * FileResource
 */
public class FileResource extends Resource {
    public FileResource(String id, String title, String location, int year, String author) {
        super(id, title, location, year, author);
    }

    @Override
    public void view() throws ResourceException {
        Desktop desktop = Desktop.getDesktop();

        try {
            desktop.open(new File(this.location));
        } catch (Exception e) {
            throw new ResourceException("Failed to open File: " + this.location, e);
        }
    }
}
