package noc;
public class ClassInfo {
    private String name;
    private String[] knownSuperclasses;

    public ClassInfo(String n, String[] s){
        name = n;
        knownSuperclasses = s;
    }

    public ClassInfo(String n){
        name = n;
        knownSuperclasses = new String[1];
        knownSuperclasses[0] = "java.lang.Object";
    }

    public ClassInfo(){
        name = "java.lang.Object";
        knownSuperclasses = null;
    }

    public String[] getKnownSuperclass(){
        return knownSuperclasses;
    }

    public String getName(){
        return name;
    }
}
