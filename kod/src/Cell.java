import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */

public class Cell {
    public int type=0;
    public ArrayList<Cell> neighboors=new ArrayList<Cell>();

    private HashMap<String,Double> parameters;
    private int nextState;
    private HashMap<String, Double> nextStateParameters;

    public Cell(int type){
        parameters=new HashMap<>();
        nextStateParameters=new HashMap<>();
        nextState=0;
        this.type=type;
    }

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

    public void calculateNewState(){
        //callculate new state
        //System.out.println("calculating new state");

    }
    public void changeState(){
        //change state

        this.type=nextState;
        parameters.clear();
        parameters.putAll(nextStateParameters);
    }

    public void clear(){
        this.type=0;
        //rest of parameters //hashmap?
    }
}
