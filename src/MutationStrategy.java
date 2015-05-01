/**
 * Created by wernermostert on 2015/04/30.
 */
public abstract class MutationStrategy {
    protected double mutationProb;
    public MutationStrategy(double mutationProbability){
        mutationProb = mutationProbability;
    }
    public abstract char [] mutateGene(char [] chromosome);
}
