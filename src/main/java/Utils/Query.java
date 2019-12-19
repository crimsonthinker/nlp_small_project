package Utils;

import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {
    private HashMap<String,String> info = new HashMap<>();
    private HashMap<String, HashMap<String,String>> database = new HashMap<String, HashMap<String,String>>();
    private int counter = 0;

    public Query() throws IOException, ParseException {
        info.put("BUS",null);
        info.put("DDEST",null);
        info.put("ADEST",null);
        info.put("DTIME",null);
        info.put("ATIME",null);
        info.put("RUN-TIME",null);
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./data/database.json");
        //Read JSON file
        JSONObject jsonObject= (JSONObject) jsonParser.parse(reader);
        for(Object key : jsonObject.keySet()){
            String k = (String)key;
            JSONObject value = (JSONObject) jsonObject.get(k);
            HashMap<String,String> map = new HashMap<>();
            for(Object key2 : value.keySet()){
                map.put((String)key2,(String)value.get(key2));
            }
            database.put(k,map);
        }
    }

    private String getNewID(){
        String result = "t" + Integer.toString(counter);
        counter = counter + 1;
        return result;
    }

    public void put(String key,String value){
        info.replace(key,value);
    }

    public String getQuery(){
        counter = 0;
        //get list of main id
        String mainIDs = "";
        for(Map.Entry<String,String> i : info.entrySet()){
            if(i.getValue() == null){ //null -> ?
                info.replace(i.getKey(),"?" + getNewID());
            }else if(i.getValue().startsWith("x")){ // begin with x -> initial
                info.replace(i.getKey(),"?" + i.getValue());
                mainIDs = mainIDs + i.getValue() + " ";
            }
        }
        return "PRINT-ALL " + mainIDs + "(BUS " + info.get("BUS") + ")"
                + "(ATIME " + info.get("BUS") + " " + info.get("ADEST") + " " + info.get("ATIME").replaceAll("HR","").replaceAll("GIO","").trim() + " HR)"
                + "(DTIME " + info.get("BUS") + " " + info.get("DDEST") + " " + info.get("DTIME").replaceAll("HR","").replaceAll("GIO","").trim() + " HR)"
                + "(RUN-TIME " + info.get("BUS") + " " + info.get("DDEST") + " " + info.get("ADEST") + " " + info.get("RUN-TIME") + " HR)";
    }

    public String getAnswerFromBus(String b){
        HashMap<String,String> busInfo = database.get(b);
        String str = "Xe buýt " + b;
        Boolean aDestAns = false;

        if(info.get("ADEST").startsWith("?")){
            str = str + " đến " + busInfo.get("ADEST");
            aDestAns = true;
        }else if(!info.get("ADEST").equals(busInfo.get("ADEST"))){
            return "";
        }
        Boolean aTimeAns = false;
        if(info.get("ATIME").startsWith("?")){
            if(aDestAns)
                str = str + " vào lúc " + busInfo.get("ATIME") + " HR";
            else
                str = str + " đến vào lúc " + busInfo.get("ATIME") + " HR";
            aTimeAns = true;
        }else if(!info.get("ATIME").replaceAll("HR","").replaceAll("GIO","").trim().equals(busInfo.get("ATIME"))){
            return "";
        }
        Boolean dDestAns = false;
        if(info.get("DDEST").startsWith("?")) {
            if (aTimeAns || aDestAns)
                str = str + ", đi từ " + busInfo.get("DDEST");
            else
                str = str + " đi từ " + busInfo.get("DDEST");
            dDestAns = true;
        }else if(!info.get("DDEST").equals(busInfo.get("DDEST"))){
            return "";
        }
        Boolean dTimeAns = false;
        if(info.get("DTIME").startsWith("?")){
            if(dDestAns)
                str = str + " từ lúc " + busInfo.get("DTIME") + " HR";
            else
                str = str + " đi từ lúc " + busInfo.get("DTIME") + " HR";
            dTimeAns = true;
        }else if(!info.get("DTIME").replaceAll("HR","").replaceAll("GIO","").trim().equals(busInfo.get("DTIME"))){
            return "";
        }
        if(info.get("RUN-TIME").startsWith("?")){
            if(aDestAns || aTimeAns || dDestAns || dTimeAns)
                str = str + " với thời gian chạy là " + busInfo.get("DURATION") + " HR";
            else
                str = str + " chạy với thời gian là " + busInfo.get("DURATION") + " HR";
        }
        return str + ".";
    }

    public String checkBus(String b){
        HashMap<String,String> busInfo = database.get(b);
        String str = "Xe buýt " + b;
        Boolean aDestAns = false;

        if(info.get("ADEST").startsWith("?")){
            str = str + " đến " + busInfo.get("ADEST");
            aDestAns = true;
        }
        Boolean aTimeAns = false;
        if(info.get("ATIME").startsWith("?")){
            if(aDestAns)
                str = str + " vào lúc " + busInfo.get("ATIME") + " HR";
            else
                str = str + " đến vào lúc " + busInfo.get("ATIME") + " HR";
            aTimeAns = true;
        }
        Boolean dDestAns = false;
        if(info.get("DDEST").startsWith("?")) {
            if (aTimeAns || aDestAns)
                str = str + ", đi từ " + busInfo.get("DDEST");
            else
                str = str + " đi từ " + busInfo.get("DDEST");
            dDestAns = true;
        }
        Boolean dTimeAns = false;
        if(info.get("DTIME").startsWith("?")){
            if(dDestAns)
                str = str + " từ lúc " + busInfo.get("DTIME") + " HR";
            else
                str = str + " đi từ lúc " + busInfo.get("DTIME") + " HR";
            dTimeAns = true;
        }
        if(info.get("RUN-TIME").startsWith("?")){
            if(aDestAns || aTimeAns || dDestAns || dTimeAns)
                str = str + " với thời gian chạy là " + busInfo.get("DURATION") + " HR";
            else
                str = str + " chạy với thời gian là " + busInfo.get("DURATION") + " HR";
        }
        return str + ".";
    }

    public String answer(){
        String str = "";
        if(info.get("BUS").startsWith("?")){ //begin iterate
            for(Map.Entry<String,HashMap<String,String>> x : database.entrySet()){
                String b = (String)x.getKey();
                String a = getAnswerFromBus(b);
                str = str + a + "\n";
            }
        }else{ //known bus
            String f = info.get("BUS");
            str = getAnswerFromBus(f) + "\n";
        }
        return str;
    }
}
