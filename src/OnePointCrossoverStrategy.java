import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */
public class OnePointCrossoverStrategy extends CrossoverStrategy {
    public OnePointCrossoverStrategy(double crossoverRate, SelectionStrategy selectionStrategy) {
        super(crossoverRate, selectionStrategy);
    }

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

          //  System.out.println("Crossover result:");




            Renko renkoData = parents[0].getRenkoData();
            Trader child1 = new Trader(chromosome1,renkoData);
          //  System.out.println(Arrays.toString(chromosome1));
          //  System.out.println(child1.getFitness());
            result.add(child1);
            Trader child2 = new Trader(chromosome2,renkoData);
          //  System.out.println(Arrays.toString(chromosome2));
         //   System.out.println(child2.getFitness());
            result.add(child2);
            return result;
        }
    }
}
