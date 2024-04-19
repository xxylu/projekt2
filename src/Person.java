import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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