public enum Day {

    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri"),
    SATURDAY("Sat"),
    SUNDAY("Sun");

    private final String abbreviation;

    Day(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() { return abbreviation; }

    public boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }

    public Day next() {
        Day[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}