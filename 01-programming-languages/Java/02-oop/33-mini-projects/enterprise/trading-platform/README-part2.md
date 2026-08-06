# Trading Platform — Part 2: Implementation Guide

**[← Part 1: Project Overview & Design](README.md)** | **[Part 3: Tests & Challenges →](README-part3.md)**

---

## Implementation Guide

### Step 1: Implement Order Book

```java
package com.academy.trading.orderbook;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderBook {
    private final String symbol;
    private final ConcurrentSkipListMap<BigDecimal, Deque<Order>> bidLevels;
    private final ConcurrentSkipListMap<BigDecimal, Deque<Order>> askLevels;
    private final List<Trade> recentTrades;
    private final ReentrantLock lock;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.bidLevels = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.askLevels = new ConcurrentSkipListMap<>();
        this.recentTrades = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public void addOrder(Order order) {
        lock.lock();
        try {
            if (order.getSide() == OrderSide.BUY) {
                bidLevels.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>())
                    .add(order);
            } else {
                askLevels.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>())
                    .add(order);
            }
        } finally {
            lock.unlock();
        }
    }

    public List<Trade> matchOrders() {
        lock.lock();
        try {
            List<Trade> trades = new ArrayList<>();
            
            while (!bidLevels.isEmpty() && !askLevels.isEmpty()) {
                BigDecimal bestBid = bidLevels.firstKey();
                BigDecimal bestAsk = askLevels.firstKey();
                
                if (bestBid.compareTo(bestAsk) < 0) {
                    break;
                }
                
                Deque<Order> bids = bidLevels.get(bestBid);
                Deque<Order> asks = askLevels.get(bestAsk);
                
                while (!bids.isEmpty() && !asks.isEmpty()) {
                    Order buyOrder = bids.peek();
                    Order sellOrder = asks.peek();
                    
                    int quantity = Math.min(buyOrder.getRemainingQuantity(), 
                                          sellOrder.getRemainingQuantity());
                    BigDecimal tradePrice = bestAsk;
                    
                    Trade trade = new Trade(buyOrder.getOrderId(), sellOrder.getOrderId(),
                                          quantity, tradePrice);
                    trades.add(trade);
                    
                    buyOrder.execute(quantity, tradePrice);
                    sellOrder.execute(quantity, tradePrice);
                    
                    if (buyOrder.getRemainingQuantity() == 0) {
                        bids.poll();
                    }
                    if (sellOrder.getRemainingQuantity() == 0) {
                        asks.poll();
                    }
                }
                
                if (bids.isEmpty()) bidLevels.remove(bestBid);
                if (asks.isEmpty()) askLevels.remove(bestAsk);
            }
            
            recentTrades.addAll(trades);
            return trades;
        } finally {
            lock.unlock();
        }
    }

    public BigDecimal getBestBid() {
        return bidLevels.isEmpty() ? null : bidLevels.firstKey();
    }

    public BigDecimal getBestAsk() {
        return askLevels.isEmpty() ? null : askLevels.firstKey();
    }
}
```

### Step 2: Implement Trading Strategies

```java
package com.academy.trading.strategy;

public interface TradingStrategy {
    Signal analyze(Stock stock, List<StockQuote> priceHistory);
    String getStrategyName();
}

public enum Signal {
    BUY, SELL, HOLD
}

package com.academy.trading.strategy;

public class MovingAverageStrategy implements TradingStrategy {
    private final int shortPeriod;
    private final int longPeriod;

    public MovingAverageStrategy(int shortPeriod, int longPeriod) {
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
    }

    @Override
    public Signal analyze(Stock stock, List<StockQuote> priceHistory) {
        if (priceHistory.size() < longPeriod) {
            return Signal.HOLD;
        }

        double shortMA = calculateMA(priceHistory, shortPeriod);
        double longMA = calculateMA(priceHistory, longPeriod);
        double previousShortMA = calculateMA(priceHistory.subList(0, priceHistory.size() - 1), shortPeriod);
        double previousLongMA = calculateMA(priceHistory.subList(0, priceHistory.size() - 1), longPeriod);

        if (previousShortMA <= previousLongMA && shortMA > longMA) {
            return Signal.BUY;
        } else if (previousShortMA >= previousLongMA && shortMA < longMA) {
            return Signal.SELL;
        }
        return Signal.HOLD;
    }

    private double calculateMA(List<StockQuote> quotes, int period) {
        return quotes.subList(quotes.size() - period, quotes.size()).stream()
            .mapToDouble(q -> q.getClose().doubleValue())
            .average()
            .orElse(0.0);
    }
}

public class RSIStrategy implements TradingStrategy {
    private final int period;
    private final double overboughtLevel;
    private final double oversoldLevel;

    public RSIStrategy(int period, double overbought, double oversold) {
        this.period = period;
        this.overboughtLevel = overbought;
        this.oversoldLevel = oversold;
    }

    @Override
    public Signal analyze(Stock stock, List<StockQuote> priceHistory) {
        if (priceHistory.size() < period + 1) {
            return Signal.HOLD;
        }

        double rsi = calculateRSI(priceHistory);
        
        if (rsi < oversoldLevel) {
            return Signal.BUY;
        } else if (rsi > overboughtLevel) {
            return Signal.SELL;
        }
        return Signal.HOLD;
    }

    private double calculateRSI(List<StockQuote> quotes) {
        List<Double> changes = new ArrayList<>();
        for (int i = 1; i < quotes.size(); i++) {
            double change = quotes.get(i).getClose().doubleValue() - 
                          quotes.get(i - 1).getClose().doubleValue();
            changes.add(change);
        }

        List<Double> gains = changes.stream().filter(c -> c > 0).collect(Collectors.toList());
        List<Double> losses = changes.stream().filter(c -> c < 0).map(Math::abs).collect(Collectors.toList());

        double avgGain = gains.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double avgLoss = losses.stream().mapToDouble(Double::doubleValue).average().orElse(0.001);

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
}
```

