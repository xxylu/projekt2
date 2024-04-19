public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(Person person) {
        super(person.getName() + " " +person.getDeathDate() + " died before "+ person.getBirthDate());
    }
}
