/**
 * Designer
 */
public class Designer extends Person {
    int skillLevel;

    Designer(String name, int id) {
        super(name, id);
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
}
