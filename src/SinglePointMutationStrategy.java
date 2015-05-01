import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */
public class SinglePointMutationStrategy extends MutationStrategy {
    @Override
    public char[] mutateGene(char [] chromosome) {
        Random r = new Random();
        double r1 = r.nextDouble();
        if(r1 <= mutationProb){
            for(int k =0; k < 3;k++) {
                while (true) {
                    char[] possibleGenes = {'B', 'H', 'S'};
                    int i = r.nextInt(possibleGenes.length);
                    int c = r.nextInt(chromosome.length);
                    if (chromosome[c] != possibleGenes[i]) {
                        chromosome[c] = possibleGenes[i];
                        break;
                    }
                }
            }
            return chromosome;
        }
        return chromosome;
    }

    public SinglePointMutationStrategy(double mutationProbability){
        super(mutationProbability);
    }
}
