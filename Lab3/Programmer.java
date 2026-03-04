/**
 * Programmer
 */
public class Programmer extends Person {
    String programmingLanguage;

    Programmer(String name, int id) {
        super(name, id);
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
}
