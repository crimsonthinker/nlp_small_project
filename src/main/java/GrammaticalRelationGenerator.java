import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammaticalRelationGenerator {
    //private variables
    List<Pair<String, Pair<String,String>>> deps;
    HashMap<String,String> termList = new HashMap<>();
    int counter= 0;

    //for storing grammatical relation
    private class TripletProposition{
        private String pred;
        private String term1;
        private String term2;
        public TripletProposition(String p, String t1, String t2){
            pred = p;
            term1 = t1;
            term2 = t2;
        }
    }

    //defining word term
    private class Term{
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
            return "(" + wordSense + " " + var + " " + constant + ")";
        }

        public void setVar(String var) {
            this.var = var;
        }
    }

    List<Term> terms = new ArrayList();
    List<TripletProposition> relationList = new ArrayList();

    //constructors
    public GrammaticalRelationGenerator(List<Pair<String, Pair<String,String>>> d) throws IOException, ParseException {
        deps = d;
        //read json file for terms
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./data/terms.json");
        //Read JSON file
        JSONObject jsonObject= (JSONObject) jsonParser.parse(reader);
        for(Object key : jsonObject.keySet()){
            String k = (String)key;
            List<String> value = (List<String>) jsonObject.get(k);
            for(String v : value){
                termList.put(v,k);
            }
        }
    }

    private String getNewID(){
        String result = "id" + Integer.toString(counter);
        counter = counter + 1;
        return result;
    }

    private Boolean isTermExist(Term t){
        for(Term term : terms){
            if(t.wordSense.equals(term.wordSense) && t.constant.equals(term.constant)){
                return true;
            }
        }
        return false;
    }

    private void getTerm(String v){
        for(Map.Entry<String,String> entry : termList.entrySet()){
            if(v.matches(entry.getKey()) || v.equals(entry.getKey())){
                Term t = new Term(entry.getValue(),"null",v);
                //check if term exist
                if(!this.isTermExist(t)){
                    t.setVar(this.getNewID());
                    terms.add(t);
                    return;
                }
            }
        }
    }
    public void generate() throws IOException {
        //reset counter
        counter = 0;
        //first loop : get terms
        for(Pair<String, Pair<String,String>> relation : deps){
            //get current terms
            this.getTerm(relation.getValue().getKey());
            this.getTerm(relation.getValue().getValue());
            //get grammatical relation
        }
        //second loop : get
        System.out.println(terms);
    }
}