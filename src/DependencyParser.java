import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DependencyParser{
    List<Pair<String, List<String>>> wordTags = new ArrayList<>(); //word -> List of POS tag
    List<Pair<String,Pair<String,String>>> deps = new ArrayList<>(); //dep(wa,wb) -> wa => wb (wa is head, wb is dependent)
    Boolean foundRoot = false;
    String rootVerb = "";

    public DependencyParser(List<Pair<String, List<String>>> wt){
        wordTags = wt;
    }

    private String getDepRelation(Pair<String, List<String>> head, Pair<String, List<String>> dependent){
        //ROOT
        if(head.getKey().equals("ROOT") && dependent.getValue().contains("VB") && !foundRoot){
            rootVerb = dependent.getKey();
            foundRoot = true;
            return "root";
        }
        if(head.getKey().equals("ROOT") || dependent.getKey().equals("ROOT")){
            return "skip"; //exception for root
        }
        //bus_name and xe buyt
        if(head.getValue().contains("BUS_NAME") && dependent.getValue().contains("NSUBJ")){
            return "amod";
        }
        //for verbs
        if(head.getValue().contains("VB")){
            if(dependent.getValue().contains("NSUBJ")){
                return "nsubj";
            }
            if(dependent.getValue().contains("BUS_NAME")){
                return "nsubj";
            }
            if(dependent.getValue().contains("TIME")){
                return "nsubj";
            }
            if(dependent.getValue().contains("NOBJ")){
                return "dobj";
            }
            if(dependent.getValue().contains("P")){
                return "pmod";
            }
            if(dependent.getValue().contains("WH_TIME") || dependent.getValue().contains("WH_LOC") || dependent.getValue().contains("WH_OBJ")){
                return "pobj";
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
        List< Pair<String, List<String>> > sigma = new ArrayList<>();
        sigma.add(new Pair("ROOT",new ArrayList<>()));

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

}