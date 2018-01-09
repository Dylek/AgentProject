import javafx.scene.paint.Paint;
import java.util.HashMap;

public interface Cell {
    void addNeigbour(Cell c);
    Paint getColor();
    void calculateNewState();
    void changeState();
    void clear();
    int getType();
    void setType(int i,HashMap<String,Double> par);
    int getNumberOfTypes();
}
