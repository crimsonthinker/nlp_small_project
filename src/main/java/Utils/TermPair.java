package Utils;

public class TermPair{
    private String name;
    private String constant;
    public TermPair(String n, String c){
        name =  n;
        constant = c;
    }

    @Override
    public String toString() {
        return "(" + name + " " + constant + ")";
    }

    public String getName(){
        return name;
    }

    public String getConstant(){
        return constant;
    }
}