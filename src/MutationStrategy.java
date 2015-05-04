/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * An abstract class for pluggable mutation strategies
 */
public abstract class MutationStrategy {
    protected double mutationProb;

    /**
     * Constructor for initialisation
     * @param mutationProbability A double value between in the range 0.0 to 1.0
     * */
    public MutationStrategy(double mutationProbability){
        mutationProb = mutationProbability;
    }
    public abstract char [] mutateGene(char [] chromosome);
}
