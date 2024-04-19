//package org.example.ooplab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Przyklad uzycia listy i mapy
public class Osoba {
    int pesel;
    String name;
    public Osoba(int pesel, String name) {
        this.pesel = pesel;
        this.name = name;
    }
    @Override
    public String toString() {
        return "Osoba{" +
                "pesel=" + pesel +
                ", name='" + name + '\'' +
                '}';
    }
    //Przyklad uzycia list i map:
    public static void main(String[] args) {
        List<Osoba> osoby = new ArrayList<>();
        Osoba o1 = new Osoba(111,"alice");
        Osoba o2 = new Osoba(222,"bob");
        Osoba o3 = new Osoba(111,"carl"); //duplikat peselu!!

        osoby.add(o1);
        osoby.add(o2);
        osoby.add(o3); //duplikat

        for (int i=0;i<osoby.size();i++)
            System.out.println(osoby.get(i));
        osoby.remove(o3); // lub: osoby.remove(2) w remove object uwaga na brak implementacji equals porównanie referencji!
        // to by nie zadziałało: osoby.remove(new Osoba(111,"carl"));

        for(Osoba o : osoby){
            System.out.println(o);
        }

        osoby.add(o3);
        Map<Integer,Osoba> mapa = new HashMap<>();
        for (int i=0; i<osoby.size();i++){
            var oc = osoby.get(i);
            //uwaga trzeba by napisać logikę jeżeli nie chcemy nadpisać klucza
            //if (!mapa.containsKey(oc.pesel))
            mapa.put(oc.pesel,oc);
        }

        //pobieranie Map.Entry - jest to para klucz-wartosc:
        for (Map.Entry<Integer, Osoba> para : mapa.entrySet()) {
            Integer klucz = para.getKey();
            Osoba wartosc = para.getValue();
            System.out.println(klucz + " -> " + wartosc);
        }
        //przechodzenie przez wartosci:
        for (Osoba o : mapa.values()){
            o.name="ZMIANA "+o.name;
        }

        //przechodzenie przez klucze:
        for (Integer pesel  : mapa.keySet()){
            System.out.println(pesel);
        }

        //wyszukanie po kluczu:
        System.out.println("wyszukanie: "+mapa.get(111));

        //usuniecie z mapy:
        mapa.remove(111);
        System.out.println(mapa);

        //referencje w obiektach, liscie i mapie odnoszą się do tych samych obiektów osób:
        //pamiętamy, że w mapie nie ma alice bo wpis został nadpisany.
        //Reszta została zmieniona w mapie.
        System.out.println(osoby);
    }
}
