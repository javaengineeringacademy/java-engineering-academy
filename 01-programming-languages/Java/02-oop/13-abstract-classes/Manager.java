public class Manager extends Employee {

    private final int teamSize;

    public Manager(String name, String id, double salary, int teamSize) {
        super(name, id, salary);
        this.teamSize = teamSize;
    }

    @Override
    public double calculateBonus() {
        return salary * 0.20 + (teamSize * 1000);
    }

    @Override
    public String getRole() {
        return "Manager";
    }

    public int getTeamSize() { return teamSize; }
}