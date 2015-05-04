import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wernermostert on 2015/05/03.
 */

/**
 * The simulation class is intended to be used as an interface to run the GA's and Hill Climber's
 */
public class Simulation {
    private static String stockName = "";
    private GA previousGA = null;

    /**
     *Reads the stock data from a file, separating each line
     * @param filename The default input file name
     * @param in The BufferedReader to get user input from the terminal
     * @return The stock data which was read from the file.
     */
    private String[] readFile(String filename, BufferedReader in) {
        System.out.println("Please enter file name of stock data: [" + filename + "]");
        String line;
        boolean fileRead = false;
        while (!fileRead) {
            try {
                line = in.readLine();
                if (line.equals("QUIT")) {
                    break;
                } else if (!line.equals("")) filename = line;

                String[] stock_data = FileOperations.readFile("stockData/" + filename).split("\r\n");
                fileRead = true;
                stockName = filename.substring(0, filename.indexOf("."));
                return stock_data;
            } catch (Exception fnf) {
                System.out.println("\"" + filename + "\" is not a valid file name.");
                System.out.println("Make sure the file is in the stockData folder.\nEnter QUIT to cancel");

            }
        }

        return null;
    }

    private String[] readFile(String filename) throws IOException {
                String[] stock_data = FileOperations.readFile("stockData/" + filename).split("\r\n");
                stockName = filename.substring(0, filename.indexOf("."));
                return stock_data;
    }

    /**
     * Runs the GA with the parameterized configuration
     * @param populationSize    The size of the population
     * @param maxGenerations    The number of generation to iterate for
     * @param mutation          The mutation strategy to apply
     * @param crossover         The crossover strategy to apply
     * @param elitism           The elitism strategy to apply
     * @param renkoData         The renko data in order to initialise the original population.
     * @param outFilename       The name of the file to report the resuls to
     * @return The best Trader found by the GA
     */
    public Trader runGA(int populationSize, int maxGenerations, MutationStrategy mutation, CrossoverStrategy crossover,
                               ElitismStrategy elitism, Renko renkoData, String outFilename) {
        System.out.println("Running GA");
        GA traderTrainer = new GA(populationSize, maxGenerations, mutation, crossover, elitism, renkoData);
        previousGA = traderTrainer;
        ArrayList<Trader> traderHistory = traderTrainer.train();

        String output = "Generation;Best_Trader_Fitness\n";
        int count = 1;
        for (int i = 0; i < traderHistory.size(); i++) {
            Trader t = traderHistory.get(i);
            output += (count++) + ";" + t.fitness + "\n";
        }
        Trader bestTrader = traderHistory.get(traderHistory.size()-1);
        FileOperations.writeToFile(Arrays.toString(bestTrader.getGenotype()),"Best"+outFilename.substring(0,outFilename.length()-4)+".txt");
        FileOperations.writeToFile(output, outFilename);

        System.out.println("GA completed successfully.\nResults saved in " + outFilename);
        return traderHistory.get(traderHistory.size() - 1);
    }

    public Trader runGA(String outFilename,Renko renkoData) {
        System.out.println("Running GA ");
        GA traderTrainer = new GA(previousGA,renkoData);
        previousGA = traderTrainer;

        ArrayList<Trader> traderHistory = traderTrainer.train();

        String output = "Generation;Best_Trader_Fitness\n";
        int count = 1;
        for (int i = 0; i < traderHistory.size(); i++) {
            Trader t = traderHistory.get(i);
            output += (count++) + ";" + t.fitness + "\n";
        }
        Trader bestTrader = traderHistory.get(traderHistory.size()-1);
        FileOperations.writeToFile(Arrays.toString(bestTrader.getGenotype()),"Best"+outFilename.substring(0,outFilename.length()-4)+".txt");
        FileOperations.writeToFile(output, outFilename);

        System.out.println("GA completed successfully.\nResults saved in " + outFilename);
        return traderHistory.get(traderHistory.size() - 1);
    }

    /**
     *Runs the Hill Climber algorithm on a given data set and reports the results to a text file
     * @param filename The default input file name
     * @param in The BufferedReader to get user input from the terminal
     * @throws IOException An error that occurred reading reading/writing to the file
     */
    public void getTraderFitness(String filename, BufferedReader in) throws IOException {
        String line;

        String[] stock_data = readFile(filename, in);


        System.out.println("Constructing Renko Graph...");
        Renko renkoGraph = new Renko(stock_data);
        System.out.println("Completed");

        String traderFile = "bestTrader.txt";
        System.out.println("Enter path of trader file: [" + traderFile + "]");
        line = in.readLine();
        if (!line.equals("")) traderFile = line;

        String traderConfig = FileOperations.readFile(traderFile);

        traderConfig = traderConfig.replace("[", "");
        traderConfig = traderConfig.replace("]", "");
        traderConfig = traderConfig.replace(",", "");

        char[] configuration = new char[traderConfig.length()];

        for (int i = 0; i < traderConfig.length(); i++)
            configuration[i] = traderConfig.charAt(i);


        Trader testTrader = new Trader(configuration, renkoGraph);

        System.out.println(testTrader.getFitness());
    }

