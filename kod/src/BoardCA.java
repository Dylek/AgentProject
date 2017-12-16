

import javafx.scene.canvas.Canvas;

import java.util.ArrayList;

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
                board[x][y]=new Cell(0);
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
                                    board[x][y].neighboors.add(board[tempX][tempY]);
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
                                    board[x][y].neighboors.add(board[tempX][tempY]);
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
}
