import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by wernermostert on 2015/05/01.
 */
public class TwoPointCrossoverStrategy extends CrossoverStrategy{
    public TwoPointCrossoverStrategy(double crossoverRate, SelectionStrategy selectionStrategy) {
        super(crossoverRate, selectionStrategy);
    }

    @Override
    public ArrayList<Trader> crossover(ArrayList<Trader> population) {
        ArrayList<Trader> result = new ArrayList<Trader>();

        double [] populationFitness = new double[population.size()];
        for(int i =0;i<populationFitness.length;i++)
            populationFitness[i] = population.get(i).getFitness();

        int size = population.size();

        Trader [] parents = new Trader[size];
        do {
            parents[0] = selectionStrategy.select(population, populationFitness);
            parents[1] = selectionStrategy.select(population, populationFitness);
        }while(Arrays.equals(parents[0].getGenotype(),parents[1].getGenotype()));

        char [] chromosome1 = parents[0].getGenotype();
        char [] chromosome2 = parents[1].getGenotype();

        Random r = new Random();

        int skip = r.nextInt(3);
        int third = chromosome1.length/3;
        int startPoint = 0;
        int endPoint = third;
        for(int k = 0; k < 3; k++) {
            if(k == 2)
                endPoint = chromosome1.length;
            if(k == skip) continue;
            for (int i = startPoint; i < endPoint; i++) {
                char tempGene = chromosome1[i];
                chromosome1[i] = chromosome2[i];
                chromosome2[i] = tempGene;
            }
            startPoint = endPoint;
            endPoint += third;
        }

        Renko renkoData = parents[0].getRenkoData();
        Trader child1 = new Trader(chromosome1,renkoData);
        Trader child2 = new Trader(chromosome2,renkoData);
        result.add(child2);
        result.add(child1);
        return  result;
    }
}
