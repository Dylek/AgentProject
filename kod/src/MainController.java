/**
 * Sample Skeleton for 'CAapp.fxml' Controller Class
 */

import java.net.URL;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MainController {

    @FXML
    private ChoiceBox<EpidemicModels> modelChooser;
    @FXML // fx:id="neighborhood"
    private ChoiceBox<Neighborhood> neighborhood; // Value injected by FXMLLoader
    @FXML // fx:id="sizeSpinner"
    private Spinner<Integer> sizeSpinner; // Value injected by FXMLLoader
    @FXML
    private Spinner<Integer> typeChange;
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
    private Slider speedSlider; // Value injected by FXMLLoader    // /\ it change itself
    @FXML // fx:id="chart1"
    private LineChart<?, ?> chart1; // Value injected by FXMLLoader

    private BoardCA board;
    private int cellSize =10;
    private int laneThickness=2;
    private HashMap<String,Double> parameters;
    private int iterationNumber=0;
    private Timeline timeline;


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
        assert typeChange != null : "fx:id=\"typeChange\" was not injected: check your FXML file 'CAapp.fxml'.";
        assert chart1 != null : "fx:id=\"chart1\" was not injected: check your FXML file 'CAapp.fxml'.";


        initUI();
        disableParameters(true);
    }

    @FXML
    void clearClicked(ActionEvent event) {
        if(timeline!=null){
            timeline.stop();
            timeline=null;
        }
        prepareCanvas();
        disableParameters(true);
    }

    @FXML
    void mouseClickedCanvas(MouseEvent event) {
        int x = (int) event.getX()/cellSize;
        int y = (int) event.getY()/cellSize;

        if((x<board.getSize()) && (x>0) && (y>0) && (y<board.getSize())){
            board.board[x][y].setType(typeChange.getValue());

            paintBoard();
        }
    }

    @FXML
    void stopClicked(ActionEvent event) {

        timeline.stop();

    }

    @FXML
    void initClicked(ActionEvent event) {
        startSim();
        disableParameters(false);
    }


    @FXML
    void startClicked(ActionEvent event) {

        updateSimulationSpeed();
    }

    private void updateSimulationSpeed() {
        if(timeline!=null){
            timeline.stop();
            timeline=null;
        }
        timeline=new Timeline(new KeyFrame(getIterationDelay(),a->doIteration()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    //TODO add parameters(correct) for each model
    private void setParameters(EpidemicModels model) {
        parameters.clear();
        switch (model){
            case GAMEOFLIFE:
                parameters.put("Live",0.0);
                parameters.put("DIe",0.0);
                break;
            case SIR:
                parameters.put("S",0.0);
                parameters.put("I",0.0);
                parameters.put("R",0.0);
                break;
            case SEIR:
                parameters.put("S",0.0);
                parameters.put("E",0.0);
                parameters.put("I",0.0);
                parameters.put("R",0.0);
                break;
            case SIS:
                parameters.put("S",0.0);
                parameters.put("I",0.0);
                break;
        }
        paintParameters();
    }

    private void prepareCanvas() {
        boardToPaint.getGraphicsContext2D().setFill(Color.WHITE);
        boardToPaint.getGraphicsContext2D().fillRect(0,0,boardToPaint.getWidth(),boardToPaint.getHeight());
    }
    void startSim(){
        board=new BoardCA(100,modelChooser.getValue());
        board.setDrawingProperties(boardToPaint,cellSize,laneThickness);

        initTypeChange();
        paintBoard();
        board.setNeigbourhood(neighborhood.getValue(),sizeSpinner.getValue());

        System.out.println("Simulation initialised for model: "+modelChooser.getValue());
    }

    void initTypeChange(){
        int maxTypes=0;
        switch (modelChooser.getValue()){
            case GAMEOFLIFE: maxTypes=1;break; //0-dead cell 1-alive cell
            case SIR: maxTypes=3; break; //0-empty,1-S,2-I,3-R
            case SIS: maxTypes=2; break; //0-empty,1-S,2-I
            case SEIR: maxTypes=4; break; //0-empty,1-S,2-E,3-I,4-R
        }

        typeChange.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,maxTypes,0));
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

    private void paintBoard(){
        board.paintBoard();
    }

    private void disableParameters(boolean status){
        initButton.setDisable(!status);
        modelChooser.setDisable(!status);
        parametersSpace.setDisable(!status);
        neighborhood.setDisable(!status);
        sizeSpinner.setDisable(!status);
        typeChange.setDisable(status);
        stopButton.setDisable(status);
        clearButton.setDisable(status);
        speedSlider.setDisable(status);
        startButton.setDisable(status);
    }



    private void doIteration(){
        iterationNumber+=1;
        board.iteration();
        System.out.println("Iteration number:"+iterationNumber);
    }

    private void paintCharts(){
        //TODO painting charts
    }
    private void initUI() {
        parameters=new HashMap<>();
        prepareCanvas();
        speedSlider.setMin(0.5);
        speedSlider.setMax(2);
        speedSlider.setMajorTickUnit(0.2f);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setBlockIncrement(0.1f);
        speedSlider.setValue(1);
        speedSlider.setSnapToTicks(true);
        speedSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println(speedSlider.getValue());
                if(timeline!=null){
                    updateSimulationSpeed();
                }
            }
        });

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

    public Duration getIterationDelay() {
        //return Duration.seconds(1f);
        return Duration.seconds(1f / speedSlider.getValue());
    }
}
