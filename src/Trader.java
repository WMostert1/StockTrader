import java.util.Random;

/**
 * Created by wernermostert on 2015/04/29.
 */
public class Trader {
    private double funds;
    private int shares;
    private char [] choices;
    private Renko renkoData;
    public double fitness;

    int n;

    public Trader(Renko r){
        funds = 100000;
        shares = 0;
        n = 5;
        choices = new char[(int)Math.pow(2.0,5.0)];
        for(int i = 0; i < choices.length; i ++){
            choices[i] = getRandomChoice();
        }
        renkoData = r;
        fitness = -1.0;
    }

    public Trader(double funds,int n, Renko r){
        this.funds = funds;
        shares = 0;
        this.n = n;
        choices = new char[(int)Math.pow(2.0,5.0)];
        for(int i = 0; i < choices.length; i ++){
            choices[i] = getRandomChoice();
        }
        renkoData = r;
        fitness = -1.0;
    }

    public Trader(char [] choices, Renko r){
        funds = 100000;
        shares = 0;
        n = 5;
        this.choices = choices;
        renkoData = r;
        fitness = -1.0;
    }

    public Trader(double funds,int n,char [] choices, Renko r){
        this.funds = funds;
        shares = 0;
        this.n = n;
        this.choices = choices;
        renkoData = r;
        fitness = -1.0;
    }

    private char getRandomChoice(){
        char [] choices = {'B','S','H'};
        Random r = new Random();
        int c = r.nextInt(choices.length);
        return choices[c];
    }

    public char decide(boolean [] renkoData){
        int decimalValue = 0;
        for(int i = 0; i < renkoData.length; i++){
            if(renkoData[i])
                decimalValue += (int)Math.pow(2.0,(renkoData.length-1)-i);
        }
        return choices[decimalValue];
    }

    private void run(){
        int renkoBlockCounter = 4;
        RenkoBlock currentRenkoBlock = renkoData.blocks.get(renkoBlockCounter);

        int firstTradingDay = 0;
        while(!renkoData.stockData[firstTradingDay].equals(currentRenkoBlock.dataUnit)){
            firstTradingDay++;
        }



        for(int i = firstTradingDay+1; i < renkoData.stockData.length; i++) {
            StockDayUnit currentDay = renkoData.stockData[i];

            //Adjust current Renko Block
       if(currentDay.date.compareTo(currentRenkoBlock.dataUnit.date) > 0){
           currentRenkoBlock = renkoData.blocks.get(++renkoBlockCounter);
       }

            boolean[] pattern = new boolean[n];
            for (int j = 0; j < n; j++) {
                pattern[j] = renkoData.blocks.get(renkoBlockCounter - (n - 1) + j).type;
            }
            char decision = decide(pattern);

            switch (decision) {
                case 'S':
                    sell(currentDay);
                    break;
                case 'B':
                    buy(currentDay);
                    break;
                case 'H':
                    break;
            }
        }
    }

    public void buy(StockDayUnit dataUnit){

        double sharePrice = dataUnit.highPrice+dataUnit.lowPrice/2.0;
        int numShares = (int)(funds / sharePrice);

        double tradeAmount = 0.0;
        double feeTotal = 0.0;
        do {
            tradeAmount = sharePrice * numShares;
            //Fees
            double STT = tradeAmount * 0.0025;

            double brokerageFee = tradeAmount * 0.005;
            double minimumBrokerageFee = 70.0;
            if (brokerageFee < minimumBrokerageFee) brokerageFee = minimumBrokerageFee;

            final double STRATE_TAX = 11.58;
            double IPL = 0.000002 * tradeAmount;
            double VAT = 0.14 * (STT + brokerageFee + STRATE_TAX + IPL);

            feeTotal = STT + brokerageFee + STRATE_TAX + IPL + VAT;

            if(tradeAmount + feeTotal > funds) numShares--;
            if(numShares == 0) return;

        }while(tradeAmount + feeTotal > funds);

        double total = Math.round((feeTotal + tradeAmount)*100.0)/100.0;

        shares += numShares;
        funds -= total;


    }

    public void sell(StockDayUnit dataUnit){
        if(shares != 0) {
            double sharePrice = dataUnit.highPrice + dataUnit.lowPrice / 2.0;
            int numShares = shares;

            double tradeAmount = sharePrice * numShares;
            //Fees

            double brokerageFee = tradeAmount * 0.005;
            double minimumBrokerageFee = 70.0;
            if (brokerageFee < minimumBrokerageFee) brokerageFee = minimumBrokerageFee;

            final double STRATE_TAX = 11.58;
            double IPL = 0.000002 * tradeAmount;
            double VAT = 0.14 * (brokerageFee + STRATE_TAX + IPL);

            double feeTotal = brokerageFee + STRATE_TAX + IPL + VAT;

            double total = Math.round((tradeAmount - feeTotal)*100.0)/100.0;


            shares -= numShares;



            funds += total;

        }
    }

    public double getFitness(){
        if(fitness == -1.0) {
            run();
            double fitness = Math.round((funds + (shares * renkoData.stockData[renkoData.stockData.length - 1].closingPrice)) * 100.0) / 100.0;

            if (fitness < 0) {
                boolean t = true;
            }
            resetTrader();
            this.fitness = fitness;
        }
        return fitness;
    }

    public char [] getGenotype(){
        return choices;
    }

    private void resetTrader(){
        funds = 100000;
        shares = 0;
    }

    public void setGenotype(char [] geno){
        choices = geno;
        fitness = -1.0;
    }

    public Renko getRenkoData(){
        return renkoData;
    }

}
