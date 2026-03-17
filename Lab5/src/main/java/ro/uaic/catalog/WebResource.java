package ro.uaic.catalog;

import java.awt.Desktop;
import java.net.URI;

/**
 * WebResource
 */
public class WebResource extends Resource {
    public WebResource(String id, String title, String location, int year, String author) {
        super(id, title, location, year, author);
    }

    @Override
    public void view() throws ResourceException {
        Desktop desktop = Desktop.getDesktop();

        try {
            desktop.browse(new URI(this.location));
        } catch (Exception e) {
            throw new ResourceException("Failed to open URL: " + this.location, e);
        }
    }
}
