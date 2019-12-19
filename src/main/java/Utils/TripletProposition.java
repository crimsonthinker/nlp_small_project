package Utils;

//for storing grammatical relation
public class TripletProposition{
    private String pred;
    private String term1; //ảnh hưởng
    private String term2; //tác nhân
    public TripletProposition(String p, String t1, String t2){
        pred = p;
        term1 = t1;
        term2 = t2;
    }
    @Override
    public String toString() {
        return "(" + term1 + " " + pred + " " + term2 + ")";
    }

    @Override
    public boolean equals(Object obj) {
        TripletProposition t = (TripletProposition) obj;
        return t.getTerm1().equals(term1) && t.getTerm2().equals(term2) && t.getPred().equals(pred);
    }

    public String getTerm2(){
        return term2;
    }

    public String getTerm1(){
        return term1;
    }

    public String getPred()  { return pred; }

    public void setTerm2(String newTerm2){
        term2 = newTerm2;
    }
}
