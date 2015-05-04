import java.util.ArrayList;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * An abstract class for pluggable Elitism strategie
 */
public abstract class ElitismStrategy {
    protected int genGap;
    public ElitismStrategy(int generationGap){
        genGap = generationGap;
    }

    public abstract ArrayList<Trader> applyElitism(ArrayList<Trader> currentPopulation,ArrayList<Trader> offspring);
}
