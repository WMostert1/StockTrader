import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wernermostert on 2015/04/29.
 */
public class GA {
    private Renko renkoData;
    private ElitismStrategy elitismStrategy;
    private int popSize;
    private int maxGen;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy  mutationStrategy;
    private Trader [] P0;

    public GA(int populationSize, int maxGenerations, MutationStrategy mutation,CrossoverStrategy crossover,
              ElitismStrategy elitism,Renko renkoData){
        popSize = populationSize;
        elitismStrategy = elitism;
        mutationStrategy = mutation;
        crossoverStrategy = crossover;
        this.renkoData = renkoData;
        maxGen = maxGenerations;
        P0 = new Trader[popSize];
        for(int i = 0; i < populationSize;i++)
            P0[i] = new Trader(this.renkoData);
    }

    public ArrayList<Trader> train(){
        ArrayList<Trader> population = new ArrayList<Trader>(Arrays.asList(P0));
        ArrayList<Trader> bestTraders = new ArrayList<Trader>();


        int progressPoint = maxGen/10;
        int percentDone = 10;

        System.out.print("0%");
        for(int generationNum = 0; generationNum < maxGen;generationNum++) {

        //track-progress-start
            if (generationNum >= progressPoint) {
                progressPoint += maxGen / 10;
                System.out.print("..." + percentDone + "%");
                percentDone += 10;
            }
        //track-progress-end

            Trader bestTrader = population.get(0);
            for(Trader trader : population){
                trader.getFitness();
                if(trader.getFitness() > bestTrader.getFitness())
                    bestTrader = trader;
            }

            bestTraders.add(bestTrader);


            //Selection and crossover applied to new generation
            ArrayList<Trader> newPopulation = new ArrayList<Trader>();

            while(newPopulation.size() < popSize){
                ArrayList<Trader> offspring = crossoverStrategy.crossover(population);


                while(!offspring.isEmpty() && newPopulation.size() < popSize){
                    newPopulation.add(offspring.get(0));
                    offspring.remove(0);
                }
            }

            //Mutation is applied to new generation
            for(int i = 0; i < popSize;i++){
                Trader individual = newPopulation.get(i);
                individual.setGenotype(mutationStrategy.mutateGene(individual.getGenotype()));
            }

            ArrayList<Trader> elitistOffspring =  elitismStrategy.applyElitism(population,newPopulation);

            population = elitistOffspring;

           // population = newPopulation;

        }
        System.out.println("...100%");
        return bestTraders;
    }

    public Trader getBestTrader(){
        ArrayList<Trader> traderHistory = train();
        return traderHistory.get(traderHistory.size()-1);
    }

}
