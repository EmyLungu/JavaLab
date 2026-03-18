package ro.uaic;

import ro.uaic.resource.*;
import ro.uaic.catalog.*;
import ro.uaic.command.AddCommand;
import ro.uaic.command.Command;
import ro.uaic.command.ListCommand;
import ro.uaic.command.LoadCommand;
import ro.uaic.command.OptimizeCommand;
import ro.uaic.command.ReportCommand;
import ro.uaic.command.SaveCommand;
import ro.uaic.command.ViewCommand;

/**
 * App
 */
public class App {
    public static void manualLoad(Catalog catalog) {
        Resource cv = new FileResource("emyCV", "Curriculum vitae", "/home/eeemy/Downloads/EmyCV.pdf", 2026, "Lungu Emanuel");
        Resource meowl = new WebResource("moew1", "Classic Moewl", "https://static.wikia.nocookie.net/outcomememoriesfanon/images/f/f0/MeowlBG.jpg/revision/latest/thumbnail/width/360/height/360?cb=20251020201838", 2013, "Moewl's mom");

        Resource xwing = new FileResource("xwing", "Sudoku X-Wing", "/home/eeemy/Pictures/first_xwing.png", 2026, "Lungu Emanuel");
        Resource errEx = new FileResource("err", "Example of error (non existent file)", "/should/be/error.emy", 2025, "Error");

        try {
            AddCommand add = new AddCommand(catalog);
            add.setResource(cv);
            add.execute();
            add.setResource(meowl);
            add.execute();
            add.setResource(xwing);
            add.execute();
            add.setResource(errEx);
            add.execute();

            SaveCommand save = new SaveCommand(catalog, "catalog.emysave");
            save.execute();

        } catch (Exception e){
            System.err.println("[Error] " + e);
        }
    }

    public static void advanced(Catalog catalog) {
        Generator.generateRandom(catalog, 100_000, 5);

        Command optimize = new OptimizeCommand(catalog, Generator.possibleKeywords);
        try {
            optimize.execute();
        } catch (Exception e){
            System.err.println("[Error] " + e);
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Should provide 1 arg [1 = Compulsory | 2 = Homework | 3 = Advanced]");
        }

        int ex = Integer.parseInt(args[0]);
        Catalog catalog = new Catalog("Catalog");

        if (ex == 1) {
            manualLoad(catalog);
        } else if (ex == 2) {
            try {
                LoadCommand load = new LoadCommand(catalog, "catalog.emysave");
                load.execute();
                System.err.println("Loaded from file successfully");
            } catch (Exception e) {
                System.err.println("[Error] " + e);
            }
        }
        else if (ex == 3) {
            advanced(catalog);

            return;
        }

        try {
            ListCommand list = new ListCommand(catalog);
            list.execute();

            ViewCommand view = new ViewCommand(catalog);
            view.setResourceID("xwing");
            view.execute();
            view.setResourceID("emyCV");
            view.execute();

            if (ex == 2) {
                ReportCommand report = new ReportCommand(catalog);
                report.execute();
            }
        } catch (Exception e){
            System.err.println("[Error] " + e);
        }
    }
}
