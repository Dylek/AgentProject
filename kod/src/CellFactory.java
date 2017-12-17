public class CellFactory {


    public static Cell getCell(EpidemicModels model) {
        switch (model){
            case GAMEOFLIFE: return new CellGameOfLife();
            case SIS:return new CellSIS();
            case SIR: return new CellSIR();
            case SEIR: return new CellSEIR();
            default: return null;
        }
    }
}
