package ro.uaic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Lab 4
 *
 */
public class App {
    static void compulsory() {
        System.out.println("Compulsory:");

        List<Intersection> intersections =
            IntStream.range(0, 10)
            .mapToObj(num -> new Intersection("Intersection " + num))
            .collect(Collectors.toList());

        List<Street> streets = new LinkedList<>();

        for (int i = 1; i < 10; ++i) {
            streets.add(new Street("Street " + i, 10, intersections.get(i - 1), intersections.get(i)));
        }

        streets.sort((s1, s2) -> s1.compareTo(s2));

        System.out.println(intersections);
        System.out.println(streets);

        Set<Intersection> intersectionsSet = new HashSet<>(intersections);
        System.out.println("HashSet size (before): " + intersectionsSet.size());
        
        intersectionsSet.add(new Intersection("Intersection 1"));
        
        System.out.println("HashSet size (after): " + intersectionsSet.size());
    }

    static void homework() {
        System.out.println("Homework:");
        
        City c = new City("Chongqing");
        c.generate(10, 20);
        c.query(75);

        System.out.println("\n\n\n 3 Solutions:");
        c.get_k_msts(3);
    }

    static void advanced() {
        System.out.println("Advanced:");
        
        City c = new City("Chengdu");
        c.generate(10, 20);

        c.tsp();
    }

    public static void main(String[] args) {
        compulsory();
        homework();
        advanced();
    }
}
