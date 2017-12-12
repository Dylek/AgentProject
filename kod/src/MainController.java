/**
 * Sample Skeleton for 'CAapp.fxml' Controller Class
 */

import java.net.URL;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainController {



    @FXML
    private ChoiceBox<EpidemicModels> modelChooser;
    @FXML // fx:id="neighborhood"
    private ChoiceBox<Neighborhood> neighborhood; // Value injected by FXMLLoader

    @FXML // fx:id="sizeSpinner"
    private Spinner<Integer> sizeSpinner; // Value injected by FXMLLoader
    @FXML
    private VBox parametersSpace;
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
    // /\ it change itself

    @FXML // fx:id="chart1"
    private LineChart<?, ?> chart1; // Value injected by FXMLLoader


    private BoardCA board;
    private int cellSize =10;
    private int laneThickness=2;
    private HashMap<String,Double> parameters;

    private DecimalFormat format ;
    TextFormatter textFormatter;



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert modelChooser != null : "fx:id=\"modelChooser\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert initButton != null : "fx:id=\"initButton\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert neighborhood != null : "fx:id=\"neighborhood\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert sizeSpinner != null : "fx:id=\"sizeSpinner\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert parametersSpace != null : "fx:id=\"parametersSpace\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert boardToPaint != null : "fx:id=\"boardToPaint\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert speedSlider != null : "fx:id=\"speedSlider\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert chart1 != null : "fx:id=\"chart1\" was not injected: check your FXML file 'CAapp.fxml'.";


        parameters=new HashMap<>();
        boardToPaint.getGraphicsContext2D().setFill(Color.GRAY);
        speedSlider.setMin(0);
        speedSlider.setMax(2);
        speedSlider.setMajorTickUnit(0.25f);
        speedSlider.setSnapToTicks(true);

        modelChooser.setItems(FXCollections.observableArrayList(EpidemicModels.values()));
        modelChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setParameters(EpidemicModels.values()[newValue.intValue()]);
            }
        });
        modelChooser.setValue(EpidemicModels.SIR);
        neighborhood.setItems(FXCollections.observableArrayList(Neighborhood.values()));
        neighborhood.setValue(Neighborhood.Moore);
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,4,2));



    }

    //TODO add parameters(correct) for each model
    private void setParameters(EpidemicModels model) {
        parameters.clear();
        switch (model){
            case GAMEOFLIFE:
                parameters.put("G",0.0);
                parameters.put("3",0.0);
                parameters.put("2",0.0);
                break;
            case SIR:
                parameters.put("SIR",0.0);
                break;
            case SEIR:
                parameters.put("SEIR",0.0);
                break;
            case SIS:
                parameters.put("SIS",0.0);
                parameters.put("S2",0.0);
                break;
        }
        paintParameters();
    }

    private void paintParameters() {
        parametersSpace.getChildren().removeAll(parametersSpace.getChildren());
        for(String key: parameters.keySet()){
            parametersSpace.getChildren().add(new Label(key));
            TextField tem=new TextField();
            tem.setText(String.valueOf(parameters.get(key)));
            tem.textProperty().addListener((observable,oldValue,newValue) -> {
                if(newValue.matches("\\d+\\.\\d+")
                        || newValue.matches("\\d+")
                        || newValue.matches("\\d+\\.")  ){
                    if(newValue.endsWith(".")){
                        parameters.put(key,Double.valueOf(newValue.substring(0,newValue.length())));
                    }else {
                        parameters.put(key,Double.valueOf(newValue));
                    }

                }else{
                    tem.setText("0.0");
                }
            });
            parametersSpace.getChildren().add(tem);
        }
    }

    @FXML
    void startClicked(ActionEvent event) {
        //make sure everything is initialized
        if(!initButton.isDisable()){
            initButton.fire();
        }
        //TODO start simulation


    }
    @FXML
    void clearClicked(ActionEvent event) {
        //TODO get ready for again
    }
    @FXML
    void stopClicked(ActionEvent event) {
        //TODO stop/resume feature

    }
    @FXML
    void initClicked(ActionEvent event) {
        startSim();
        disableParameters();
    }

    void startSim(){
        board=new BoardCA(100,modelChooser.getValue());
        System.out.println("Simulation initialised for model: "+modelChooser.getValue());

        paintBoard();
        board.setNeigbourhood(neighborhood.getValue(),sizeSpinner.getValue());


    }

    private void paintBoard(){
        for( int x=0;x< board.getSize();x++){
            for(int y=0; y< board.getSize();y++){
                boardToPaint.getGraphicsContext2D().setFill(board.board[x][y].getColor());
                boardToPaint.getGraphicsContext2D().fillRect(x*cellSize,y*cellSize,cellSize-laneThickness,cellSize-laneThickness);
            }
        }
    }

    private void disableParameters(){
        initButton.setDisable(true);
        modelChooser.setDisable(true);
        parametersSpace.setDisable(true);
        neighborhood.setDisable(true);
        sizeSpinner.setEditable(false);
    }

}
