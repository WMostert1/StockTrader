import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * HillClimber implements the steepest ascent hill climbing optimization algorithm
 */
public class HillClimber {
    public ArrayList<Double> fitnessRecords;
    private Trader currentTrader;
    private Renko renkoData;

    /**
     * The constructor does simple initialization and creates a randomly configured Trader
     * @param renkoData The RenkoGraph together with stock data container
     */
    public HillClimber(Renko renkoData){
        fitnessRecords = new ArrayList<Double>();
        this.renkoData = renkoData;
        currentTrader = new Trader(renkoData);
    }

    /**
     * Continually looks for a better solution based on current solution. If none can be found
     * the function returns that current trader
     * @return An optimized trader
     */
    public Trader getBestTrader(){
        fitnessRecords.add(currentTrader.getFitness());

        while(useFistBestNeighbour(2,2) != null){}

        return currentTrader;
    }

    /**
     * This is the neighbourhood function to determine a better neighbouring solution. Each element
     * of the trader's choices are permuted within the intervals specified by the parameters as below.
     * In the case of overPasses = 1 and innerPasses = 1, a full permutation should be done. Her name is SORFFLE.
     * @param overPasses    The interval in which choices are changed during the outer iteration
     * @param innerPasses   The interval in which choices are changed during the inner iteration
     * @return
     */
    public char [] useFistBestNeighbour(int overPasses, int innerPasses){
        if(overPasses == 0 || innerPasses == 0) return null;
        char [] possibleChoices = {'B','S','H'};

        int arraySize = currentTrader.getGenotype().length;

        for(int j = 0; j < arraySize; j+=overPasses){
            char [] currentConfiguration = currentTrader.getGenotype().clone();
            for(char outerChoice : possibleChoices) {
                if (currentConfiguration[j] == outerChoice) continue; //No need to recalculate the same configuration

                currentConfiguration[j] = outerChoice;
                for (int i = 0; i < arraySize; i+=innerPasses) {

                    char [] temporaryConfiguration = currentConfiguration.clone();

                    for (char innerChoice : possibleChoices) {
                        if (currentConfiguration[i] == innerChoice) continue; //No need to recalculate the same configuration
                        temporaryConfiguration[i] = innerChoice;
                        Trader neighbourTrader = new Trader(temporaryConfiguration, renkoData);
                        double newNeighbourFitness = neighbourTrader.getFitness();

                        if (newNeighbourFitness > fitnessRecords.get(fitnessRecords.size() - 1)) {
                            currentTrader = neighbourTrader;
                            fitnessRecords.add(newNeighbourFitness);
                            return currentConfiguration;
                    }
                }
            }
        }
        }

        return null;
    }





}
