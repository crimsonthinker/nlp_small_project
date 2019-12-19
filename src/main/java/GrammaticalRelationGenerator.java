import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.*;

public class GrammaticalRelationGenerator {
    //private variables
    List<Pair<String, Pair<String,String>>> deps;
    GRRepList fullList = new GRRepList();
    HashMap<String,List<String>> relationConverter = new HashMap<>();
    int counter= 0;

    List<TermPair> termsInPair = new ArrayList<>();
    HashMap<String,String> termList = new HashMap<>();

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

        //read  json file for grammatical relations
        jsonParser = new JSONParser();
        reader = new FileReader("./data/grammatical_relation.json");
        jsonObject = (JSONObject) jsonParser.parse(reader);
        for(Object key : jsonObject.keySet()){
            String k = (String)key;
            List<String> value = (List<String>) jsonObject.get(k);
            relationConverter.put(k,value);
        }
    }

    private String getNewID(){
        String result = "id" + Integer.toString(counter);
        counter = counter + 1;
        return result;
    }

    private Boolean isTermExist(TermPair t){
        for(TermPair pair : termsInPair){
            if(pair.getName().equals(t.getName())){
                return true;
            }
        }
        return false;
    }

    private String getNameFromTermPair(String constant){
        for(TermPair t : termsInPair){
            if(t.getConstant().equals(constant)){
                return t.getName();
            }
        }
        return "null";
    }



    private void getTerm(String v){
        for(Map.Entry<String,String> entry : termList.entrySet()){
            if(v.matches(entry.getKey()) || v.equals(entry.getKey())){
                Term t = new Term(entry.getValue(),"null",v);
                //check if term exist
                if(!fullList.isTermExist(t) && !this.isTermExist(new TermPair(v.toUpperCase(),v))){
                    t.setVar(this.getNewID());
                    fullList.add(t);
                    //Name for cities
                    return;
                }
            }
        }
        TermPair t = new TermPair(v.toUpperCase(),v);
        if(!this.isTermExist(t) && !fullList.isTermExist(new Term("null","null",v))){
            termsInPair.add(t);
            return;
        }

    }

    private void getGrammaticalRelationRepresentation(Pair<String, Pair<String,String>> r){
        if(relationConverter.containsKey(r.getKey())){
            List<String> gr = relationConverter.get(r.getKey());
            //get id
            //if is TNS
            for(String g : gr){
                if(g.equals("TNS")) {
                    //get id of current word
                    TripletProposition tp = new TripletProposition("TNS", fullList.getIDFromTermList(r.getValue().getValue()), "PRES");
                    fullList.add(tp);
                }else if(g.equals("SELF_NAME")) { //state where predicate is the name of verb -> find term
                    Term t = fullList.getTermFromConstant(r.getValue().getKey());
                    String selfName = t.getWordSense();
                    String id = t.getVar();
                    String name = getNameFromTermPair(r.getValue().getValue());
                    if (name.equals("null")) { //WH?
                        name = fullList.getWordSenseFromConstant(r.getValue().getValue());
                    }
                    TripletProposition tp = new TripletProposition(selfName, id, name);
                    fullList.add(tp);
                }else if(g.equals("PRED")){
                    Term t = fullList.getTermFromConstant(r.getValue().getValue());
                    String id = fullList.getIDFromTermList(r.getValue().getValue());
                    TripletProposition tp = new TripletProposition(g,id,t.getWordSense());
                    fullList.add(tp);
                }else{
                    String id = fullList.getIDFromTermList(r.getValue().getKey());
                    String name = "";
                    name = getNameFromTermPair(r.getValue().getValue());
                    if(name.equals("null")){
                        name = fullList.getWordSenseFromConstant(r.getValue().getValue());

                    }
                    TripletProposition tp = new TripletProposition(g,id,name);
                    fullList.add(tp);
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
            this.getGrammaticalRelationRepresentation(relation);
        }
    }

    public GRRepList getGrammaticalRelation(){
        return fullList;
        //only get important first term and ids
    }
}