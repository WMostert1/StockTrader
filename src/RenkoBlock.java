/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * RenkoBlock represents a block on a Renko Graph
 */
public class RenkoBlock{
    public boolean type; // False -> Downwards Solid Block    True -> Upwards Open Block
    public StockDayUnit dataUnit;

    public RenkoBlock(boolean type,StockDayUnit dataUnit){
        this.type = type;
        this.dataUnit = dataUnit;
    }
}