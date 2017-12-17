import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CellSIR implements Cell {

    private int type=0;
    private ArrayList<Cell> neighboors=new ArrayList<Cell>();

    private HashMap<String,Double> parameters;
    private int nextState;
    private HashMap<String, Double> nextStateParameters;

    public CellSIR(){
        parameters=new HashMap<>();
        nextStateParameters=new HashMap<>();
        nextState=0;
        this.type=0;
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
        //System.out.println("calculating new state");

    }
    @Override
    public void changeState(){
        //change state

        this.type=nextState;
        parameters.clear();
        parameters.putAll(nextStateParameters);
    }

    @Override
    public void clear(){
        this.type=0;
        //rest of parameters //hashmap?
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void setType(int i) {
        this.type=i;
    }

    @Override
    public int getNumberOfTypes() {
        return 0;
    }
}
