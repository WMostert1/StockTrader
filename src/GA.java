import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * This is a Genetic Algorithm for optimisation of Traders
 */
public class GA {
    private ElitismStrategy elitismStrategy;
    private int popSize;
    private int maxGen;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy  mutationStrategy;
    private Trader [] P0;

    /**
     * Constructor for initialisation
     * @param populationSize    The size of the population
     * @param maxGenerations    The number of generation to iterate for
     * @param mutation          The mutation strategy to apply
     * @param crossover         The crossover strategy to apply
     * @param elitism           The elitism strategy to apply
     * @param renkoData         The renko data in order to initialise the original population.
     */
    public GA(int populationSize, int maxGenerations, MutationStrategy mutation,CrossoverStrategy crossover,
               ElitismStrategy elitism,Renko renkoData){
        popSize = populationSize;
        elitismStrategy = elitism;
        mutationStrategy = mutation;
        crossoverStrategy = crossover;
        maxGen = maxGenerations;
        P0 = new Trader[popSize];
        for(int i = 0; i < populationSize;i++)
            P0[i] = new Trader(renkoData);
    }


    public GA(GA otherGA,Renko renkoData){
        popSize = otherGA.popSize;
        elitismStrategy = otherGA.elitismStrategy;
        mutationStrategy = otherGA.mutationStrategy;
        crossoverStrategy = otherGA.crossoverStrategy;
        maxGen = otherGA.maxGen;
        P0 = otherGA.P0.clone();
        for(Trader t : P0){
            t.setRenkoData(renkoData);
        }
    }



    /**
     * This function runs the GA and adjusts the population as required
     * for maxGen generations.
     * @return
     */
    public ArrayList<Trader> train(){
        ArrayList<Trader> population = new ArrayList<Trader>(Arrays.asList(P0));
        ArrayList<Trader> bestTraders = new ArrayList<Trader>();

        //track-progress-start to indicate to the user how long the GA will still run
        int progressPoint = maxGen/10;
        int percentDone = 10;

        System.out.print("0%");

        //Stopping condition:
        //String midGenReport = "";
        for(int generationNum = 0; generationNum < maxGen;generationNum++) {
           // midGenReport += "Generation: "+generationNum+"\n";
            if (generationNum >= progressPoint) {
                progressPoint += maxGen / 10;
                System.out.print("..." + percentDone + "%");
                percentDone += 10;
            }
        //track-progress-end

            Trader bestTrader = population.get(0);
            for(Trader trader : population){
                if(trader.getFitness() > bestTrader.getFitness())
                    bestTrader = trader;
            }

            bestTraders.add(bestTrader);


            //Selection and crossover applied to new generation
            ArrayList<Trader> newPopulation = new ArrayList<Trader>();

            //Generate the new population using crossover and selection
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


            //String oldpop ="Old Population: \n";
           // int count = 0;
           // for(Trader t: population){
           //     oldpop+= count++ + " "+t.getFitness()+"\t"+Arrays.toString(t.getGenotype())+"\n";
           // }

            //The current population is updated by applying elitism
            population = elitismStrategy.applyElitism(population,newPopulation);
            //String newpop ="New Population: \n";
            //count = 0;
            //for(Trader t: population){
            //    newpop+= count++ + " "+t.getFitness()+"\t"+Arrays.toString(t.getGenotype())+"\n";
            //}
            //midGenReport += oldpop+newpop;

            //END OF GENERATION
        }
        //FileOperations.writeToFile(midGenReport,"GAMidGen.txt");
        System.out.println("...100%");
        P0 = population.toArray(P0);
        return bestTraders;
    }


    //Reports the best trader for the given data set.
    public Trader getBestTrader(){
        ArrayList<Trader> traderHistory = train();
        return traderHistory.get(traderHistory.size()-1);
    }

}
