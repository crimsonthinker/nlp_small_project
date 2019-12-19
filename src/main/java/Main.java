import java.io.*;

import Utils.Query;
import org.json.simple.parser.ParseException;
public class Main {

    public static void main(String[] args) throws IOException, ParseException{
        //String str = "Xe bus nào đi từ thành phố Đà Nẵng lúc 16:00 HR ?";
        String str = "Thời gian xe bus B3 từ Đà Nẵng đến Huế ?";
        //String str = "Xe bus nào đến thành phố Hồ Chí Minh ?"; ok
        //String str = "Khi nào xe bus b3 tới Huế ?";
        System.out.println("\nOriginal question");
        System.out.println(str);


        //wseg
        WordSegmentator seg = new WordSegmentator(str);
        seg.segment();
        System.out.println("\nWord segmentation");
        System.out.println(seg.getUtf8Segmented());

        //pos
        POSTagger pos = new POSTagger(seg.getAsciiSegmented());
        pos.getPOSTag();
        System.out.println("\nPOS tagging");
        System.out.println(pos.getPOSList());


        //dependency
        DependencyParser dp = new DependencyParser(pos.getPOSList());
        dp.parse();
        System.out.println("\nDependency parsing");
        System.out.println(dp.getDependencies());

        //grammatical relation
        GrammaticalRelationGenerator grg = new GrammaticalRelationGenerator(dp.getDependencies());
        grg.generate();
        System.out.println("\nGrammatical relation");
        System.out.println(grg.getGrammaticalRelation().toString());

        //logical form
        LogicalFormGenerator lfg = new LogicalFormGenerator(grg.getGrammaticalRelation());
        lfg.generate();
        System.out.println("\nLogical formulation");
        System.out.println(lfg.getLogicalForm());


        //procedural semantics
        ProceduralSemanticsGenerator psg = new ProceduralSemanticsGenerator(lfg.getLogicalForm());
        psg.generate();
        System.out.println("\nProcedural semantic");
        System.out.println(psg.getQuery());


        //database query
        System.out.println("\nFinal anwser");
        System.out.println(psg.getAnswer());
    }
}
