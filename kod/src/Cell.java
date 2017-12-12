import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 *
 */

public class Cell {
    public int type=0;
    public ArrayList<Cell> neighboors=new ArrayList<Cell>();

    public Cell(int type){
        this.type=type;
    }

    public Paint getColor() {
        switch (this.type){
            case 0: return Color.GRAY;
            case 1: return Color.BLACK;
        }
        return Color.WHITE;
    }
}
