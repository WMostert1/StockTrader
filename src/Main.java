import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

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
                return stock_data;
            } catch (Exception fnf) {
                System.out.println("\"" + filename + "\" is not a valid file name.");
                System.out.println("Make sure the file is in the stockData folder.\nEnter QUIT to cancel");

            }
        }

        return null;
    }



    public static void runGA(int populationSize, int maxGenerations, MutationStrategy mutation,CrossoverStrategy crossover,
                             ElitismStrategy elitism,Renko renkoData,String outFilename){
        System.out.println("Running GA ");
        GA traderTrainer = new GA(populationSize,maxGenerations,mutation,crossover,elitism,renkoData);

        ArrayList<Trader> traderHistory = traderTrainer.train();

        String output = "";
        int count = 1;
        for(Trader t : traderHistory){
            output+=(count++)+";"+t.fitness+"\n";
        }

        FileOperations.writeToFile(output,outFilename);

        System.out.println("GA completed successfully.\nResults saved in "+outFilename);
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

                }else if(line.equals("RUN COMPLETE")){
                    String[] stock_data = readFile(filename,in);
                    if(stock_data == null) break;

                    System.out.println("Constructing Renko Graph...");
                    Renko renkoGraph = new Renko(stock_data);
                    System.out.println("Completed");

                    //////////////////////// Hill Climbing ////////////////////////

                    System.out.println("Finding best Trader using Hill Climbing... (random start)");
                    HillClimber climber = new HillClimber(renkoGraph);
                    Trader bestTrader = climber.getBestTrader();

                    String traderChoices = "[";
                    for(char c : bestTrader.getGenotype()){
                        traderChoices += c+", ";
                    }
                    traderChoices = traderChoices.substring(0,traderChoices.length()-2)+"]";
                    double fitness = bestTrader.getFitness();
                    System.out.println("Trader:  "+traderChoices);
                    System.out.println("Fitness: "+fitness);

                    String report = "";
                    int count = 0;
                    for(double f : climber.fitnessRecords){
                        report += (count++)+";"+f+"\n";
                    }
                    FileOperations.writeToFile(report,"hillClimber.csv");
                    System.out.println("Completed.\nDetailed results saved to file hillClimber.csv");

                    //////////////////////// Genetic Algorithm ////////////////////////

                    //populationSize,maxGenerations,mutation,crossover,elitism,renkoData


                    int popSize = 10;
                    System.out.println("Please select population size : ["+popSize+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    int maxGenerations = 20;
                    System.out.println("Please select the maximum number of generations: ["+maxGenerations+"]");
                    line = in.readLine();
                    if(!line.equals("")) popSize = new Integer(line);

                    double mutationRate = 0.01;
                    System.out.println("Please select the mutation rate (0.0-1.0): ["+mutationRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) mutationRate = new Double(line);

                    double crossoverRate = 0.8;
                    System.out.println("Please select the crossover rate (0.0-1.0): ["+crossoverRate+"]");
                    line = in.readLine();
                    if(!line.equals("")) crossoverRate = new Double(line);

                    int genGap = 5;
                    System.out.println("Please select the generation gap: ["+genGap+"]");
                    line = in.readLine();
                    if(!line.equals("")) genGap = new Integer(line);


                    System.out.println("Random Selection:");
                    runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new RandomSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, "GARandomSelection.csv");

                    System.out.println("Tournament Selection:");
                    runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new TournamentSelectionStrategy(5)),
                            new BestKElitismStrategy(genGap), renkoGraph, "GATournamentSelection.csv");

                    System.out.println("Rank Based Selection:");
                    runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new RankBasedSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, "GARankSelection.csv");

                    System.out.println("Fitness Proportional Selection:");
                    runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                            new OnePointCrossoverStrategy(crossoverRate, new FitnessProportionalSelectionStrategy()),
                            new BestKElitismStrategy(genGap), renkoGraph, "GAFitnessPropSelection.csv");



                }
                else if(line.equals("SELECT HillClimber")){
                    String[] stock_data = readFile(filename,in);
                    if(stock_data == null) break;

                    System.out.println("Constructing Renko Graph...");
                    Renko renkoGraph = new Renko(stock_data);
                    System.out.println("Completed");

                    System.out.println("Finding best Trader using Hill Climbing... (random start)");
                    HillClimber climber = new HillClimber(renkoGraph);
                    Trader bestTrader = climber.getBestTrader();

                    String traderChoices = "[";
                    for(char c : bestTrader.getGenotype()){
                        traderChoices += c+", ";
                    }
                    traderChoices = traderChoices.substring(0,traderChoices.length()-2)+"]";
                    double fitness = bestTrader.getFitness();
                    System.out.println("Trader:  "+traderChoices);
                    System.out.println("Fitness: "+fitness);

                    String report = "Trader for "+filename+"\n\nTrader:  "+traderChoices+"\nFitness: "+fitness+"\nFitness changes: \n";
                    for(double f : climber.fitnessRecords){
                        report += f+"\n";
                    }
                    FileOperations.writeToFile(report,"hillClimber.txt");
                    System.out.println("Completed.\nDetailed results saved to file hillClimber.txt");

                }else if(line.equals("SELECT GA")){

                }
                else{
                    System.out.println("\""+line+"\" is not a recognized command.");
                }

            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }

        }



        char [] config = {'H',
                'B',
                'S',
                'B',
                'H',
                'B',
                'S',
                'B',
                'H',
                'H',
                'H',
                'S',
                'B',
                'B',
                'H',
                'B',
                'H',
                'H',
                'S',
                'B',
                'H',
                'H',
                'H',
                'S',
                'B',
                'B',
                'B',
                'H',
                'B',
                'H',
                'B',
                'H'};
try {
    String[] stock_data = FileOperations.readFile("stockData/agl.dat").split("\r\n");
    Renko renkoGraph = new Renko(stock_data);
    for(int i = 0; i < 100; i++) {
        Trader firstTrader = new Trader(config, renkoGraph);
        double fitness = firstTrader.getFitness();
        System.out.println(fitness);

    }
}catch(Exception e){

    }





    }
}
