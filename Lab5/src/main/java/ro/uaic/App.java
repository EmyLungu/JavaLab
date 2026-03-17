package ro.uaic;

import ro.uaic.catalog.*;

/**
 * App
 */
public class App {
    public static void main(String[] args) {
        Catalog catalog = new Catalog("Catalog");

        Resource cv = new FileResource("emyCV", "Curriculum vitae", "/home/eeemy/Downloads/EmyCV.pdf", 2026, "Lungu Emanuel");
        Resource meowl = new WebResource("moew1", "Classic Moewl", "https://static.wikia.nocookie.net/outcomememoriesfanon/images/f/f0/MeowlBG.jpg/revision/latest/thumbnail/width/360/height/360?cb=20251020201838", 2013, "Moewl's mom");

        Resource xwing = new FileResource("xwing", "Sudoku X-Wing", "/home/eeemy/Pictures/first_xwing.png", 2026, "Lungu Emanuel");
        Resource errEx = new FileResource("err", "Example of error (non existent file)", "/should/be/error.emy", 2025, "Error");

        catalog.addResource(cv);
        catalog.addResource(meowl);
        catalog.addResource(xwing);
        catalog.addResource(errEx);

        catalog.print();

        try {
            meowl.view();
            cv.view();
            xwing.view();
            errEx.view();
        } catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }
}
