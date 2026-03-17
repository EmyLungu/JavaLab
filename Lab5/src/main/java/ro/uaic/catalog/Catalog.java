package ro.uaic.catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Catalog
 */
public class Catalog {
    String name;
    List<Resource> resources;

    public Catalog(String name) {
        this.name = name;
        this.resources = new ArrayList<>();
    }

    public void addResource(Resource newResource) {
        this.resources.add(newResource);
    }

    public void print() {
        for (Resource r : this.resources) {
            System.out.println(r);
        }
    }
}
