import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CellSIR implements Cell {
    //0-empty,1-S,2-I,3-R
    //0-empty space, like ocean, not inhabitated land
    private int type=0;
    private ArrayList<CellSIR> neighboors=new ArrayList<CellSIR>();
    public int cellPopulation=100;//constant//cot the same for every cell
    //it holds parameters like specific of One cell
    private HashMap<String,Double> parameters;
    private int nextState=0;
    private HashMap<String, Double> nextStateParameters;

    //It holds parameters like vacination rate; things constant for whole lattice
    private static HashMap<String,Double> constantParameters;
    public CellSIR(){
        parameters=new HashMap<>();
        nextStateParameters=new HashMap<>();
        constantParameters=new HashMap<>();
        //nextState=0;
        this.type=0;
        parameters.put("infected",0.0);
        parameters.put("suspectible",0.0);
        parameters.put("recovered",0.0);
    }

    @Override
    public void addNeigbour(Cell c) {
        neighboors.add((CellSIR)c);
    }

    @Override
    public Paint getColor() {
        switch (this.type){
            case 0: return Color.GRAY;
            case 1: return Color.BLACK;
            case 2: return Color.GREEN;
            case 3: return Color.RED;
            case 4: return Color.BLUE;
            case 5: return Color.YELLOW;
        }
        return Color.WHITE;
    }

    private double getBigSum(){
        double bigSum=0;

        for(CellSIR cell: neighboors){
            bigSum+=cell.cellPopulation/this.cellPopulation*cell.getMovementNumber() * cell.parameters.get("infected");
        }
        //this.cellPopulation/this.cellPopulation*cell.getMovementNumber() * cell.parameters.get("infected");
        bigSum+=1*this.getMovementNumber() * this.parameters.get("infected");
        return bigSum;
    }
    public double getMovementNumber(){
        double ni=0;
        ni=constantParameters.get("connection factor")*constantParameters.get("movement factor")*constantParameters.get("virulence of the epidemic");
        //ni=this.parameters.get("connection factor")*this.parameters.get("movement factor")*constantParameters.get("virulence of the epidemic");

        return ni;
    }
    @Override
    public void calculateNewState(){
        if(type!=0) {
            //callculate new state
            //System.out.println("calculating new state");
            double suspectible = 0;
            double infected = 0;
            double recovered = 0;


            infected = (1 - constantParameters.get("recovery rate")) * parameters.get("infected") +
                    +constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    parameters.get("suspectible") * this.getBigSum();

            suspectible = parameters.get("suspectible") - constantParameters.get("vaccination rate") * parameters.get("suspectible") +
                    -constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    -parameters.get("suspectible") * this.getBigSum();

            recovered = parameters.get("recovered") +
                    constantParameters.get("recovery rate") * parameters.get("infected") +
                    constantParameters.get("vaccination rate") * parameters.get("suspectible");


            //TODO FACT CHECK IT
            //cell change type if one type of inhabitans has majory in population
            if (infected > suspectible && infected > recovered) {
                nextState = 2;//infected
            }else
            if (suspectible > infected && suspectible > recovered) {
                nextState = 1;//suspectible
            }else
            if (recovered > suspectible && recovered > infected) {
                nextState = 3;//recovered
            }else{
                nextState=this.type;//no change
            }

            nextStateParameters.put("suspectible", suspectible);
            nextStateParameters.put("infected", infected);
            nextStateParameters.put("recovered", recovered);
        }
    }
    @Override
    public void changeState(){
        //change state
        if(type!=0){
            this.type=nextState;
            parameters.clear();
            parameters.putAll(nextStateParameters);

        }

    }

    @Override
    public void clear(){
        this.type=0;
        //rest of parameters //hashmap?
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int i,HashMap<String,Double> par) {

        switch (i){
            case 0:
                parameters.put("infected",0.0);
                parameters.put("suspectible",0.0);
                parameters.put("recovered",0.0);
                break;
            case 1:
                parameters.put("infected",0.0);
                parameters.put("suspectible",1.0);
                parameters.put("recovered",0.0);
                break;
            case 2:
                parameters.put("infected",1.0);
                parameters.put("suspectible",0.0);
                parameters.put("recovered",0.0);
                break;
            case 3:
                parameters.put("infected",0.0);
                parameters.put("suspectible",0.0);
                parameters.put("recovered",1.0);
                break;
            default:
        }
        this.setParameters(par);
        this.type=i;
    }

    @Override
    public int getNumberOfTypes() {
        return 4;
    }

    //@Override
    //set parameters local for a cell
    private void setParameters(HashMap<String, Double> par) {
        constantParameters.putAll(par);
        if(par.get("movement factor")>1 || par.get("movement factor")<0){
            constantParameters.put("movement factor",0.5);
        }
        if(par.get("connection factor")>1 || par.get("connection factor")<0){
            constantParameters.put("connection factor",0.4);//TODO add snap to 0,0.4,0.6,1
        }
        if(par.get("cell population")<1){
            cellPopulation=10;
        }else{
            cellPopulation=par.get("cell population").intValue();
        }


    }
   // @Override
    //set parameters from global configuration
    public static void setConstantParameters(HashMap<String, Double> par) {

        constantParameters.putAll(par);
        //make sure parameters are in constraints
        if(par.get("virulence of the epidemic")>1 || par.get("virulence of the epidemic")<0){
            constantParameters.put("virulence of the epidemic",0.0);
        }
        if(par.get("vaccination rate")>1 || par.get("vaccination rate")<0){
            constantParameters.put("vaccination rate",0.2);
        }
        if(par.get("recovery rate")>1 || par.get("recovery rate")<0){
            constantParameters.put("recovery rate",0.3);
        }
        //double c=par.get("connection factor");//snap to
    }

    public static ArrayList<String> getParametersType(){
        ArrayList<String>par=new ArrayList<>();
        par.add("virulence of the epidemic");
        par.add("vaccination rate");
       // par.add("connection factor");//snap //supose to be chosen for each cell
        //par.add("movement factor");//supose to be chosen for each cell
        par.add("recovery rate");

        return par;
    }

    public static ArrayList<String> getCellParametersType() {
        ArrayList<String>par=new ArrayList<>();
        par.add("cell population");
        par.add("connection factor");//snap //supose to be chosen for each cell
        par.add("movement factor");//supose to be chosen for each cell
        return par;
    }
}
