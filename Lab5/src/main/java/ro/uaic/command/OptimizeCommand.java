package ro.uaic.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;
import ro.uaic.resource.Resource;

/**
 * OptimizeCommand
 */
public class OptimizeCommand implements Command {
    Catalog catalog;
    String[] allKeywords;

    public OptimizeCommand(Catalog catalog, String[] allKeywords) {
        this.catalog = catalog;
        this.allKeywords = allKeywords;
    }

    /*
     * Greedy Aproximate for Set Cover Problem
     * https://www.geeksforgeeks.org/dsa/greedy-approximate-algorithm-for-set-cover-problem/
     */
    @Override
    public void execute() throws CatalogException {
        long startTime = System.currentTimeMillis();

        Set<String> uncovered = new HashSet<>(Arrays.asList(allKeywords));
        List<Resource> selected = new ArrayList<>();

        while (!uncovered.isEmpty()) {
            int maxKeyCount = -1;
            Resource maxR = null;

            for (Resource r : this.catalog.getResources()) {
                int keyCount = 0;
                for (String keyword : r.getKeywords()) {
                    if (uncovered.contains(keyword)) {
                        keyCount++;
                    }
                }

                if (keyCount > maxKeyCount) {
                    maxKeyCount = keyCount;
                    maxR = r;
                }
            }

            if (maxR == null || maxKeyCount <= 0) {
                throw new CatalogException("Impossible to cover all concepts with current resources!");
            }

            uncovered.removeAll(maxR.getKeywords());
            selected.add(maxR);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Selected resources: ");
        selected.forEach(System.out::println);

        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
