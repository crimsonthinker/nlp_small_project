import javafx.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class POSTagger {
    //assume all are lowercase
    List<String> segmentedWords = new ArrayList<>(); //has to be ascii
    List<Pair<String,List<String>>> wordTags = new ArrayList<>();
    HashMap<String,List<String>> tags = new HashMap<>();

    private static Boolean isTime(List<String> regs, String t){
        for(String reg : regs){
            if(t.matches(reg)){
                return true;
            }
        }
        return false;
    }

    public POSTagger(List<String> s) throws IOException,ParseException{
        segmentedWords = s;
        //read json
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./corpus/pos_tag.json");
        //Read JSON file
        JSONObject jsonObject= (JSONObject) jsonParser.parse(reader);
        for(Object key : jsonObject.keySet()){
            String k = (String)key;
            List<String> value = (List<String>) jsonObject.get(k);
            tags.put(k,value);
        }
    }

    public void getPOSTag(){
        //first priority: V over P
        for(String w : segmentedWords){
            List<String> tagList = new ArrayList();
            for(Map.Entry<String,List<String>> tag : tags.entrySet()){
                String tagName = tag.getKey();
                List<String> cTagList = tag.getValue();
                if((tagName == "TIME" && isTime(cTagList,w)) ||
                        (tagName != "TIME" && cTagList.contains(w))){
                    tagList.add(tagName);
                }
            }
            wordTags.add(new Pair(w,tagList));
        }
    }

    public List<Pair<String , List<String> >> getPOSList(){
        return wordTags;
    }

}
