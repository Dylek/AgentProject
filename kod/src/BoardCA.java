

import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 *
 */
public class BoardCA {
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
                board[x][y]= CellFactory.getCell(model);
            }
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


    public void iteration(){
        System.out.println("iteratrion new");
        for(Cell[] arr:board){
            for(Cell cell:arr){
                cell.calculateNewState();
            }
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
}
