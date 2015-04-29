import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String filename = "stockData/bil.dat";
        int last_n_bricks = 5;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("############################################");
        System.out.println("## Welcome to Werner Mostert's - 13019695 ##");
        System.out.println("##          COS 314 Project 2             ##");
        System.out.println("############################################");
        boolean go = true;
        while(go){
            try {
               // String line = in.readLine();
                String line = "SELECT HillClimber";
                if(line.equals("HELP")){

                }else if(line.equals("SELECT HillClimber")){
                    System.out.println("Values in [ ] are  defaults. Just press enter to continue.");
                    System.out.println("Please enter file name of stock data: [agl.dat]");
                    in.readLine();

                    System.out.println("Constructing Renko Graph...");
                    String [] stock_data = FileOperations.readFile(filename).split("\r\n");
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
                    System.out.println("Completed. Detailed results saved to file hillClimber.txt");






                }else if(line.equals("SELECT GA")){

                }



            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }

        }



//
//        char [] c = {'H', 'B', 'S', 'B', 'S', 'B', 'S', 'H', 'S', 'B', 'S', 'H', 'S', 'B', 'S', 'H', 'H', 'B', 'S', 'H', 'S', 'B', 'S', 'H', 'S', 'B', 'S', 'H', 'S', 'B', 'S', 'H'};
//        String [] stock_data = FileOperations.readFile(filename).split("\r\n");
//        Renko renkoGraph = new Renko(stock_data);
//        Trader firstTrader = new Trader(c,renkoGraph);
//        double fitness = firstTrader.getFitness();
//
//        Trader climber = (new HillClimber(renkoGraph)).getBestTrader();


    }
}