### Step 3: Implement Trading Service

```java
package com.academy.trading.service;

import com.academy.trading.model.*;
import com.academy.trading.command.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TradingService {
    private final OrderBookService orderBookService;
    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final CommandHistory commandHistory;
    private final MarketEventManager eventManager;

    public TradeResult executeTrade(String userId, OrderRequest request) {
        User user = userService.getUser(userId);
        Stock stock = marketService.getStock(request.getSymbol());

        // Validate order
        validateOrder(user, request, stock);

        // Create order
        Order order = Order.create(userId, stock, request);
        
        // Execute command
        TradeCommand command = createCommand(order);
        commandHistory.execute(command);

        // Match orders
        List<Trade> trades = orderBookService.matchOrders(stock.getSymbol());

        // Update portfolio
        for (Trade trade : trades) {
            portfolioService.executeTrade(trade);
        }

        // Notify observers
        eventManager.notifyTradeExecuted(trades);

        return new TradeResult(order.getOrderId(), trades);
    }

    private void validateOrder(User user, OrderRequest request, Stock stock) {
        if (!marketService.isMarketOpen()) {
            throw new MarketClosedException("Market is currently closed");
        }

        BigDecimal orderValue = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        
        if (request.getSide() == OrderSide.BUY) {
            if (user.getWallet().getBalance().compareTo(orderValue) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
        } else {
            Portfolio portfolio = portfolioService.getPortfolio(user.getUserId());
            Holding holding = portfolio.getHolding(request.getSymbol());
            if (holding == null || holding.getQuantity() < request.getQuantity()) {
                throw new InsufficientSharesException("Insufficient shares");
            }
        }
    }
}
```

### Step 4: Implement Portfolio with Thread Safety

```java
package com.academy.trading.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Portfolio {
    private final String portfolioId;
    private final String userId;
    private final ConcurrentHashMap<String, Holding> holdings;
    private final AtomicReference<BigDecimal> cashBalance;
    private final ReadWriteLock lock;

    public Portfolio(String userId, BigDecimal initialBalance) {
        this.portfolioId = UUID.randomUUID().toString();
        this.userId = userId;
        this.holdings = new ConcurrentHashMap<>();
        this.cashBalance = new AtomicReference<>(initialBalance);
        this.lock = new ReentrantReadWriteLock();
    }

    public Holding buyStock(Stock stock, int quantity, BigDecimal price) {
        lock.writeLock().lock();
        try {
            BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
            
            if (cashBalance.get().compareTo(totalCost) < 0) {
                throw new InsufficientFundsException("Insufficient balance");
            }

            cashBalance.updateAndGet(balance -> balance.subtract(totalCost));
            
            return holdings.compute(stock.getSymbol(), (symbol, existing) -> {
                if (existing == null) {
                    return new Holding(stock, quantity, price);
                } else {
                    existing.addShares(quantity, price);
                    return existing;
                }
            });
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BigDecimal sellStock(String symbol, int quantity, BigDecimal price) {
        lock.writeLock().lock();
        try {
            Holding holding = holdings.get(symbol);
            if (holding == null || holding.getQuantity() < quantity) {
                throw new InsufficientSharesException("Insufficient shares");
            }

            BigDecimal proceeds = price.multiply(BigDecimal.valueOf(quantity));
            cashBalance.updateAndGet(balance -> balance.add(proceeds));
            
            holding.removeShares(quantity);
            if (holding.getQuantity() == 0) {
                holdings.remove(symbol);
            }
            
            return proceeds;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BigDecimal getTotalValue(Map<String, BigDecimal> currentPrices) {
        lock.readLock().lock();
        try {
            BigDecimal holdingsValue = holdings.values().stream()
                .map(h -> {
                    BigDecimal currentPrice = currentPrices.get(h.getSymbol());
                    return currentPrice != null ? 
                        currentPrice.multiply(BigDecimal.valueOf(h.getQuantity())) :
                        h.getCurrentValue();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            return cashBalance.get().add(holdingsValue);
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

---

**[← Part 1: Project Overview & Design](README.md)** | **[Part 3: Tests & Challenges →](README-part3.md)**