    /**
     *Runs the Hill Climber algorithm on a given data set and reports the results to a text file
     * @param filename The default input file name
     * @param in The BufferedReader to get user input from the terminal
     * @throws IOException An error that occurred reading reading/writing to the file
     */

    public void produceCompleteResults(String filename, BufferedReader in) throws IOException {
        String line;

        ArrayList<Trader> optimalTraders = new ArrayList<Trader>();

        String[] stock_data = readFile(filename, in);

        System.out.println("Constructing Renko Graph...");
        Renko renkoGraph = new Renko(stock_data);
        System.out.println("Completed");


        //////////////////////// Hill Climbing ////////////////////////



        for(int i = 0; i < 6;i++) {
            HillClimber climber = new HillClimber(renkoGraph);
            Trader bestTrader = climber.getBestTrader();

            optimalTraders.add(bestTrader);


            String report = "";
            int count = 0;
            for (double f : climber.fitnessRecords) {
                report += (count++) + ";" + f + "\n";
            }
            FileOperations.writeToFile(report, stockName + " - hillClimber"+i+".csv");
            System.out.println("Completed.\nDetailed results saved to file " + stockName + " - hillClimber" + i + ".csv");

        }
        //////////////////////// Genetic Algorithm ////////////////////////

        //populationSize,maxGenerations,mutation,crossover,elitism,renkoData


        int popSize = 40;
        System.out.println("Please select population size : [" + popSize + "]");
        line = in.readLine();
        if (!line.equals("")) popSize = new Integer(line);

        int maxGenerations = 500;
        System.out.println("Please select the maximum number of generations: [" + maxGenerations + "]");
        line = in.readLine();
        if (!line.equals("")) maxGenerations = new Integer(line);

        double mutationRate = 0.01;
        System.out.println("Please select the mutation rate (0.0-1.0): [" + mutationRate + "]");
        line = in.readLine();
        if (!line.equals("")) mutationRate = new Double(line);

        double crossoverRate = 0.6;
        System.out.println("Please select the crossover rate (0.0-1.0): [" + crossoverRate + "]");
        line = in.readLine();
        if (!line.equals("")) crossoverRate = new Double(line);

        int genGap = 4;
        System.out.println("Please select the generation gap: [" + genGap + "]");
        line = in.readLine();
        if (!line.equals("")) genGap = new Integer(line);

        int tournamentSize = 8;
        System.out.println("Please select the tournament size: ["+tournamentSize+"]");
        line = in.readLine();
        if(!line.equals("")) tournamentSize = new Integer(line);


        //TWO POINT CROSSOVER
        System.out.println("Random Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new TwoPointCrossoverStrategy(crossoverRate, new RandomSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GATwoPointCrossoverRandomSelection.csv"));

