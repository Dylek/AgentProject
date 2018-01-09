import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CellSIS implements Cell {
    //0-empty,1-S,2-I
    //0-empty space, like ocean, not inhabitated land
    private int type=0;
    private ArrayList<CellSIS> neighboors=new ArrayList<CellSIS>();
    public int cellPopulation=100;//constant//cot the same for every cell
    //it holds parameters like specific of One cell
    private HashMap<String,Double> parameters;
    private int nextState=0;
    private HashMap<String, Double> nextStateParameters;

    //It holds parameters like vacination rate; things constant for whole lattice
    private static HashMap<String,Double> constantParameters;
    public CellSIS(){
        parameters=new HashMap<>();
        nextStateParameters=new HashMap<>();
        constantParameters=new HashMap<>();
        //nextState=0;
        this.type=0;
        parameters.put("infected",0.0);
        parameters.put("suspectible",0.0);
    }

    @Override
    public void addNeigbour(Cell c) {
        neighboors.add((CellSIS)c);
    }

    @Override
    public Paint getColor() {
        return BoardCA.getCellColor(this.type);
    }

    private double getBigSum(){
        double bigSum=0;

        for(CellSIS cell: neighboors){
            bigSum+=cell.cellPopulation/this.cellPopulation*cell.getMovementNumber() * cell.parameters.get("infected");
        }

        return bigSum;
    }
    public double getMovementNumber(){
        double ni=0;
        ni=constantParameters.get("connection factor")*constantParameters.get("movement factor")*constantParameters.get("virulence of the epidemic");
        return ni;
    }
    @Override
    public void calculateNewState(){
        if(type!=0) {
            //callculate new state

            double suspectible = 0;
            double infected = 0;


            //TODO CHECK EQUATIONS
            infected = (1 - constantParameters.get("recovery rate")) * parameters.get("infected") +
                    +constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    +parameters.get("suspectible") * this.getBigSum()
                    -constantParameters.get("recovery rate") * parameters.get("infected");

            suspectible = parameters.get("suspectible")  +
                    -constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    -parameters.get("suspectible") * this.getBigSum()
                    +constantParameters.get("recovery rate") * parameters.get("infected");


            if (infected > suspectible) {
                nextState = 2;//infected
            }else
            if (suspectible > infected) {
                nextState = 1;//suspectible
            }else{
                nextState=this.type;//no change
            }

            nextStateParameters.put("suspectible", suspectible);
            nextStateParameters.put("infected", infected);
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
                break;
            case 1:
                parameters.put("infected",0.0);
                parameters.put("suspectible",1.0);
                break;
            case 2:
                parameters.put("infected",1.0);
                parameters.put("suspectible",0.0);
                break;
            default:
        }
        this.setParameters(par);
        this.type=i;
    }

    @Override
    public int getNumberOfTypes() {
        return 3;
    }

    //@Override
    //set parameters local for a cell
    private void setParameters(HashMap<String, Double> par) {
        constantParameters.putAll(par);

//        snap to 0,0.4,0.6,1
        double[] snapV={0.0,0.3,0.6,1.0};
        int ind=0;
        double minD=Math.abs(par.get("connection factor")-snapV[0]);
        for(int d=1;d<snapV.length;d++){
            double c=Math.abs(par.get("connection factor")-snapV[d]);
            if(c<minD){
                ind=d;
                minD=c;
            }
        }
        constantParameters.put("connection factor",snapV[ind]);
        //System.out.println(snapV[ind]);

        if(par.get("movement factor")>1 || par.get("movement factor")<0){
            constantParameters.put("movement factor",0.0);
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
        if(par.get("virulence of the epidemic")>1.0 || par.get("virulence of the epidemic")<0.0){
            constantParameters.put("virulence of the epidemic",0.0);
        }
       // if(par.get("vaccination rate")>1.0 || par.get("vaccination rate")<0.0){
      //      constantParameters.put("vaccination rate",0.0);
      //  }
        if(par.get("recovery rate")>1.0 || par.get("recovery rate")<0.0){
            constantParameters.put("recovery rate",0.0);
        }

    }

    public static ArrayList<String> getParametersType(){
        ArrayList<String>par=new ArrayList<>();
        par.add("virulence of the epidemic");
      //  par.add("vaccination rate");
        par.add("recovery rate");
        return par;
    }

    public static ArrayList<String> getCellParametersType() {
        ArrayList<String>par=new ArrayList<>();
        par.add("cell population");
        par.add("connection factor");
        par.add("movement factor");
        return par;
    }
}
