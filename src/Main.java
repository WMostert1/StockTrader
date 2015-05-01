import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static String name = "";
    private static String [] readFile(String filename, BufferedReader in){
        System.out.println("Please enter file name of stock data: ["+filename+"]");
        String line;
        boolean fileRead = false;
        while(!fileRead) {
            try {
                line = in.readLine();
                if(line.equals("QUIT")){
                    break;
                }
                else if(!line.equals("")) filename = line;

                String [] stock_data = FileOperations.readFile("stockData/"+filename).split("\r\n");
                fileRead = true;
                name = filename.substring(0,filename.indexOf("."));
                return stock_data;
            } catch (Exception fnf) {
                System.out.println("\"" + filename + "\" is not a valid file name.");
                System.out.println("Make sure the file is in the stockData folder.\nEnter QUIT to cancel");

            }
        }

        return null;
    }

    public static Trader runGA(int populationSize, int maxGenerations, MutationStrategy mutation,CrossoverStrategy crossover,
                             ElitismStrategy elitism,Renko renkoData,String outFilename){
        System.out.println("Running GA ");
        GA traderTrainer = new GA(populationSize,maxGenerations,mutation,crossover,elitism,renkoData);

        ArrayList<Trader> traderHistory = traderTrainer.train();

        String output = "Generation;Best_Trader_Fitness;Average_Fitness_of_Population\n";
        int count = 1;
        for(int i =0;i < traderHistory.size();i++){
            Trader t = traderHistory.get(i);
            output+=(count++)+";"+t.fitness+";"+traderTrainer.generationAverageFitness[i]+"\n";
        }

        FileOperations.writeToFile(output,outFilename);

        System.out.println("GA completed successfully.\nResults saved in "+outFilename);
        return traderHistory.get(traderHistory.size()-1);
    }

    public static void main(String[] args) {
	// write your code here
        String filename = "agl.dat";
        int last_n_bricks = 5;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("############################################################");
        System.out.println("##         Welcome to Werner Mostert's - 13019695         ##");
        System.out.println("##                  COS 314 Project 2                     ##");
        System.out.println("############################################################");
        System.out.println("Listening for input...type HELP for a list of input commands");
        System.out.println("Values in [ ] are  defaults. Just press enter to continue.");

        String defValue = "RUN COMPLETE";
        System.out.println("["+defValue+"]");
        boolean run = true;
        while(run){
            try {
                String line = in.readLine();
                if(line.equals("")) line = defValue;
                if(line.equals("HELP")){
                    System.out.println("Commands:");
                    System.out.println("------------------------------------------------------------------------------------");
                    System.out.println("RUN COMPLETE");
                    System.out.println("- Runs the basic HillClimbing algorithm and 8 configurations of the Genetic Algorithm");
                    System.out.println();
                    System.out.println("SELECT HillClimber");
                    System.out.println("- Runs the basic HillClimbing algorithm.");
                    System.out.println();
                    System.out.println("SELECT GA");
                    System.out.println("- Runs the Genetic Algorithm with specified parameters");
                    System.out.println();
                    System.out.println("QUIT");
                    System.out.println("- Ends the program");

                }
                else if(line.equals("RUN COMPLETE")){


                    ArrayList<Trader> optimalTraders = new ArrayList<Trader>();

                    String[] stock_data = readFile(filename,in);
                    if(stock_data == null) break;

                    System.out.println("Constructing Renko Graph...");
                    Renko renkoGraph = new Renko(stock_data);
                    System.out.println("Completed");

                    System.out.println(renkoGraph.getStringRepresentation());

                    //////////////////////// Hill Climbing ////////////////////////

                    System.out.println("Finding best Trader using Hill Climbing... (random start)");
                    HillClimber climber = new HillClimber(renkoGraph);
                    Trader bestTrader = climber.getBestTrader();

                    optimalTraders.add(bestTrader);

                    String traderChoices = "[";
                    for(char c : bestTrader.getGenotype()){
                        traderChoices += c+", ";
                    }
                    traderChoices = traderChoices.substring(0,traderChoices.length()-2)+"]";
                    double fitness = bestTrader.getFitness();


                    String report = "";
                    int count = 0;
                    for(double f : climber.fitnessRecords){
                        report += (count++)+";"+f+"\n";
                    }
                    FileOperations.writeToFile(report,name+"|hillClimber.csv");
                    System.out.println("Completed.\nDetailed results saved to file "+name+"|hillClimber.csv");

                    //////////////////////// Genetic Algorithm ////////////////////////

                    //populationSize,maxGenerations,mutation,crossover,elitism,renkoData


                    int popSize = 60;
                    System.out.println("Please select population size : ["+popSize+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    int maxGenerations = 300;
                    System.out.println("Please select the maximum number of generations: ["+maxGenerations+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    double mutationRate = 0.01;
                    System.out.println("Please select the mutation rate (0.0-1.0): ["+mutationRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) mutationRate = new Double(line);

                    double crossoverRate = 0.85;
                    System.out.println("Please select the crossover rate (0.0-1.0): ["+crossoverRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) crossoverRate = new Double(line);

                    int genGap = 5;
                    System.out.println("Please select the generation gap: ["+genGap+"]");
                    line = in.readLine();
                    if(!line.equals("")) genGap = new Integer(line);

                    
                    //TWO POINT CROSSOVER
                    System.out.println("Random Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new TwoPointCrossoverStrategy(crossoverRate, new RandomSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GATwoPointCrossoverRandomSelection.csv"));

                    System.out.println("Tournament Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new TwoPointCrossoverStrategy(crossoverRate, new TournamentSelectionStrategy(8)),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GATwoPointCrossoverTournamentSelection.csv"));

                    System.out.println("Rank Based Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new TwoPointCrossoverStrategy(crossoverRate, new RankBasedSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GATwoPointCrossoverRankSelection.csv"));

                    System.out.println("Fitness Proportional Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new TwoPointCrossoverStrategy(crossoverRate, new FitnessProportionalSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GATwoPointCrossoverFitnessPropSelection.csv"));

                    
                    //ONE POINT CROSSOVER
                    System.out.println("Random Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new RandomSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GAOnePointCrossoverRandomSelection.csv"));

                    System.out.println("Tournament Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new TournamentSelectionStrategy(5)),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GAOnePointCrossoverTournamentSelection.csv"));

                    System.out.println("Rank Based Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new RankBasedSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GAOnePointCrossoverRankSelection.csv"));

                    System.out.println("Fitness Proportional Selection:");
                    optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new FitnessProportionalSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, name+"|GAOnePointCrossoverFitnessPropSelection.csv"));

                    Trader absoluteBest = optimalTraders.get(0);
                    for(Trader t : optimalTraders)
                        if(t.getFitness() > absoluteBest.getFitness())
                            absoluteBest = t;

                    FileOperations.writeToFile(Arrays.toString(absoluteBest.getGenotype()),"bestTrader.txt");

                }
                else if(line.equals("SELECT HillClimber")){
                    String[] stock_data = readFile(filename,in);
                    if(stock_data == null) break;

                    System.out.println("Constructing Renko Graph...");
                    Renko renkoGraph = new Renko(stock_data);
                    System.out.println("Completed");

                    System.out.println(renkoGraph.getStringRepresentation());

                    System.out.println("Finding best Trader using Hill Climbing... (random start)");
                    HillClimber climber = new HillClimber(renkoGraph);
                    Trader bestTrader = climber.getBestTrader();

                    String traderChoices = "[";
                    for(char c : bestTrader.getGenotype()){
                        traderChoices += c+", ";
                    }
                    traderChoices = traderChoices.substring(0,traderChoices.length()-2)+"]";
                    double fitness = bestTrader.getFitness();


                    String report = "";
                    int count = 0;
                    for(double f : climber.fitnessRecords){
                        report += (count++)+";"+f+"\n";
                    }
                    FileOperations.writeToFile(report,name+"|hillClimber.csv");
                    System.out.println("Completed.\nDetailed results saved to file "+name+"|hillClimber.csv");

                }else if(line.equals("SELECT GA")){
                    String[] stock_data = readFile(filename,in);
                    if(stock_data == null) break;

                    System.out.println("Constructing Renko Graph...");
                    Renko renkoGraph = new Renko(stock_data);
                    System.out.println("Completed");

                    int popSize = 60;
                    System.out.println("Please select population size : ["+popSize+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    int maxGenerations = 300;
                    System.out.println("Please select the maximum number of generations: ["+maxGenerations+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    double mutationRate = 0.01;
                    System.out.println("Please select the mutation rate (0.0-1.0): ["+mutationRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) mutationRate = new Double(line);

                    double crossoverRate = 0.85;
                    System.out.println("Please select the crossover rate (0.0-1.0): ["+crossoverRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) crossoverRate = new Double(line);

                    int genGap = 5;
                    System.out.println("Please select the generation gap: ["+genGap+"]");
                    line = in.readLine();
                    if(!line.equals("")) genGap = new Integer(line);


                    int defaultMutation = 1;
                    MutationStrategy mutationStrat;
                    System.out.println("Please select the mutation strategy: ["+defaultMutation+"]");
                    System.out.println("1 : Single Point Mutation");
                    line = in.readLine();
                    if(!line.equals("")) defaultMutation = new Integer(line);
                    switch(defaultMutation){
                        case 1:
                            mutationStrat = new SinglePointMutationStrategy(mutationRate);
                            break;
                        default:
                            System.out.println(defaultMutation + " is not a choice. Defaulting to 1.");
                            mutationStrat = new SinglePointMutationStrategy(mutationRate);
                            break;
                    }

                    int defaultSelection = 1;
                    SelectionStrategy selectionStrat;
                    System.out.println("Please select the selection strategy: ["+defaultSelection+"]");
                    System.out.println("1 : Random Selection");
                    System.out.println("2 : Fitness Proportional Selection");
                    System.out.println("3 : Rank Based Selection");
                    System.out.println("4 : Tournament Selection");
                    line = in.readLine();
                    if(!line.equals("")) defaultSelection = new Integer(line);
                    switch(defaultSelection){
                        case 1:
                            selectionStrat= new RandomSelectionStrategy();
                            break;
                        case 2:
                            selectionStrat= new FitnessProportionalSelectionStrategy();
                            break;
                        case 3:
                            selectionStrat= new RankBasedSelectionStrategy();
                            break;
                        case 4:
                                int tournamentSize = popSize/10;
                                System.out.println("Please select tournament size: ["+tournamentSize+"]");
                                line = in.readLine();
                                if(!line.equals("")) tournamentSize = new Integer(line);
                            selectionStrat= new TournamentSelectionStrategy(tournamentSize);

                            break;
                        default:
                            System.out.println(defaultSelection + " is not a choice. Defaulting to 1.");
                            selectionStrat = new RandomSelectionStrategy();
                            break;
                    }


                    int defaultCrossover = 1;
                    CrossoverStrategy crossoverStrat;
                    System.out.println("Please select the crossover strategy: ["+defaultCrossover+"]");
                    System.out.println("1 : One Point Crossover");
                    System.out.println("2 : Two Point Crossover");
                    line = in.readLine();
                    if(!line.equals("")) defaultCrossover = new Integer(line);
                    switch(defaultCrossover){
                        case 1:
                            crossoverStrat = new OnePointCrossoverStrategy(crossoverRate,selectionStrat);
                            break;
                        case 2:
                            crossoverStrat = new TwoPointCrossoverStrategy(crossoverRate,selectionStrat);
                            break;
                        default:
                            System.out.println(defaultCrossover + " is not a choice. Defaulting to 1.");
                            crossoverStrat = new OnePointCrossoverStrategy(crossoverRate,selectionStrat);
                            break;
                    }

                    int defaultElitism = 1;
                    ElitismStrategy elitismStrat;
                    System.out.println("Please select the elitism strategy: ["+defaultElitism+"]");
                    System.out.println("1 : Best K");
                    line = in.readLine();
                    if(!line.equals("")) defaultElitism = new Integer(line);
                    switch(defaultElitism){
                        case 1:
                            elitismStrat = new BestKElitismStrategy(genGap);
                            break;
                        default:
                            System.out.println(defaultElitism + " is not a choice. Defaulting to 1.");
                            elitismStrat = new BestKElitismStrategy(genGap);
                            break;
                    }

                    String outputFileName = "defaultOutput.csv";
                    System.out.println("Please select output file name: ["+outputFileName+"]");
                    line = in.readLine();
                    if(!line.equals("")) outputFileName = line;



                    runGA(popSize, maxGenerations, mutationStrat,crossoverStrat,
                            elitismStrat, renkoGraph, name+"|"+outputFileName);


                }
                else if(line.equals("QUIT")){
                    run = false;
                }
                else{
                    System.out.println("\""+line+"\" is not a recognized command.");
                }

            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }

        }



//        char [] config = {'H',
//                'B',
//                'S',
//                'B',
//                'H',
//                'B',
//                'S',
//                'B',
//                'H',
//                'H',
//                'H',
//                'S',
//                'B',
//                'B',
//                'H',
//                'B',
//                'H',
//                'H',
//                'S',
//                'B',
//                'H',
//                'H',
//                'H',
//                'S',
//                'B',
//                'B',
//                'B',
//                'H',
//                'B',
//                'H',
//                'B',
//                'H'};
//try {
//    String[] stock_data = FileOperations.readFile("stockData/agl.dat").split("\r\n");
//    Renko renkoGraph = new Renko(stock_data);
//    for(int i = 0; i < 100; i++) {
//        Trader firstTrader = new Trader(config, renkoGraph);
//        double fitness = firstTrader.getFitness();
//        System.out.println(fitness);
//
//    }
//}catch(Exception e){
//
//    }





    }
}
