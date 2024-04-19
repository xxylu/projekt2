import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonWithParentsNames {

    private Person person;
    private List<String> parentsNames;

    public PersonWithParentsNames(Person person, List<String> names) {
        this.person = person;
        this.parentsNames = names;
    }

    public Person getPerson() {
        return person;
    }

    public static PersonWithParentsNames fromCsvLine(String line){
        String[] parts = line.split(",", -1);
        Person person = Person.fromCsvLine(line);
        List<String> names = new ArrayList<>();

        for(int i = 3; i <= 4; i++){
            if(!parts[i].isEmpty()){
                names.add(parts[i]);
            }
        }
        return new PersonWithParentsNames(person, names);
    }

    public static void fillParent(Map<String, PersonWithParentsNames> map){

        for(PersonWithParentsNames helperChild : map.values()){
            for(String name : helperChild.parentsNames){
                PersonWithParentsNames helperParent = map.get(name);
                Person parent = helperParent.getPerson();
                helperChild.getPerson().addParent(parent);
            }
        }
    }
}