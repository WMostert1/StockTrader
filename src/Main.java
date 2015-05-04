import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Main class is the interface the user interacts with to run the different algorithms
 */
public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation();
	// write your code here
        String filename = "agl.dat";

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
                    System.out.println("SELECT GA_MANY");
                    System.out.println("- Runs the Genetic Algorithm with specified parameters on many different files");
                    System.out.println();
                    System.out.println("RUN TRADER");
                    System.out.println("- Runs the specified trader on a data set");
                    System.out.println();
                    System.out.println("QUIT");
                    System.out.println("- Ends the program");

                }else if(line.equals("SELECT GA_MANY")){
                    sim.produceGAMultipleFileTraining(in);
                }else if(line.equals("RUN TRADER")){
                    sim.getTraderFitness(filename, in);
                }
                else if(line.equals("RUN COMPLETE")){
                    sim.produceCompleteResults(filename, in);
                }else if(line.equals("SELECT HillClimber")){
                   sim.produceHillClimberResults(filename,in);
                }else if(line.equals("SELECT GA")){
                    sim.produceGAResults(filename,in);
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

    }
}
