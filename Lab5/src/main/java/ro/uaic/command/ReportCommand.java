package ro.uaic.command;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.*;

import ro.uaic.catalog.Catalog;
import ro.uaic.catalog.CatalogException;

/**
 * ReportCommand
 */
public class ReportCommand implements Command {
    Catalog catalog;

    public ReportCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() throws CatalogException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        try {
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateUpdateDelayMilliseconds(0);

            Map<String, Object> root = new HashMap<>();
            root.put("items", this.catalog.getResources());

            Template temp = cfg.getTemplate("report.ftl");
            File outputFile = new File("report.html");

            try (Writer out = new FileWriter(outputFile)) {
                temp.process(root, out);
            }

            Desktop.getDesktop().open(outputFile);
        } catch (Exception e) {
            throw new CatalogException("Failed to generate report: " + e.getMessage());
        }
    }
    
}
