import Utils.*;

import java.util.ArrayList;
import java.util.List;

public class LogicalFormGenerator {
    private GRRepList grRepList;
    private SimpleLogicForm form = new SimpleLogicForm();

    int counter= 0;
    public LogicalFormGenerator(GRRepList l){
        grRepList = l;
    }

    private String getNewID(){
        String result = "x" + Integer.toString(counter);
        counter = counter + 1;
        return result;
    }

    public void generate(){
        String whatToAsk = null;
        Term aDestTerm = null;
        Term dDestTerm = null;
        Term aTimeTerm = null;
        Term dTimeTerm = null;
        Term busNameTerm = null;
        Term from = grRepList.getTermFromWordSense("TU"); //get predicate
        if(from != null){
            for(int i = 0 ; i < grRepList.getRelationList().size() ; i++){
                TripletProposition tp = grRepList.getRelationList().get(i);
                if(tp.getTerm1().equals(from.getVar())) {
                    if(tp.getPred().equals("LSUBJ")){ //
                        if(tp.getTerm2().equals("BUS_NAME")){ //unknown bus
                            Term t = grRepList.getTermFromWordSense("BUS_NAME");
                            if(t != null){
                                busNameTerm = t;
                            }
                        }
                    }else if(tp.getTerm2().equals("WH_OBJ")) {
                        whatToAsk = "WH_OBJ";
                    }else if(tp.getTerm2().equals("WH_TIME")) {
                        whatToAsk = "WH_TIME";
                    }else if(tp.getTerm2().equals("WH_LOC")){
                        whatToAsk = "DDEST";
                    }else if(tp.getPred().equals("LOBJ")){
                        if(tp.getTerm2().equals("AT")){ //dtime
                            Term t = grRepList.getTermFromWordSense("TIME");
                            if(t != null){
                                dTimeTerm = t;
                            }
                        }else if(tp.getTerm2().equals("HUE") ||
                                tp.getTerm2().equals("DANANG") ||
                                tp.getTerm2().equals("HCMC")){ //city
                            Term t = grRepList.getTermFromWordSense(tp.getTerm2());
                            if(t != null){
                                dDestTerm = t;
                            }
                        }
                    }
                }
            }
        }
        Term to = grRepList.getTermFromWordSense("DEN"); //get predicate
        if(to != null){
            for(int i = 0 ; i < grRepList.getRelationList().size() ; i++){
                TripletProposition tp = grRepList.getRelationList().get(i);
                if(tp.getTerm1().equals(to.getVar())) {
                    if(tp.getPred().equals("LSUBJ")){ //
                        if(tp.getTerm2().equals("BUS_NAME")){ //unknown bus
                            Term t = grRepList.getTermFromWordSense("BUS_NAME");
                            if(t != null){
                                busNameTerm = t;
                            }
                        }
                    }else if(tp.getTerm2().equals("WH_OBJ")) {
                        whatToAsk = "WH_OBJ";
                    }else if(tp.getTerm2().equals("WH_TIME")) {
                        whatToAsk = "WH_TIME";
                    }else if(tp.getTerm2().equals("WH_LOC")){
                        whatToAsk = "ADEST";
                    }else if(tp.getPred().equals("LOBJ")){
                        if(tp.getTerm2().equals("AT")){ //dtime
                            Term t = grRepList.getTermFromWordSense("TIME");
                            if(t != null){
                                aTimeTerm = t;
                            }
                        }else if(tp.getTerm2().equals("HUE") ||
                                tp.getTerm2().equals("DANANG") ||
                                tp.getTerm2().equals("HCMC")){ //city
                            Term t = grRepList.getTermFromWordSense(tp.getTerm2());
                            if(t != null){
                                aDestTerm = t;
                            }
                        }
                    }
                }
            }
        }

        form.declareWhatToAsk(whatToAsk);

        counter = 0;
        if(busNameTerm != null){
            form.add(new LogicUnit("BUS",busNameTerm));
        }
        if(aDestTerm != null){
            form.add(new LogicUnit("ADEST",aDestTerm));
        }
        if(aTimeTerm != null){
            form.add(new LogicUnit("ATIME",aTimeTerm));
        }
        if(dDestTerm != null){
            form.add(new LogicUnit("DDEST",dDestTerm));
        }
        if(dTimeTerm != null){
            form.add(new LogicUnit("DTIME",dTimeTerm));
        }
        //understanding WH_TIME:
        //asking for ARRIVE_TIME, DESTINATION_TIME, or DURATION
        //asking for arrive time
        if(whatToAsk.equals("WH_TIME") && ((dTimeTerm != null && aTimeTerm == null ) ||
                (aTimeTerm == null && dTimeTerm == null))){
            String arriveID = getNewID();
            form.add(new LogicUnit("ATIME",arriveID));
            form.addID(arriveID);
        }

        if(whatToAsk.equals("WH_TIME") & ((dTimeTerm == null && aTimeTerm != null) ||
                (aTimeTerm == null && dTimeTerm == null))){
            //asking for destination time
            String destID = getNewID();
            form.add(new LogicUnit("DTIME",destID));
            form.addID(destID);
        }

        if(aTimeTerm == null && dTimeTerm == null && whatToAsk.equals("WH_TIME")){
            //asking for duration
            String durID = getNewID();
            form.add(new LogicUnit("RUN-TIME",durID));
            form.addID(durID);
        }

        if(whatToAsk.equals("WH_OBJ")){
            String objID = getNewID();
            form.add(new LogicUnit("WH_OBJ",objID));
            form.addID(objID);
        }

        if(whatToAsk.equals("ADEST")){
            String locID = getNewID();
            form.add(new LogicUnit("ADEST",locID));
            form.addID(locID);
        }

        if(whatToAsk.equals("DDEST")){
            String locID = getNewID();
            form.add(new LogicUnit("DDEST",locID));
            form.addID(locID);
        }
    }

    public SimpleLogicForm getLogicalForm(){
        return form;
    }

}
