import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wernermostert on 2015/04/29.
 */

/**
 * StockDayUnit is a container for all the relevant information related to a day of trading
 */

public class StockDayUnit {
    public Date date;
    public double closingPrice;
    public double lowPrice;
    public double highPrice;

    /**
     * Constructor to initialise values
     * @param date          Date of the trading day
     * @param closingPrice  Closing price of stock for the trading day
     * @param lowPrice      Lowest price of stock during the trading day
     * @param highPrice     Highest price of stock during the trading day
     */
    public StockDayUnit(String date, int closingPrice, int lowPrice, int highPrice){

        try
        {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
            this.date = formatter.parse(date);
        }
        catch (Exception e)
        {
            System.out.println("ERROR : Inconsistent date format");
        }

        this.closingPrice = closingPrice/100.0;
        this.lowPrice = lowPrice/100.0;
        this.highPrice = highPrice/100.0;
    }
}
