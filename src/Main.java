import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> people = Person.fromCsv("family.csv");
        System.out.println(people.get(3).generateUML());
        //for(Person person : people){
        //    System.out.println(person);
        //}
        PlanUMLRunner.setPath("plantuml-1.2024.4.jar");
        String uml = people.get(3).generateUML();
        PlanUMLRunner.generateDiagram(uml,"./", "out2");



    }
}