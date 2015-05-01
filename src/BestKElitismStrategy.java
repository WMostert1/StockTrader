import java.util.ArrayList;
import java.util.Random;


/**
 * Created by wernermostert on 2015/04/30.
 */
public class BestKElitismStrategy extends ElitismStrategy {
    public BestKElitismStrategy(int generationGap) {
        super(generationGap);
    }

    @Override
    public ArrayList<Trader> applyElitism(ArrayList<Trader> currentPopulation, ArrayList<Trader> offspring) {
        if(genGap <= 0) return offspring;

        int size = currentPopulation.size();
        double [] populationFitness = new double[size];
        for(int i =0;i<size;i++)
            populationFitness[i] = currentPopulation.get(i).getFitness();
        
        
        Trader [] populationArr = new Trader[size];

        for(int i = 0; i < size;i++)
            populationArr[i] = currentPopulation.get(i);
        
        for(int i = 0; i < size-1; i++){
            for(int j = i+1; j < size; j++){

                if(populationFitness[j] > populationFitness[i]){
                    Trader tempTrader = populationArr[j];
                    double tempPopulationFitness= populationFitness[j];

                    populationFitness[j] = populationFitness[i];
                    populationFitness[i] = tempPopulationFitness;

                    populationArr[j] = populationArr[i];
                    populationArr[i] = tempTrader;
                }
            }
        }


         double [] offspringFitness = new double[size];

        for(int i =0; i< size;i++){
            offspringFitness[i] = offspring.get(i).getFitness();
        }


        Trader [] offspringArr = new Trader[size];

        for(int i = 0; i < size;i++)
            offspringArr[i] = offspring.get(i);

        for(int i = 0; i < size-1; i++){
            for(int j = i+1; j < size; j++){
                if(offspringFitness[j] > offspringFitness[i]){
                    Trader tempTrader = offspringArr[j];
                    double tempOffspringFitness= offspringFitness[j];

                    offspringFitness[j] = offspringFitness[i];
                    offspringFitness[i] = tempOffspringFitness;

                    offspringArr[j] = offspringArr[i];
                    offspringArr[i] = tempTrader;
                }
            }
        }

        Random r = new Random();
        
        
        
        
        
        for(int i = 0; i < genGap;i++)
            offspring.remove(offspringArr[size-1-i]);


        for(int i = 0; i < genGap;i++)
        offspring.add(populationArr[i]);

        return offspring;
    }
}
