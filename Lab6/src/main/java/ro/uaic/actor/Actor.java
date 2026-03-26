package ro.uaic.actor;

/**
 * Actor
 */
public class Actor {
    private int id;
    private String firstName;
    private String lastName;

    public Actor(String firstName, String lastName) {
        this.id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Actor(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
