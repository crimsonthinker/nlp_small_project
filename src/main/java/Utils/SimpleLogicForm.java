package Utils;

import java.util.ArrayList;
import java.util.List;

public class SimpleLogicForm {
    private String whatToAsk = "";
    private List<String> id = new ArrayList<>();
    private List<LogicUnit> theList = new ArrayList<>();

    public void addID(String i){
        id.add(i);
    }

    public void declareWhatToAsk(String wta){
        whatToAsk = wta;
    }

    public void add(LogicUnit u){
        theList.add(u);
    }

    public List<String> getIDList(){
        return id;
    }

    public List<LogicUnit> getLogicList(){
        return theList;
    }

    @Override
    public String toString() {
        String idList = "?" + id.get(0);
        if(id.size() > 1){
            for(int i = 1 ; i < id.size() ; i++){
                idList = idList + ", ?" + id.get(i);
            }
        }
        String l = "";
        for(LogicUnit x : theList){
            l = l + x;
        }
        return "(" + whatToAsk + " " + idList + " & " + l + ")";
    }
}
