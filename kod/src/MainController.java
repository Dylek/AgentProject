/**
 * Sample Skeleton for 'CAapp.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class MainController {

    private BoardCA board;
    private int cellSize =10;
    private int laneThickness=2;
    @FXML // fx:id="initButton"
    private Button initButton; // Value injected by FXMLLoader
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boardToPaint"
    private Canvas boardToPaint; // Value injected by FXMLLoader


    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="startButton"
    private Button startButton; // Value injected by FXMLLoader

    @FXML // fx:id="stopButton"
    private Button stopButton; // Value injected by FXMLLoader

    @FXML // fx:id="speedSlider"
    private Slider speedSlider; // Value injected by FXMLLoader
    @FXML // fx:id="chart1"
    private LineChart<?, ?> chart1; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boardToPaint != null : "fx:id=\"boardToPaint\" was not injected: check your FXML file 'CAapp.fxml'.";
        boardToPaint.getGraphicsContext2D().setFill(Color.GRAY);




    }

    @FXML
    void startClicked(ActionEvent event) {

    }
    @FXML
    void clearClicked(ActionEvent event) {

    }
    @FXML
    void stopClicked(ActionEvent event) {

    }
    @FXML
    void initClicked(ActionEvent event) {
        startSim();
        initButton.setDisable(true);
    }

    void startSim(){
        board=new BoardCA(100,EpidemicModels.GAMEOFLIFE);
        System.out.println("SDASD");
        for( int x=0;x< board.getSize();x++){
            for(int y=0; y< board.getSize();y++){
                boardToPaint.getGraphicsContext2D().setFill(board.board[x][y].getColor());
                boardToPaint.getGraphicsContext2D().fillRect(x*cellSize,y*cellSize,cellSize-laneThickness,cellSize-laneThickness);
            }
        }
    }
}
