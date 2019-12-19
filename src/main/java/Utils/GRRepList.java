package Utils;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GRRepList {
    private List<Term> terms;
    private List<TripletProposition> relationList;

    public GRRepList(){
        terms = new ArrayList<>();
        relationList = new ArrayList<>();
    }

    public void add(Term t){
        terms.add(t);
    }

    public void removeTerm(int index){
        terms.remove(index);
    }

    public Boolean isTermExist(Term t){
        for(Term term : terms){
            if(t.getConstant().equals(term.getConstant())){
                return true;
            }
        }
        return false;
    }

    public String getIDFromTermList(String constant){
        for(Term term : terms){
            if(term.getConstant().equals(constant)){
                return term.getVar();
            }
        }
        return "null";
    }

    public Term getTermFromID(String id){
        for(Term term : terms){
            if(term.getVar().equals(id)){
                return term;
            }
        }
        return null;
    }

    public Term getTermFromWordSense(String wordSense){
        for(Term term : terms){
            if(term.getWordSense().equals(wordSense)){
                return term;
            }
        }
        return null;
    }

    public Term getTermFromConstant(String constant){
        for(Term term : terms){
            if(term.getConstant().equals(constant)){
                return term;
            }
        }
        return null;
    }

    public String getWordSenseFromConstant(String constant){
        for(Term term : terms){
            if(term.getConstant().equals(constant)){
                return term.getWordSense();
            }
        }
        return "null";
    }

    public TripletProposition getTripletFromPred(String pred){
        for(TripletProposition tp : relationList){
            if(tp.getPred().equals(pred)){
                return tp;
            }
        }
        return null;
    }

    public TripletProposition getTripletFromTerm1(String term1){
        for(TripletProposition tp : relationList){
            if(tp.getTerm1().equals(term1)){
                return tp;
            }
        }
        return null;
    }

    public TripletProposition getTripletFromTerm2(String term2){
        for(TripletProposition tp : relationList){
            if(tp.getTerm2().equals(term2)){
                return tp;
            }
        }
        return null;
    }

    public void add(TripletProposition tp){
        relationList.add(tp);
    }

    public void removeTriplet(int index){
        relationList.remove(index);
    }

    public Integer sizeTermList(){
        return terms.size();
    }

    public Integer sizeRelationList(){
        return relationList.size();
    }

    public List<TripletProposition> getRelationList(){
        return relationList;
    }

    public List<Term> getTermList(){
        return terms;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0 ; i < relationList.size() ; i++){
            if(relationList.get(i).getPred().equals("LOBJ") &&
            relationList.get(i).getTerm2().equals("AT")){
                //find the time
                TripletProposition tp = new TripletProposition(relationList.get(i).getPred(),
                        relationList.get(i).getTerm1(),relationList.get(i).getTerm2());
                tp.setTerm2("TIME");
                str = str + tp.toString();
                Term t = getTermFromWordSense(tp.getTerm2());
                if(t != null){
                    str = str + " " + t.toString();
                }
                str = str + "\n";
            }
            else if(relationList.get(i).getPred().equals("LSUBJ") ||
                    relationList.get(i).getPred().equals("LOBJ")){
                str = str + relationList.get(i).toString();
                Term t = getTermFromWordSense(relationList.get(i).getTerm2());
                if(t != null){
                    str = str + " " + t.toString();
                }
                str = str + "\n";
            }else if(relationList.get(i).getTerm2().equals("WH_TIME") ||
                    relationList.get(i).getTerm2().equals("WH_LOC") ||
                    relationList.get(i).getTerm2().equals("WH_OBJ")){
                str = str + relationList.get(i).toString();
                str = str + "\n";
            }
            else if(relationList.get(i).getPred().equals("TNS")){
                Term t = getTermFromID(relationList.get(i).getTerm1()); //PRED
                str = str + t.toString();
                str = str + " " + relationList.get(i).toString();
                str = str + "\n";
            }
        }

        String x = "";
        for(Term t : terms){
            x = x + t.toString();
            x = x + ", ";
        }
        x = x + "\n";
        for(int i = 0 ; i < relationList.size() ; i++){
            x = x + relationList.get(i).toString();
            if(i != relationList.size() - 1){
                x = x + ", ";
            }
        }

        return str;
    }
}
