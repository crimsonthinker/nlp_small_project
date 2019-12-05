

public class GrammaticalRelationGenerator {
    /*
    //private variables
    private Annotation ann;
    //declare word types of dependency list
    private HashMap<String,String> wordTag = new HashMap<>();
    private HashMap<String, Pair<String,String>> deps = new HashMap<>();

    //for storing grammatical relation
    private class Triplet{
        private String var; //biến
        private String pattern; //nội dung
        private String value; //giá trị
        public Triplet(String vr, String p, String vl){
            var = vr;
            pattern = p;
            value = vl;
        }

    }

    ArrayList<Triplet> nerList = new ArrayList<>();
    ArrayList<Triplet> relationList = new ArrayList<>();
    //constructors
    public GrammaticalRelationGenerator(String s){
        ann = new Annotation(s);
    }

    private String getID(String value){
        for(Triplet t : nerList){
            if(t.value.equals(value)){
                return t.var;
            }
        }
        return "null";
    }

    private void wordProcessingAndDependencies(){
        //get words
        List<Word>words = ann.getWords();

        //modifying word segmentation, POS tagging and dependencies
        for(Word word : words){
            System.out.println(word.toString());
            wordTag.put(word.getForm(),word.getPosTag());
            Integer headIndex = word.getHead() - 1;
            String headForm = (headIndex == -1) ? "root" : words.get(headIndex).getForm();
            deps.put(word.getDepLabel(),new Pair(headForm,word.getForm()));
            //System.out.println(word.getDepLabel() + " " + headForm + " " + word.getForm());
        }
    }

    private void getEntity() throws IOException{
        Integer id = 0;
        for(Map.Entry<String,String> w : wordTag.entrySet()){
            String form = w.getKey();
            String tag = w.getValue();
            Entity en = new NameEntityRecognition(form,tag).getEntity();
            if(en != Entity.NULL){ //not null or it is a noun or it is a verb
                //check if entity existed
                Boolean existed = false;
                for(Integer i = 0 ; i < nerList.size() ; i++){
                    Triplet tmp = nerList.get(i);
                    Boolean isMatchedPattern = tmp.pattern == en.toString();
                    Boolean isMatchedWord = tmp.value == form;
                    if(isMatchedPattern && isMatchedWord){
                        existed = true;
                        break;
                    }
                }
                if(!existed){
                    nerList.add(new Triplet(
                            "id" + Integer.toString(id),en.toString(),form));
                    if(tag.matches("V\\w*")){ //its a verb -> add TNS
                        //vietnamese does not have past tense, or it is not necessary in this work
                        nerList.add(new Triplet(
                                "id" + Integer.toString(id),Relation.TNS.toString(),"PRES"));
                    }
                    id = id + 1;
                }
            }
        }
    }

    private void getRelation(){
        //for each relation
        for(Map.Entry<String, Pair<String,String>> d : deps.entrySet()) {
            String relation = d.getKey();
            String first = d.getValue().getKey();
            String second = d.getValue().getValue();
            //check cases
            //get var
            String var = this.getID(first);
            if(var.equals("null")){
                System.out.println("word " + first + " is not having an entity");
                continue;
            }
            String value = this.getID(second);
            if(value.equals("null")){
                System.out.println("word " + second + " is not having an entity");
                continue;
            }
            if(relation.equals(Dependencies.SUB.toString())){
                relationList.add(new Triplet(var,Relation.LSUBJ.toString(),value));
            }
            if(relation.equals(Dependencies.POB.toString())){
                relationList.add(new Triplet(var,Relation.LOBJ.toString(),value));
            }
            if(relation.equals(Dependencies.VMOD.toString())){
                relationList.add(new Triplet(var,Relation.VMOD.toString(),value));
            }
            if(relation.equals(Dependencies.NMOD.toString())){
                relationList.add(new Triplet(var, Relation.NMOD.toString(),value));
            }

        }
    }

    //
    public void generate() throws IOException{
        //wseg: correctly segmented
        //ner: name entity recognition: does not need, will create on our own
        //pos: part-of-speech tagging
        //parse: name of relation
        VnCoreNLP p = new VnCoreNLP(new String[]{"wseg","pos","parse"});
        p.annotate(ann);

        //1 get POS tagging and dependencies
        this.wordProcessingAndDependencies();

        //get entity
        this.getEntity();

        System.out.println(nerList.size());
        for(int i = 0 ; i < nerList.size() ; i++){
            Triplet t = nerList.get(i);
            System.out.println(t.var + " " + t.pattern + " " + t.value);
        }
    }
    */
}