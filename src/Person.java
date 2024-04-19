import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;
    private final List<Person> parents;
    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.parents = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public void addParent(Person person) {
        parents.add(person);
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public LocalDate getDeathDate() {
        return deathDate;
    }
    public static Person fromCsvLine(String line){

        String[] parts = line.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = null;

        if(!parts[2].equals(""))
        {
            deathDate = LocalDate.parse(parts[2], formatter);
        }

        return new Person(parts[0], birthDate, deathDate);
    }
    public static List<Person> fromCsv(String path){
        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentsNames>  personWithParentsNamesMap = new HashMap<>();
        String line;
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            br.readLine();

            while((line = br.readLine()) != null){
                PersonWithParentsNames personWithParentsNames = PersonWithParentsNames.fromCsvLine(line);
                Person person = personWithParentsNames.getPerson();
                try{
                    person.lifespanValidate();
                    people.add(person);
                    personWithParentsNamesMap.put(person.name, personWithParentsNames);
                }catch (NegativeLifespanException e) {
                    System.err.println(e.getMessage());
                }
            }
            PersonWithParentsNames.fillParent(personWithParentsNamesMap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return people;
    }
    public String generateUML(){
        StringBuilder sb = new StringBuilder();
        Function<Person, String> deleteSpaces = p -> p.getName().replaceAll(" ", "");
        Function<Person,String> addObject = p -> "object " + deleteSpaces.apply(p);
        String nameSurname = deleteSpaces.apply(this);
        sb.append("@startuml\n" + addObject.apply(this));
        if(!parents.isEmpty()){
            sb.append(parents.stream().map(p -> "\n"
                    + addObject.apply(p)
                    + "\n" + deleteSpaces.apply(p)
                    + "<--" + nameSurname + "\n")
                    .collect(Collectors.joining()));
        }
        sb.append("\n@enduml");
        return sb.toString();
    }

    public static String generateUML(List<Person> people){
        StringBuilder sb = new StringBuilder();
        Function<Person, String> deleteSpaces = p -> p.getName().replaceAll(" ", "");
        Function<Person,String> addObject = p -> "object " + deleteSpaces.apply(p);
        sb.append("@startuml\n");
        sb.append(people.stream()
                .map(p -> "\n" + addObject.apply(p))
                .collect(Collectors.joining()));

        people.stream()
                .flatMap(person -> person.parents.isEmpty() ? Stream.empty()
                        : person.parents.stream()
                            .map(p -> "\n" + deleteSpaces.apply(p) + "<-- " + deleteSpaces.apply(person))).collect(Collectors.joining());
        sb.append("\n@enduml");
        return sb.toString();
    }

    public static List<Person> filterByName(List<Person> people, String substring){
        return people.stream().filter(p -> p.getName().contains(substring)).collect(Collectors.toList());
    }

    public static List<Person> sortByLifeSpan(List<Person> people){
        Function<Person,Long> birthDateToLong = p -> p.birthDate.toEpochDay();
        return people.stream().sorted(o1,o2) -> Long.compare(birthDateToLong.apply(o1),birthDateToLong.apply(o1));
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", parents=" + parents +
                '}';
    }
    public void lifespanValidate() throws NegativeLifespanException {
        if(this.deathDate != null && this.deathDate.isBefore(this.birthDate)){
            throw new NegativeLifespanException(this);
        }
    }
}