        System.out.println("Tournament Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new TwoPointCrossoverStrategy(crossoverRate, new TournamentSelectionStrategy(tournamentSize)),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GATwoPointCrossoverTournamentSelection.csv"));

        System.out.println("Rank Based Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new TwoPointCrossoverStrategy(crossoverRate, new RankBasedSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GATwoPointCrossoverRankSelection.csv"));

        System.out.println("Fitness Proportional Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new TwoPointCrossoverStrategy(crossoverRate, new FitnessProportionalSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GATwoPointCrossoverFitnessPropSelection.csv"));


        //ONE POINT CROSSOVER
        System.out.println("Random Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new OnePointCrossoverStrategy(crossoverRate, new RandomSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GAOnePointCrossoverRandomSelection.csv"));

        System.out.println("Tournament Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new OnePointCrossoverStrategy(crossoverRate, new TournamentSelectionStrategy(tournamentSize)),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GAOnePointCrossoverTournamentSelection.csv"));

        System.out.println("Rank Based Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new OnePointCrossoverStrategy(crossoverRate, new RankBasedSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GAOnePointCrossoverRankSelection.csv"));

        System.out.println("Fitness Proportional Selection:");
        optimalTraders.add(runGA(popSize, maxGenerations, new SinglePointMutationStrategy(mutationRate),
                new OnePointCrossoverStrategy(crossoverRate, new FitnessProportionalSelectionStrategy()),
                new BestKElitismStrategy(genGap), renkoGraph, stockName + " - GAOnePointCrossoverFitnessPropSelection.csv"));

        Trader absoluteBest = optimalTraders.get(0);
        for (Trader t : optimalTraders)
            if (t.getFitness() > absoluteBest.getFitness())
                absoluteBest = t;

        FileOperations.writeToFile(Arrays.toString(absoluteBest.getGenotype()), "bestTrader.txt");


    }

    /**
     * Runs the Hill Climber algorithm on a given data set and reports the results to a text file
     * @param filename The default input file name
     * @param in The BufferedReader to get user input from the terminal
     * @throws IOException An error that occurred reading reading/writing to the file
     */
    public void produceHillClimberResults(String filename, BufferedReader in) throws IOException{
        String[] stock_data = readFile(filename,in);

        System.out.println("Constructing Renko Graph...");
        Renko renkoGraph = new Renko(stock_data);
        System.out.println("Completed");

        System.out.println("Finding best Trader using Hill Climbing... (random start)");
        HillClimber climber = new HillClimber(renkoGraph);
        Trader bestTrader = climber.getBestTrader();

        String report = "";
        int count = 0;
        for(double f : climber.fitnessRecords){
            report += (count++)+";"+f+"\n";
        }
        FileOperations.writeToFile(report,stockName+" - hillClimber.csv");
        System.out.println("Completed.\nDetailed results saved to file "+stockName+" - hillClimber.csv");
    }

    public void produceGAMultipleFileTraining(BufferedReader in) throws IOException{
        File folder = new File("./stockData");
        File[] listOfFiles = folder.listFiles();
        String[] fileNames = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            fileNames[i] = listOfFiles[i].getName();
        }

        ArrayList<String> filesToRunOn = new ArrayList<String>();

        System.out.println("Please select the files to train the GA on:");
        System.out.println("Type: FIN when done");

        String line = "";
        for(int i =0; i<fileNames.length;i++)
            System.out.println("["+i+"] : "+fileNames[i]);
        do{
            line = in.readLine();
            if(!line.equals("FIN")){
            try{
            int choice = new Integer(line);
                System.out.println(fileNames[choice]+" has been selected.");
                filesToRunOn.add(fileNames[choice]);
            }catch (NumberFormatException nfe){
                System.out.println("Invalid selection made, please try again");
            }
        }

        }while(!line.equals("FIN"));



        int popSize = 40;
        System.out.println("Please select population size : ["+popSize+"]");
        line = in.readLine();
        if(!line.equals("")) popSize = new Integer(line);

        int maxGenerations = 500;
        System.out.println("Please select the maximum number of generations: ["+maxGenerations+"]");
        line = in.readLine();
        if(!line.equals("")) maxGenerations = new Integer(line);

        double mutationRate = 0.01;
        System.out.println("Please select the mutation rate (0.0-1.0): ["+mutationRate+"]");
        line = in.readLine();
        if(!line.equals("")) mutationRate = new Double(line);

        double crossoverRate = 0.6;
        System.out.println("Please select the crossover rate (0.0-1.0): ["+crossoverRate+"]");
        line = in.readLine();
        if(!line.equals("")) crossoverRate = new Double(line);

        int genGap = 4;
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

        String outputFileName = "defaultTrainedOutput.csv";
        System.out.println("Please select output file name: ["+outputFileName+"]");
        line = in.readLine();
        if(!line.equals("")) outputFileName = line;


        String[] stock_data = readFile(filesToRunOn.get(0));
        System.out.println("Constructing Renko Graph...");
        Renko renkoGraph = new Renko(stock_data);
        System.out.println("Completed");

        runGA(popSize, maxGenerations, mutationStrat, crossoverStrat,
                elitismStrat, renkoGraph, "0"+stockName+" - "+outputFileName);

        for(int i = 1; i < filesToRunOn.size();i++) {
            String [] new_stock_data = readFile(filesToRunOn.get(i));
            System.out.println("Constructing Renko Graph...");
            renkoGraph = new Renko(new_stock_data);
            System.out.println("Completed");
            runGA(i+stockName+" - " + outputFileName,renkoGraph);
        }

    }



    /**
     *Runs the Hill Climber algorithm on a given data set and reports the results to a text file
     * @param filename The default input file name
     * @param in The BufferedReader to get user input from the terminal
     * @throws IOException An error that occurred reading reading/writing to the file
     */
    public void produceGAResults(String filename, BufferedReader in) throws IOException{
        String line;
        String[] stock_data = readFile(filename,in);

        System.out.println("Constructing Renko Graph...");
        Renko renkoGraph = new Renko(stock_data);
        System.out.println("Completed");

        int popSize = 40;
        System.out.println("Please select population size : ["+popSize+"]");
        line = in.readLine();
        if(!line.equals("")) popSize = new Integer(line);

        int maxGenerations = 500;
        System.out.println("Please select the maximum number of generations: ["+maxGenerations+"]");
        line = in.readLine();
        if(!line.equals("")) maxGenerations = new Integer(line);

        double mutationRate = 0.01;
        System.out.println("Please select the mutation rate (0.0-1.0): ["+mutationRate+"]");
        line = in.readLine();
        if(!line.equals("")) mutationRate = new Double(line);

        double crossoverRate = 0.6;
        System.out.println("Please select the crossover rate (0.0-1.0): ["+crossoverRate+"]");
        line = in.readLine();
        if(!line.equals("")) crossoverRate = new Double(line);

        int genGap = 4;
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


        for(int i = 0; i < 6; i++)
        runGA(popSize, maxGenerations, mutationStrat,crossoverStrat,
                elitismStrat, renkoGraph, stockName+i+" - "+outputFileName);

    }




}
