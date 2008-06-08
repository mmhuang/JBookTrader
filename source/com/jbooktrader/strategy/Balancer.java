package com.jbooktrader.strategy;

import com.ib.client.*;
import com.jbooktrader.indicator.balance.*;
import com.jbooktrader.indicator.price.*;
import com.jbooktrader.platform.bar.*;
import com.jbooktrader.platform.commission.*;
import com.jbooktrader.platform.indicator.*;
import com.jbooktrader.platform.marketdepth.*;
import com.jbooktrader.platform.model.*;
import com.jbooktrader.platform.optimizer.*;
import com.jbooktrader.platform.schedule.*;
import com.jbooktrader.platform.strategy.*;
import com.jbooktrader.platform.util.*;

/**
 *
 */
public class Balancer extends Strategy {

    // Technical indicators
    private final Indicator depthBalanceInd, rsiInd;

    // Strategy parameters names
    private static final String EMA_PERIOD = "EmaPeriod";
    private static final String PERIOD = "Period";
    private static final String RSI_ENTRY = "RSIEntry";
    private static final String ENTRY = "Entry";

    // Strategy parameters values
    private final int entry, rsiEntry;


    public Balancer(StrategyParams optimizationParams, MarketBook marketBook, PriceHistory priceHistory) throws JBookTraderException {
        super(optimizationParams, marketBook, priceHistory);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("ES", "GLOBEX");
        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("9:20", "16:10", "America/New_York");
        int multiplier = 50;// contract multiplier
        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);

        entry = getParam(ENTRY);
        rsiEntry = getParam(RSI_ENTRY);

        // Create technical indicators
        rsiInd = new PriceRSI(priceHistory, getParam(PERIOD));
        depthBalanceInd = new BalanceEMA(marketBook, getParam(EMA_PERIOD));
        addIndicator("PriceRSI", rsiInd);
        addIndicator("Depth Balance", depthBalanceInd);

    }

    /**
     * Adds parameters to strategy. Each parameter must have 5 values:
     * name: identifier
     * min, max, step: range for optimizer
     * value: used in backtesting and trading
     */
    @Override
    public void setParams() {
        addParam(ENTRY, 26, 26, 5, 26);
        addParam(RSI_ENTRY, 0, 45, 5, 10);
        addParam(EMA_PERIOD, 2, 15, 1, 10);
        addParam(PERIOD, 10, 10, 5, 10);
    }

    /**
     * This method is invoked by the framework when an order book changes and the technical
     * indicators are recalculated. This is where the strategy itself should be defined.
     */
    @Override
    public void onBookChange() {
        double rsi = rsiInd.getValue() - 50;
        double depthBalance = depthBalanceInd.getValue();
        if (depthBalance >= entry && rsi <= -rsiEntry) {
            setPosition(1);
        } else if (depthBalance <= -entry && rsi >= rsiEntry) {
            setPosition(-1);
        }

    }
}
