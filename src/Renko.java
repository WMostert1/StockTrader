import java.util.ArrayList;

/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * The Renko class acts as a container for the actual stockData
 * and Renko Blocks (which forms the graph). This class is used
 * by almost everything else in order to access the data
 */
public class Renko {
    public final StockDayUnit [] stockData;
    public final ArrayList<RenkoBlock> blocks;

    /**
     * Populates the blocks and stockData fields - simple initialization
     * @param data An element in data will be a single line in the data file
     */
    public Renko(String[] data){
        blocks = new ArrayList<RenkoBlock>();
        stockData = new StockDayUnit[data.length];
        double boxSize;
        double maxClosingPrice = 0.0;
        double minClosingPrice = 0.0;

        for(int i =0; i < data.length; i ++){
            String [] parts = data[i].split("\t");

            //Populate stockData

                stockData[i] = new StockDayUnit(parts[0], new Integer(parts[1]), new Integer(parts[3]), new Integer(parts[2]));

            //Determine maximum and minimum closing prices
            if(i == 0){
                maxClosingPrice = stockData[0].closingPrice;
                minClosingPrice = stockData[0].closingPrice;
            }else{
                if(maxClosingPrice < stockData[i].closingPrice)
                    maxClosingPrice = stockData[i].closingPrice;
                if(minClosingPrice > stockData[i].closingPrice)
                    minClosingPrice = stockData[i].closingPrice;
            }
        }

        boxSize = Math.round(((maxClosingPrice-minClosingPrice)*0.01)*100.0)/100.0;

        //Building the renko blocks
        double balance = stockData[0].closingPrice;
        for(int i = 1; i < data.length; i++){
            if(stockData[i].closingPrice >= balance + boxSize) {
                balance += boxSize;
                blocks.add(new RenkoBlock(false, stockData[i]));
            }
            else if (stockData[i].closingPrice <= balance - boxSize) {
                balance -= boxSize;
                blocks.add(new RenkoBlock(true, stockData[i]));
            }
        }

    }

    /**
     * Converts the Renko object's Renko blocks to a string representation of solid and open blocks.
     * @return A binary string representation of the renko blocks.
     */
    public String getStringRepresentation(){
        String output = "";
        for(RenkoBlock r : blocks){
            if(!r.type)
                output+= "0";
            else
                output += "1";
        }
        return output;
    }
}
