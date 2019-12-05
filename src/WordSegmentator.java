import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class WordSegmentator {
    //priority next segment with more words (only bigrams, trigrams that are important
    private HashMap<String, List<String>> inlineWords  = new HashMap<String, List<String>>() {{
        put("thanh", new ArrayList<String>(){{
            add("pho hue");
            add("pho da nang");
            add("pho dn");
            add("pho ho chi minh");
            add("pho hcm");
        }});
        put("pho", new ArrayList<String>(){{
            add("hue");
            add("da nang");
            add("dn");
            add("ho chi minh");
            add("hcm");
        }});

        put("ho", new ArrayList<String>(){{
            add("chi minh");
        }});
        put("da", new ArrayList<String>(){{
            add("nang");
        }});
        put("xe", new ArrayList<String>(){{
            add("bus");
            add("buyt");
        }});
        put("\\d+:\\d+", new ArrayList<String>(){{
            add("hr");
            add("gio");
        }});
        put("di", new ArrayList<String>(){{
            add("den");
            add("tu");
        }});
        put("tu", new ArrayList<String>(){{
            add("luc nao");
            add("khi nao");
            add("may gio");
        }});
        put("den", new ArrayList<String>(){{
            add("luc nao");
            add("khi nao");
            add("may gio");
        }});
        put("may", new ArrayList<String>(){{
            add("gio");
        }});
        put("thoi", new ArrayList<String>(){{
            add("gian nao");
            add("gian");
        }});
        put("luc", new ArrayList<String>(){{
            add("nao");
            add("may gio");
        }});
        put("khi", new ArrayList<String>(){{
            add("nao");
        }});
        put("tai", new ArrayList<String>(){{
            add("dau");
        }});
        put("o", new ArrayList<String>(){{
            add("dau");
        }});
        put("bao", new ArrayList<String>(){{
            add("lau");
        }});
    }};
    private List<String> asciiWords = new ArrayList<>();
    private List<String> utf8Words = new ArrayList<>();
    private List<String> asciiSegmented = new ArrayList<>();
    private List<String> utf8Segmented = new ArrayList<>();
    private String sentence;

    private static String toASCII(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String s1 = pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
        return s1;
    }

    public WordSegmentator(String s) {
        sentence = s;
    }

    public void segment() {
        String s = sentence.toLowerCase();
        String asciiStr = toASCII(s);
        asciiWords = Arrays.asList(asciiStr.split(" "));
        utf8Words = Arrays.asList(s.split(" "));
        for(int i = 0 ; i < asciiWords.size();){ //for each separate word
            List<String> nextSegment = inlineWords.get(asciiWords.get(i)); //get its next words
            if(nextSegment == null){ //null -> no followed words
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
