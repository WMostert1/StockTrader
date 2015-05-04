import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * Single point mutation mutates a single gene to a different gene
 * in the chromosome.
 */
public class SinglePointMutationStrategy extends MutationStrategy {
    /**
     * Depending on the mutation rate, the original chromosome is mutated.
     * @param chromosome Original chromosome of Trader
     * @return Mutated or original chromosome
     */
    @Override
    public char[] mutateGene(char [] chromosome) {
        Random r = new Random();
        for(int i = 0;i < chromosome.length; i++){
            double r1 = r.nextDouble();
            if (r1 <= mutationProb) {
                while (true) {
                    char[] possibleGenes = {'B', 'H', 'S'};
                    int c = r.nextInt(possibleGenes.length);
                    if (chromosome[i] != possibleGenes[c]) {
                        chromosome[i] = possibleGenes[c];
                        break;
                    }
                }
            }
        }
        return chromosome;
    }

    /**
     * Constructor to initialise the mutationProbability
     * @param mutationProbability A value between 0.0 and 1.0
     */
    public SinglePointMutationStrategy(double mutationProbability){
        super(mutationProbability);
    }
}
