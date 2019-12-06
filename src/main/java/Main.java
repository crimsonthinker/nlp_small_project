import java.io.*;
import org.json.simple.parser.ParseException;
public class Main {

    public static void main(String[] args) throws IOException, ParseException{
        String str = "Thời gian xe bus B3 từ Đà Nẵng đến Huế ?";
        //wseg
        WordSegmentator seg = new WordSegmentator(str);
        seg.segment();
        System.out.println(seg.getUtf8Segmented());

        //pos
        POSTagger pos = new POSTagger(seg.getAsciiSegmented());
        pos.getPOSTag();

        //dependency
        DependencyParser dp = new DependencyParser(pos.getPOSList());
        dp.parse();
        //grammatical relation

        //logical form

        //procedural semantics

        //database query
    }
}
