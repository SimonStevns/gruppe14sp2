package Domain;

public class Testclasses {


    public static void main(String[] args) {
        Residence r = new Residence("Humlehuset","8gange8","test@test.dk", "Cortex park 24");
        
        Ward w1 = new Ward(r.numberOfWards(), "Afvening");
        Resident r1 = new Resident("Andreas", "50509433", "999999-9999");
        w1.addResident(r1);
        w1.addResident(new Resident("Andreas1", "505094331", "999999-99991"));
        
        r.addWard(w1);
        
        Ward w2 = new Ward(r.numberOfWards(), "");
        
        w2.addResident(new Resident("Andreas", "50509433", "999999-9999"));
        w2.addResident(new Resident("Andreas1", "505094331", "999999-99991"));
        
        r.addWard(w2);
        
        System.out.println(r.toString());
    }
    
}
