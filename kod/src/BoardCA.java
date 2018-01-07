

import javafx.scene.canvas.Canvas;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 *
 */
public class BoardCA {
    private XYChart.Series[] series;
    private Canvas boardToPaint;
    private int cellSize =10;
    private int laneThickness=2;
    public Cell[][] board;
    private int size;
    public int getSize(){
        return size;
    }
    public BoardCA(int boardSize,EpidemicModels model){
        size=boardSize;
        board=new Cell[size][size];
        for(int x=0;x<size;x++){
            for(int y=0;y<size;y++){
                board[x][y]= CellFactory.getCell(model,x,y);
            }
        }
        series=new XYChart.Series[board[0][0].getNumberOfTypes()];
        for(int i=0;i<board[0][0].getNumberOfTypes();i++){
            series[i]=new XYChart.Series();
            series[i].setName("Type "+i);
        }

    }

    private ArrayList<Integer> getIndexesRange(int i,int size,int end){
        ArrayList<Integer> result=new ArrayList<>();
        for(int k=i-size;k<=i+size;k++){
            if(k>=0 && k<end){
                result.add(k);
            }
        }
        return result;
    }

    public void setNeigbourhood(Neighborhood n,int size){
        switch (n){
            case Moore:
                for(int x=0;x<board.length;x++){
                    for(int y=0;y<board.length;y++){
                        for(int tempX:getIndexesRange(x,size,board.length)){
                            for(int tempY:getIndexesRange(y,size,board.length)){
                               if(tempY==y && tempX==x)continue;
                                if(Math.abs(tempX-x)<=size && Math.abs(tempY-y)<=size){
                                    board[x][y].addNeigbour(board[tempX][tempY]);
                                }
                            }
                        }
                    }
                }
                break;
            case VonNeman:
                for(int x=0;x<board.length;x++){
                    for(int y=0;y<board.length;y++){
                        for(int tempX:getIndexesRange(x,size,board.length)){
                            for(int tempY:getIndexesRange(y,size,board.length)){
                                if(tempY==y && tempX==x)continue;
                                if(Math.abs(tempX-x) + Math.abs(tempY-y)<=size){
                                    board[x][y].addNeigbour(board[tempX][tempY]);
                                }
                            }
                        }
                    }
                }
                break;
        }
        System.out.println("Neoghboorhood has been set");
    }


    public void iteration(int iteration){
        //System.out.println("iteratrion new");
        Double[] data=new Double[board[0][0].getNumberOfTypes()];
        for(int i=0;i<board[0][0].getNumberOfTypes();i++){
            data[i]=0.0;
        }
        for(Cell[] arr:board){
            for(Cell cell:arr){
                cell.calculateNewState();
                data[cell.getType()]+=1;
            }
        }
        for(int i=0;i<board[0][0].getNumberOfTypes();i++){
            series[i].getData().add(new XYChart.Data<>(iteration,data[i]));
        }


        for(Cell[] arr:board){
            for(Cell cell:arr){
                cell.changeState();
            }
        }



        paintBoard();
      //  Arrays.stream(board).map(x-> Arrays.stream(x).map(y->y.calculateNewState()));
    }

    public void clear(){

    }

    public void paintBoard(){
        for( int x=0;x< size;x++){
            for(int y=0; y< size;y++){
                boardToPaint.getGraphicsContext2D().setFill(this.board[x][y].getColor());
                boardToPaint.getGraphicsContext2D().fillRect(x*cellSize,y*cellSize,cellSize-laneThickness,cellSize-laneThickness);
            }
        }
    }

    public void setDrawingProperties(Canvas boardToPaint, int cellSize, int laneThickness) {
        this.boardToPaint=boardToPaint;
        this.cellSize=cellSize;
        this.laneThickness=laneThickness;
    }

    public void setParameters(HashMap<String,Double> par,EpidemicModels model){
        System.out.println(par);
        if(model.equals(EpidemicModels.GAMEOFLIFE)){
            CellGameOfLife.setParameters(par);
            //for(int x=0;x<board.length;x++){
            //    for(int y=0;y<board.length;y++){
           //         board[x][y].setParameters(par);
          //      }
          //  }
        }
        if(model.equals(EpidemicModels.SIR)){
            CellSIR.setConstantParameters(par);
        }

    }

    public static Paint getCellColor(int type) {
        switch (type){
            case 0: return Color.GRAY;
            case 1: return Color.BLACK;
            case 2: return Color.GREEN;
            case 3: return Color.RED;
            case 4: return Color.BLUE;
            case 5: return Color.YELLOW;
        }
        return Color.WHITE;
    }

    public XYChart.Series[] getDataForCharts(){
        return series;
    }
}
