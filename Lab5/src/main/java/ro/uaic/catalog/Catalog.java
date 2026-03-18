package ro.uaic.catalog;

import ro.uaic.resource.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Catalog
 */
public class Catalog implements Serializable {
    String name;
    List<Resource> resources;

    public Catalog(String name) {
        this.name = name;
        this.resources = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public Resource getResource(String id) throws CatalogException {
        for (Resource r: this.getResources()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }

        throw new CatalogException("Resource not found");
    }
}
