package ro.uaic;

import java.util.Random;

import ro.uaic.catalog.Catalog;
import ro.uaic.command.AddCommand;
import ro.uaic.resource.Resource;
import ro.uaic.resource.WebResource;

/**
 * Generator
 */
public class Generator {
    public static String[] possibleKeywords = {
        "Algebra Liniara si Optimizare", "Algoritmica grafurilor", "Analiza experimentală a algoritmilor",
        "Arhitectura calculatoarelor şi sisteme de operare", "Baze de date", "Big data analytics",
        "Blockchain: Fundamente și Aplicații", "Calcul Numeric", "Calitatea Sistemelor Software",
        "Capitole Speciale de Invatare Automata", "Capitole Speciale de Sisteme de Operare",
        "Cercetari Operationale", "Cloud Computing", "Rețele de Calculatoare",
        "Capitole Avansate in Vedere Artificiala", "Programare Concurenta si Distribuita",
        "Data Mining", "Dezvoltarea Sistemelor Fizice Utilizand Microprocesoare",
        "Etica si Integritate Academica", "Grafica pe Calculator si Geometrie Computationala",
        "Ingineria programării", "Internet of Things", "Introducere în .NET",
        "Introducere in Programare", "Introducere in realitati mixte", "Invatare Automata",
        "Limba Engleza", "Limbaje Formale Automate si Compilatoare", "Logica pentru Informatica",
        "Matematica: calcul diferential si integral", "Metode Formale in Ingineria Software",
        "Modelarea Sistemelor Distribuite", "Sisteme de Operare si Retele de Calculatoare",
        "Practica Pedagogica in Invatamantul Preuniversitar", "Practica Sisteme de Gestiune pentru Baze de Date",
        "Prelucrarea Statistica a Limbajului Natural", "Principii ale Limbajelor de Programare",
        "Probabilitati si statistica", "Programare Avansată", "Programare Competitivă",
        "Programare probabilista", "Proiectarea Algoritmilor", "Proiectarea Jocurilor",
        "Protocoale de Securitate: Modelare si Verificare", "Quantum computing",
        "Retele Petri si Aplicatii", "Securitate Software", "Securitatea Retelelor de Calculatoare",
        "Sisteme Bazate pe Evenimente", "Sisteme de Operare", "Sisteme de Operare Distribuite",
        "Sisteme embedded", "Smart Card-uri si Aplicatii", "Specificarea si Verificarea Sistemelor",
        "Statistica Computationala", "Structuri de date", "Tehnici avansate de ingineria programarii",
        "Tehnici de Programare Multiprocesor", "Tehnici de prelucrare a limbajului natural",
        "Tehnologii Java", "Topici avansate în .NET", "Bazele Dezvoltarii Web", "Tehnologii Web"
    };

    public static void generateRandom(Catalog catalog, int resourceCount, int keywordsPerResource) {
        AddCommand add = new AddCommand(catalog);
        Random rand = new Random();

        for (int i = 0; i < resourceCount; ++i) {
            Resource r = new WebResource("id" + i, "Resource " + i, "https://edu.info.uaic.ro/" + i, 2026, "Emy");

            for (int j = 0; j < keywordsPerResource; ++j) {
                r.addKeyword(possibleKeywords[rand.nextInt(possibleKeywords.length)]);
            }

            try {
                add.setResource(r);
                add.execute();
            } catch (Exception e){
                System.err.println("[Error] " + e);
            }
        }
    }
    
}
