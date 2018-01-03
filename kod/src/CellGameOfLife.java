import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CellGameOfLife implements Cell {


    private int type=0;
    private ArrayList<Cell> neighboors=new ArrayList<Cell>();

    private static HashMap<String,Double> parameters;
    private int nextState;
   // private HashMap<String, Double> nextStateParameters;


    public CellGameOfLife(){
        parameters=new HashMap<>();
      //  nextStateParameters=new HashMap<>();
        nextState=0;
        this.type=0;
    }
    public static void setParameters(HashMap<String,Double> par){
        parameters.putAll(par);
    }

    @Override
    public void addNeigbour(Cell c) {
        neighboors.add(c);
    }

    @Override
    public Paint getColor() {
        switch (this.type){
            case 0: return Color.GRAY;
            case 1: return Color.BLACK;
            case 2: return Color.GREEN;
            case 3: return Color.RED;
            case 4: return Color.BLUE;
            case 5: return Color.YELLOW;
        }
        return Color.WHITE;
    }

    @Override
    public void calculateNewState(){
        //callculate new state

        int al=getAliveN();


        // 23/3
        //"stay alive from" "stay alive to" /"become alive from" "become alive to"

        if(this.type==1){
            if(parameters.get("stay alive from")<= al && al<=parameters.get("stay alive to")){
                nextState=1;
            }else{
                nextState=0;
            }
        }else{

            if(parameters.get("become alive from")<= al && al<=parameters.get("become alive to")){
                nextState=1;
            }else{
                nextState=0;
            }
        }

    }
    @Override
    public void changeState(){
        //change state

        this.type=nextState;
        //parameters.clear();
        //parameters.putAll(nextStateParameters);
    }

    @Override
    public void clear(){
        this.type=0;
        parameters.clear();
        //rest of parameters //hashmap?
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int i) {
        this.type=i;
    }

    @Override
    public int getNumberOfTypes() {
        return 0;
    }

    public static ArrayList<String> getParametersTypes(){
        ArrayList<String> pars=new ArrayList<>();
        pars.add("stay alive from");
        pars.add("stay alive to");
        pars.add("become alive from");
        pars.add("become alive to");
        return pars;
    }

    private int getAliveN(){
        int alive=0;
        alive=neighboors.stream().mapToInt(i->i.getType()).sum();
        return alive;
    }
}
