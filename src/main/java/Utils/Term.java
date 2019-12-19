package Utils;

//defining word term
public class Term{
    private String wordSense; //meaning
    private String constant; //nội dung
    private String var; //second var
    public Term(String s, String v,String c){
        wordSense = s;
        var = v;
        constant = c;
    }

    @Override
    public String toString() {
        return "(" + wordSense + " " + var + " " + "\"" + constant + "\"" + ")";
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar(){
        return this.var;
    }

    public String getWordSense(){
        return this.wordSense;
    }

    public String getConstant(){
        return constant;
    }
}
