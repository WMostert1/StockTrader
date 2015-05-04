import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * One point crossover divides the two chromosomes in two parts each and then interchanges
 * these parts between the two in order to obtain a more diverse population.
 */
public class OnePointCrossoverStrategy extends CrossoverStrategy {
    /**
     * Constructor for initialisation.
     * @param crossoverRate The crossover probability, 0.0 to 1.0
     * @param selectionStrategy The selection strategy to be applied in order to select the parents from the population.
     */
    public OnePointCrossoverStrategy(double crossoverRate, SelectionStrategy selectionStrategy) {
        super(crossoverRate, selectionStrategy);
    }

    /**
     * Selection and Crossover takes place here, applying the Selection strategy to determine the parents
     * and then possible applying crossover to the selected parents in
     * @param population A collection of Traders
     * @return Two Traders as offspring
     */
    @Override
    public ArrayList<Trader> crossover(ArrayList<Trader> population) {
        Trader [] parents = new Trader[2];
        double [] populationFitness = new double[population.size()];
        for(int i =0;i<populationFitness.length;i++)
            populationFitness[i] = population.get(i).getFitness();

        do {
            parents[0] = selectionStrategy.select(population, populationFitness);
            parents[1] = selectionStrategy.select(population, populationFitness);
        }while(Arrays.equals(parents[1].getGenotype(),parents[0].getGenotype()));

        Random r = new Random();
        double r1 = r.nextDouble();
        ArrayList<Trader> result = new ArrayList<Trader>();
        if(r1 > crossoverRate) {
            //Crossover does not take place. Return Trader with the same configuration as the parents.
            result.add(new Trader(parents[0].getGenotype(),parents[0].getRenkoData()));
            result.add(new Trader(parents[1].getGenotype(),parents[1].getRenkoData()));
            return result;
        }else{

            char [] chromosome1 = parents[0].getGenotype();
            char [] chromosome2 = parents[1].getGenotype();
            int n;
            if(chromosome1.length < chromosome2.length)
                n = chromosome1.length;
            else
                n = chromosome2.length;

            int crossoverPoint = r.nextInt(n);
            for(int i = crossoverPoint+1;i<n;i++){
                char tempGene = chromosome1[i];
                chromosome1[i] = chromosome2[i];
                chromosome2[i] = tempGene;
            }

            Renko renkoData = parents[0].getRenkoData();
            Trader child1 = new Trader(chromosome1,renkoData);
            result.add(child1);
            Trader child2 = new Trader(chromosome2,renkoData);
            result.add(child2);

            return result;
        }
    }
}
