package Utils;

public class LogicUnit {
    private String name = null;
    private Term term = null;
    private String id = null;

    public LogicUnit(String n,Term t){
        name = n;
        term = t;
    }

    public LogicUnit(String n,String i){
        name = n;
        id = i;
    }

    public String getName(){
        return name;
    }

    public Term getTerm(){
        return term;
    }

    public String getID(){
        return id;
    }

    @Override
    public String toString() {
        if(term == null){
            return "(" + name + " ?" + id + ")";
        }else{
            return "(" + name + " " + term.toString() + ")";
        }
    }
}
