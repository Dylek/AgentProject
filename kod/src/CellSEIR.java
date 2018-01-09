import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CellSEIR implements Cell {
    //0-empty,1-S,2-I,3-R,4-exposed
    //0-empty,1-S,2-E,3-I,4-R
    //0-empty space, like ocean, not inhabitated land
    private int x;
    private int y;
    private int type=0;
    private ArrayList<CellSEIR> neighboors=new ArrayList<CellSEIR>();
    private HashMap<CellSEIR,Integer[]> neighbourInfo=new HashMap<>();
    public int cellPopulation=100;//constant//cot the same for every cell
    //it holds parameters like specific of One cell
    private HashMap<String,Double> parameters;
    private int nextState=0;
    private HashMap<String, Double> nextStateParameters;

    //It holds parameters like vacination rate; things constant for whole lattice
    private static HashMap<String,Double> constantParameters;
    public CellSEIR(int x, int y){
        parameters=new HashMap<>();
        nextStateParameters=new HashMap<>();
        constantParameters=new HashMap<>();
        //nextState=0;
        this.x=x;
        this.y=y;
        this.type=0;
        parameters.put("infected",0.0);
        parameters.put("suspectible",0.0);
        parameters.put("exposed",0.0);
        parameters.put("recovered",0.0);
    }

    @Override
    public void addNeigbour(Cell c) {
        CellSEIR temp=(CellSEIR)c;
        neighboors.add((CellSEIR)c);
        Integer[] tem={temp.x-this.x,temp.y-this.y};
        neighbourInfo.put((CellSEIR)c,tem);
    }

    @Override
    public Paint getColor() {
        return BoardCA.getCellColor(this.type);
    }

    private double getBigSum(){
        double bigSum=0;
        //carefull
        for(CellSEIR cell: this.neighboors){
            bigSum+=cell.cellPopulation/this.cellPopulation*this.getMovementNumber(cell) * cell.parameters.get("infected");
        }

        return bigSum;
    }

    public double getMovementNumber(CellSEIR cell){
        double ni=0;
        double smallSum=0;

        for(CellSEIR neighboor: this.neighboors){
            smallSum+=1/Math.sqrt(Math.pow(neighbourInfo.get(neighboor)[0],2)+Math.pow(neighbourInfo.get(neighboor)[1],2));
        }

        ni=(1/Math.sqrt(Math.pow(neighbourInfo.get(cell)[0],2)+Math.pow(neighbourInfo.get(cell)[1],2)))/smallSum;

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
            double exposed=0;

            //TODO dokodzić przemieszczenmia
            suspectible = parameters.get("suspectible") +
                    -constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    -constantParameters.get("virulence of the epidemic")*parameters.get("suspectible") * this.getBigSum();

            exposed=(1 - constantParameters.get("infected rate(from exposed)")) * parameters.get("exposed") +
                    +constantParameters.get("virulence of the epidemic") * parameters.get("suspectible") * parameters.get("infected") +
                    constantParameters.get("virulence of the epidemic")* parameters.get("suspectible") * this.getBigSum();

            infected=(1 - constantParameters.get("recovery rate")) * parameters.get("infected")+
            constantParameters.get("infected rate(from exposed)")*parameters.get("exposed");
            recovered = parameters.get("recovered")+constantParameters.get("recovery rate") * parameters.get("infected");



            //cell change type if one type of inhabitans has majory in population
            //Because following eautaion should alwways stand: infected+exposed+suspectible+recovered=1
            if(infected>exposed && infected>suspectible && infected>recovered){
                nextState=2;//2-infected
            }else if(exposed>infected && exposed>suspectible && exposed>recovered){
                nextState=4;//4-exposed
            }else if(suspectible> exposed && suspectible> infected && suspectible>recovered){
                nextState=1;//1-suspectible
            }else if(recovered>infected && recovered> exposed && recovered>suspectible){
                nextState=3;//3-recovered
            }else{
                nextState=this.type;//no change
            }

            nextStateParameters.put("suspectible", suspectible);
            nextStateParameters.put("infected", infected);
            nextStateParameters.put("recovered", recovered);
            nextStateParameters.put("exposed",exposed);
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
                parameters.put("exposed",0.0);
                parameters.put("recovered",0.0);
                break;
            case 1:
                parameters.put("infected",0.0);
                parameters.put("suspectible",1.0);
                parameters.put("exposed",0.0);
                parameters.put("recovered",0.0);
                break;
            case 4:
                parameters.put("infected",0.0);
                parameters.put("suspectible",0.0);
                parameters.put("exposed",1.0);
                parameters.put("recovered",0.0);
                break;
            case 2:
                parameters.put("infected",1.0);
                parameters.put("suspectible",0.0);
                parameters.put("exposed",0.0);
                parameters.put("recovered",0.0);
                break;
            case 3:
                parameters.put("infected",0.0);
                parameters.put("suspectible",0.0);
                parameters.put("exposed",0.0);
                parameters.put("recovered",1.0);
                break;
            default:
        }
        this.setParameters(par);
        this.type=i;
    }

    @Override
    public int getNumberOfTypes() {
        return 5;
    }

    //@Override
    //set parameters local for a cell
    private void setParameters(HashMap<String, Double> par) {
        constantParameters.putAll(par);


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

        if(par.get("recovery rate")>1 || par.get("recovery rate")<0){
            constantParameters.put("recovery rate",0.3);
        }
        if(par.get("infected rate(from exposed)")>1 || par.get("infected rate(from exposed)")<0){
            constantParameters.put("infected rate(from exposed)",0.3);
        }

    }

    public static ArrayList<String> getParametersType(){
        ArrayList<String>par=new ArrayList<>();
        par.add("virulence of the epidemic");
        par.add("infected rate(from exposed)");
        par.add("recovery rate");

        return par;
    }

    public static ArrayList<String> getCellParametersType() {
        ArrayList<String>par=new ArrayList<>();
        par.add("cell population");

        return par;
    }
}
