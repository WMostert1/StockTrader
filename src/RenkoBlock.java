/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * RenkoBlock represents a block on a Renko Graph
 */
public class RenkoBlock{
    public boolean type;
    public StockDayUnit dataUnit;

    /**
     * Constructor for initialisation
     * @param type  A solid or open block
     * @param dataUnit Container for information related to a trading day
     */
    public RenkoBlock(boolean type,StockDayUnit dataUnit){
        this.type = type;
        this.dataUnit = dataUnit;
    }
}