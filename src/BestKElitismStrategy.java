import java.util.ArrayList;
import java.util.Random;


/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * This class implements the Elitism strategy used, called "best K". The best K amount of Trader from the current
 * population are selected and replaces the worst K offspring (in the new population).
 */
public class BestKElitismStrategy extends ElitismStrategy {
    /**
     * Constructor for initialisation
     * @param generationGap The K value (the amount of Traders to replace)
     */
    public BestKElitismStrategy(int generationGap) {
        super(generationGap);
    }

    /**
     * Applies the best K elitism strategy to determine the new population.
     * @param currentPopulation A collection of Traders (the previous generation)
     * @param offspring A collection of Traders (the new generation)
     * @return The "final" collection of Traders
     */
    @Override
    public ArrayList<Trader> applyElitism(ArrayList<Trader> currentPopulation, ArrayList<Trader> offspring) {
        if(genGap <= 0) return offspring;

        //Determine and sort the fitness of the current population

        int size = currentPopulation.size();
        double [] populationFitness = new double[size];
        for(int i =0;i<size;i++)
            populationFitness[i] = currentPopulation.get(i).getFitness();
        
        
        Trader [] populationArr = new Trader[size];

        for(int i = 0; i < size;i++)
            populationArr[i] = currentPopulation.get(i);
        
        for(int i = 0; i < size-1; i++){
            for(int j = i+1; j < size; j++){

                if(populationFitness[j] > populationFitness[i]){
                    Trader tempTrader = populationArr[j];
                    double tempPopulationFitness= populationFitness[j];

                    populationFitness[j] = populationFitness[i];
                    populationFitness[i] = tempPopulationFitness;

                    populationArr[j] = populationArr[i];
                    populationArr[i] = tempTrader;
                }
            }
        }


        //Determine and sort the fitness of the offspring (new) population

        double [] offspringFitness = new double[size];

        for(int i =0; i< size;i++){
            offspringFitness[i] = offspring.get(i).getFitness();
        }


        Trader [] offspringArr = new Trader[size];

        for(int i = 0; i < size;i++)
            offspringArr[i] = offspring.get(i);

        for(int i = 0; i < size-1; i++){
            for(int j = i+1; j < size; j++){
                if(offspringFitness[j] > offspringFitness[i]){
                    Trader tempTrader = offspringArr[j];
                    double tempOffspringFitness= offspringFitness[j];

                    offspringFitness[j] = offspringFitness[i];
                    offspringFitness[i] = tempOffspringFitness;

                    offspringArr[j] = offspringArr[i];
                    offspringArr[i] = tempTrader;
                }
            }
        }

        //Replaces the worst Traders in thew offspring ("new") generation
        //with the best Traders in the previous generation

        for(int i = 0; i < genGap;i++)
            offspring.remove(offspringArr[size-1-i]);

        for(int i = 0; i < genGap;i++)
        offspring.add(populationArr[i]);

        return offspring;
    }
}
