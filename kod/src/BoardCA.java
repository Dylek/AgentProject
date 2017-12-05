/**
 *
 */
public class BoardCA {

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
                board[x][y]=new Cell(1);
            }
        }
    }

}
