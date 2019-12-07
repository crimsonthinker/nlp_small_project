import javafx.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DependencyParser{
    List<Pair<String, List<String>>> wordTags = new ArrayList<>(); //word -> List of POS tag
    HashMap<String, HashMap<String,String>> depRelationList = new HashMap<>(); // list of dependency relations
    List<Pair<String,Pair<String,String>>> deps = new ArrayList<>(); //dep(wa,wb) -> wa => wb (wa is head, wb is dependent)
    Boolean foundRoot = false;

    public DependencyParser(List<Pair<String, List<String>>> wt) throws IOException,ParseException {
        wordTags = wt;
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./data/dependency_relation.json");
        //Read JSON file
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        for(Object obj : jsonObject.keySet()){
            String h = (String) obj;
            JSONObject jsonObject2 = (JSONObject) jsonObject.get(h);
            depRelationList.put(h,new HashMap<>());
            for(Object obj2 : jsonObject2.keySet()){
                String d = (String) obj2;
                String depValue = (String) jsonObject2.get(d);
                depRelationList.get(h).put(d,depValue);
            }
        }
    }

    private String getDepRelation(Pair<String, List<String>> head, Pair<String, List<String>> dependent){
        if(head.getKey().equals("ROOT") && dependent.getValue().contains("VB")){
            System.out.println("f");
        }
        if(head.getKey().equals("ROOT") && dependent.getKey().equals("ROOT")){
            return "skip"; //exception for root
        }else{
            for(String h : head.getValue()){
                if(depRelationList.containsKey(h)){
                    for(String d : dependent.getValue()){
                        //get dependency
                        if(depRelationList.get(h).containsKey(d)){
                            String val = depRelationList.get(h).get(d);
                            if(val.equals("root") && !foundRoot){
                                foundRoot = true;
                            }else if(val.equals("root") && foundRoot){
                                continue;
                            }
                            return val;
                        }
                    }
                }
            }
        }
        return "skip";
    }

    private Boolean isDependentExist(Pair<String , List<String>> wb){
        for(Pair<String , Pair<String,String>> dep : deps){
            if(dep.getValue().getValue().equals(wb.getKey())){
                return true;
            }
        }
        return false;
    }


    //malt parser ("arc-eager")
    public void parse(){
        List< Pair<String, List<String>> > sigma = new ArrayList();
        //initialization
        sigma.add(new Pair("ROOT",new ArrayList()));
        sigma.get(0).getValue().add("ROOT");

        //beta is wordTags
        List<Pair<String, List<String>>> beta = wordTags;
        //alpha is deps
        List<Pair<String,Pair<String,String>>> alpha = deps;

        //for each word
        Integer i = 0;
        while(i <= beta.size() - 1){
            //i is always the first index
            Pair<String , List<String>> wi;
            Pair<String , List<String>> wj;
            //left-arc
            wi = sigma.get(sigma.size() - 1);
            Boolean laPredCond = !isDependentExist(wi);
            if(laPredCond){
                Boolean foundLA = false;
                for(int t = i ; t < beta.size() ; t++){
                    wj = beta.get(t);
                    String depRelation = getDepRelation(wj,wi);
                    if(!depRelation.equals("skip")) {
                        alpha.add(new Pair(depRelation, new Pair(wj.getKey(), wi.getKey())));
                        if(sigma.size() > 1)
                            sigma.remove(sigma.size() - 1);
                        foundLA = true;
                        break;
                    }
                }
                if(foundLA){
                    continue;
                }
            }

            //right-arc
            wj = beta.get(i);
            Boolean raPredCond = !isDependentExist(wj);
            if(raPredCond){
                Boolean foundRA = false;
                for(int t = sigma.size() - 1 ; t >= 0 ; t--){
                    wi = sigma.get(t);
                    String depRelation = getDepRelation(wi,wj);
                    if(!depRelation.equals("skip")) {
                        alpha.add(new Pair(depRelation, new Pair(wi.getKey(), wj.getKey())));
                        sigma.add(wj);
                        i = i + 1;
                        foundRA = true;
                        break;
                    }
                }
                if(foundRA){
                    continue;
                }
            }


            //reduce
            wi = sigma.get(sigma.size() - 1);
            Boolean rPreCond = isDependentExist(wi);
            if(rPreCond){
                if(sigma.size() > 1){
                    sigma.remove(sigma.size() - 1);
                    continue;
                }
            }

            //shift
            if(i <= beta.size() - 1){
                wi = beta.get(i);
                sigma.add(wi);
                i = i + 1;
                continue;
            }
        }

        System.out.println(deps);
    }

    public List<Pair<String,Pair<String,String>>> getDependencies(){
        return deps;
    }
}