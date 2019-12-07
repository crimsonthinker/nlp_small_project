import java.io.*;
import org.json.simple.parser.ParseException;
public class Main {

    public static void main(String[] args) throws IOException, ParseException{
        String str = "Thời gian xe bus B3 từ tp Đà Nẵng đến tp Huế ?";
        //wseg
        WordSegmentator seg = new WordSegmentator(str);
        seg.segment();
        System.out.println("Word segmentation");
        System.out.println(seg.getUtf8Segmented());

        //pos
        POSTagger pos = new POSTagger(seg.getAsciiSegmented());
        pos.getPOSTag();
        System.out.println("POS tagging");
        System.out.println(pos.getPOSList());


        //dependency
        DependencyParser dp = new DependencyParser(pos.getPOSList());
        dp.parse();
        System.out.println("Dependency parsing");
        System.out.println(dp.getDependencies());

        //grammatical relation
        GrammaticalRelationGenerator grg = new GrammaticalRelationGenerator(dp.getDependencies());
        grg.generate();

        //logical form

        //procedural semantics

        //database query
    }
}
