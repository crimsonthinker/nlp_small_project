import java.io.IOException;
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WordSegmentator {
    //priority next segment with more words (only bigrams, trigrams that are important
    private HashMap<String,List<String>> inlineWords = new HashMap<>();
    private List<String> asciiWords = new ArrayList();
    private List<String> utf8Words = new ArrayList();
    private List<String> asciiSegmented = new ArrayList();
    private List<String> utf8Segmented = new ArrayList();
    private String sentence;

    private static String toASCII(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String s1 = pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
        return s1;
    }

    public WordSegmentator(String s) throws IOException, ParseException {
        sentence = s;
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./data/word_segment.json");
        //Read JSON file
        JSONObject jsonObject= (JSONObject) jsonParser.parse(reader);
        for(Object key : jsonObject.keySet()){
            String k = (String)key;
            List<String> value = (List<String>) jsonObject.get(k);
            inlineWords.put(k,value);
        }
    }

    public void segment() {
        String s = sentence.toLowerCase();
        String asciiStr = toASCII(s);
        asciiWords = Arrays.asList(asciiStr.split(" "));
        utf8Words = Arrays.asList(s.split(" "));
        for(int i = 0 ; i < asciiWords.size();){ //for each separate word
            List<String> nextSegment = inlineWords.get(asciiWords.get(i)); //get its next words
            if(nextSegment == null) { //null -> no followed words
                if(asciiWords.get(i).matches("\\d+:\\d+")){
                    nextSegment = inlineWords.get("\\d+:\\d+");
                }
            }
            if(nextSegment == null){
                asciiSegmented.add(asciiWords.get(i));
                utf8Segmented.add(utf8Words.get(i));
                i = i + 1;
            }else{ //there is/are followed word(s)
                Boolean foundSegment = false; //found segment?
                for(String segment : nextSegment){ //for each segment (as ascii)
                    List<String> splitSegment = Arrays.asList(segment.split(" ")); //split segment into words
                    if(i + splitSegment.size() < asciiWords.size() - 1){ //check if each word is enough for this segment
                        Boolean ok = true;
                        String utf8Segment = "";
                        for(int t = 0 ; t < splitSegment.size() ; t++){
                            if(!asciiWords.get(i + t + 1).equals(splitSegment.get(t))){
                                ok = false;
                                break;
                            }else{
                                //get utf8 segment
                                utf8Segment = utf8Segment + (t != 0 ? " " : "") + utf8Words.get(i + t + 1);
                            }
                        }
                        if(ok){ //if this segment is ok
                            asciiSegmented.add(asciiWords.get(i) + " " + segment);
                            utf8Segmented.add(utf8Words.get(i) + " " + utf8Segment);
                            i = i + splitSegment.size() + 1;
                            foundSegment = true;
                            break;
                        }
                    }
                }
                if(!foundSegment){
                    asciiSegmented.add(asciiWords.get(i));
                    utf8Segmented.add(utf8Words.get(i));
                    i = i + 1;
                }
            }
        }
    }

    public List<String> getAsciiWords(){
        return asciiWords;
    }

    public List<String> getUtf8Words(){
        return utf8Words;
    }
    public List<String> getAsciiSegmented(){
        return asciiSegmented;
    }
    public List<String> getUtf8Segmented(){
        return utf8Segmented;
    }
}
