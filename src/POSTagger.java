import javafx.util.Pair;

import java.util.*;

public class POSTagger {
    //assume all are lowercase
    List<String> segmentedWords = new ArrayList<>(); //has to be ascii
    List<Pair<String,List<String>>> wordTags = new ArrayList<>();

    static Set<String> verb = new HashSet<String>() {{
        add("di tu");
        add("den");
        add("di den");
        add("di chuyen tu");
        add("den");
        add("toi");
        add("tu");
    }};

    static Set<String> noun = new HashSet<String>() {{
        add("xe bus");
        add("bus");
        add("xe");
        add("xe buyt");
        add("buyt");
    }};

    static Set<String> busName = new HashSet<String>() {{
        add("b1");
        add("b2");
        add("b3");
        add("b4");
    }};

    static Set<String> prep = new HashSet<String>() {{
        add("tu");
        add("den");
        add("toi");
        add("luc");
    }};

    //name
    static Set<String> cityName = new HashSet<String>() {{
        add("thanh pho hue");
        add("thanh pho da nang");
        add("thanh pho dn");
        add("thanh pho ho chi minh");
        add("thanh pho hcm");
        add("pho hue");
        add("pho da nang");
        add("pho dn");
        add("pho ho chi minh");
        add("pho hcm");
        add("tp hue");
        add("tp da nang");
        add("tp dn");
        add("tp ho chi minh");
        add("tp hcm");
        add("hue");
        add("da nang");
        add("dn");
        add("ho chi minh");
        add("hcm");
    }};

    //time: as a noun
    static Set<String> time = new HashSet<String>() {{
        add("\\d+:\\d+");
        add("\\d+:\\d+ hr");
        add("\\d+:\\d+hr");
        add("\\d+:\\d+gio");
        add("\\d+:\\d+ gio");
    }};

    private static Boolean isTime(String t){
        for(String reg : time){
            if(t.matches(reg)){
                return true;
            }
        }
        return false;
    }

    //wh_obj (asking which bus)
    static Set<String> whObj = new HashSet<String>() {{
        add("nao");
    }};

    //wh_time (for time)
    static Set<String> whTime = new HashSet<String>(){{
        add("thoi gian");
        add("thoi gian nao");
        add("tg");
        add("luc may gio");
        add("tu may gio");
        add("tu luc nao");
        add("tu khi nao");
        add("luc nao");
        add("khi nao");
        add("may gio");
        add("bao lau");
    }};

    //wh_loc
    static Set<String> whLoc = new HashSet<String>(){{
        add("dau");
        add("o dau");
        add("tai dau");
    }};

    //bus_name
    static Set<String> punc = new HashSet<String>(){{
        add("?");
        add(",");
        add(".");
    }};

    public POSTagger(List<String> s){
        segmentedWords = s;
    }

    public void getPOSTag(){
        //first priority: V over P
        for(String w : segmentedWords){
            List<String> tags = new ArrayList<>();
            if(verb.contains(w))
                tags.add("VB");
            if(noun.contains(w))
                tags.add("NSUBJ");
            if(cityName.contains(w))
                tags.add("NOBJ");
            if(prep.contains(w))
                tags.add("P");
            if(busName.contains(w))
                tags.add("BUS_NAME");
            if(isTime(w))
                tags.add("TIME");
            if(whObj.contains(w))
                tags.add("WH_OBJ");
            if(whTime.contains(w))
                tags.add("WH_TIME");
            if(whLoc.contains(w))
                tags.add("WH_LOC");
            if(punc.contains(w))
                tags.add("PUNC");
            wordTags.add(new Pair<>(w,tags));
        }
    }

    public List<Pair<String , List<String> >> getPOSList(){
        return wordTags;
    }

}
