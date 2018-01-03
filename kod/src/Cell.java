import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */

public interface Cell {
    /*
    int type=0;
    ArrayList<Cell> neighboors=new ArrayList<Cell>();
    HashMap<String,Double> parameters=new HashMap<String,Double> ();
    int nextState=0;
    HashMap<String, Double> nextStateParameters=new HashMap<String, Double>();
*/
    void addNeigbour(Cell c);

    Paint getColor();
    void calculateNewState();
    void changeState();
    void clear();

    int getType();
    void setType(int i);
    int getNumberOfTypes();
    void setParameters(HashMap<String,Double> par);
}
