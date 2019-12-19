import Utils.LogicUnit;
import Utils.Query;
import Utils.SimpleLogicForm;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class ProceduralSemanticsGenerator{
    private SimpleLogicForm slf;
    private Query q;
    int counter = 0;

    private String getNewID(){
        String result = "x" + Integer.toString(counter);
        counter = counter + 1;
        return result;
    }

    ProceduralSemanticsGenerator(SimpleLogicForm g) throws ParseException, IOException {
        q = new Query();
        slf = g;
    }

    public void generate() {
        List<String> askIDs = slf.getIDList();
        List<LogicUnit> logicList = slf.getLogicList();
        for (LogicUnit lu : logicList) {
            //if it is unknown
            if (lu.getName().equals("WH_OBJ") || lu.getName().equals("ATIME") || lu.getName().equals("DTIME")) {
                String f = lu.getName();
                if(lu.getName().equals("WH_OBJ"))
                    f = "BUS";
                if (lu.getTerm() == null) {
                    q.put(f, lu.getID());
                } else {
                    q.put(f, lu.getTerm().getConstant().toUpperCase());
                }
            }
            if (lu.getName().equals("ADEST") || lu.getName().equals("DDEST")) {
                if (lu.getTerm() == null) {
                    q.put(lu.getName(), lu.getID());
                } else {
                    q.put(lu.getName(), lu.getTerm().getWordSense());
                }
            }
            if(lu.getName().equals("RUN-TIME")){ //always a mystery
                q.put(lu.getName(),lu.getID());
            }
            if(lu.getName().equals("BUS")){
                String f = lu.getName();
                if (lu.getTerm() == null) {
                    q.put(f, lu.getID());
                } else {
                    q.put(f, lu.getTerm().getConstant().toUpperCase());
                }
            }
        }
    }

    public String getQuery(){
        return q.getQuery();
    }

    public String getAnswer(){
        return q.answer();
    }
}
