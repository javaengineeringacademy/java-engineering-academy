# Trading Platform

## Project Overview

A Trading Platform that handles stock trading, portfolio management, market data, and trading strategies. This enterprise project introduces the Observer pattern for real-time market updates, the Strategy pattern for trading algorithms, and the Command pattern for trade execution. Students will design a system that handles high-frequency operations with precision.

## Learning Outcomes

- Implement the Observer pattern for real-time market data
- Use the Strategy pattern for trading algorithms
- Apply the Command pattern for trade execution and undo
- Design for financial precision with BigDecimal
- Handle concurrent operations safely
- Implement audit logging for compliance
- Design event sourcing for trade history

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | User registration with KYC verification | Must |
| FR02 | Real-time stock quotes and market data | Must |
| FR03 | Place buy/sell orders (market, limit, stop) | Must |
| FR04 | Portfolio management with holdings tracking | Must |
| FR05 | Trade execution and settlement | Must |
| FR06 | Transaction history and reporting | Must |
| FR07 | Watchlist for stock monitoring | Should |
| FR08 | Technical indicators and charts | Could |
| FR09 | Algorithmic trading support | Could |
| FR10 | Paper trading (simulated) | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Financial precision (no rounding errors) |
| NFR02 | Trade execution < 100ms |
| NFR03 | Real-time updates < 1 second delay |
| NFR04 | Complete audit trail for compliance |
| NFR05 | Thread-safe portfolio operations |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        WebApp[Trading Web App]
    end
    
    subgraph Service Layer
        TradingService[Trading Service]
        PortfolioService[Portfolio Service]
        MarketService[Market Data Service]
        UserService[User Service]
    end
    
    subgraph Core Components
        OrderBook[Order Book]
        MatchingEngine[Matching Engine]
        TradingStrategy[Trading Strategy]
        Portfolio[Portfolio Manager]
    end
    
    subgraph Market Data
        PriceFeed[Price Feed]
        MarketEventManager[Event Manager]
        StockQuote[Stock Quotes]
    end
    
    subgraph Storage Layer
        UserRepository[User Repository]
        OrderRepository[Order Repository]
        TradeRepository[Trade Repository]
        PortfolioRepository[Portfolio Repository]
    end
    
    Main --> TradingService
    WebApp --> TradingService
    TradingService --> OrderBook
    TradingService --> MatchingEngine
    TradingService --> TradingStrategy
    TradingService --> Portfolio
    MarketService --> PriceFeed
    MarketService --> MarketEventManager
    MarketService --> StockQuote
```

## Package Structure

```
trading-platform/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── trading/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── User.java
│                       │   ├── Stock.java
│                       │   ├── Order.java
│                       │   ├── Trade.java
│                       │   ├── Portfolio.java
│                       │   ├── Holding.java
│                       │   ├── StockQuote.java
│                       │   ├── Wallet.java
│                       │   └── enums/
│                       │       ├── OrderSide.java
│                       │       ├── OrderType.java
│                       │       ├── OrderStatus.java
│                       │       └── TradeStatus.java
│                       ├── orderbook/
│                       │   ├── OrderBook.java
│                       │   ├── MatchingEngine.java
│                       │   └── PriceLevel.java
│                       ├── strategy/
│                       │   ├── TradingStrategy.java
│                       │   ├── MovingAverageStrategy.java
│                       │   ├── RSIStrategy.java
│                       │   └── MACDStrategy.java
│                       ├── command/
│                       │   ├── TradeCommand.java
│                       │   ├── BuyCommand.java
│                       │   ├── SellCommand.java
│                       │   └── CommandHistory.java
│                       ├── observer/
│                       │   ├── MarketObserver.java
│                       │   ├── MarketEventManager.java
│                       │   ├── PriceAlertHandler.java
│                       │   └── TradeNotificationHandler.java
│                       ├── service/
│                       │   ├── TradingService.java
│                       │   ├── PortfolioService.java
│                       │   ├── MarketService.java
│                       │   ├── UserService.java
│                       │   └── SettlementService.java
│                       ├── repository/
│                       │   ├── UserRepository.java
│                       │   ├── OrderRepository.java
│                       │   ├── TradeRepository.java
│                       │   └── PortfolioRepository.java
│                       └── exception/
│                           ├── InsufficientFundsException.java
│                           ├── InsufficientSharesException.java
│                           ├── MarketClosedException.java
│                           └── InvalidOrderException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── trading/
                        ├── TradingServiceTest.java
                        ├── OrderBookTest.java
                        ├── PortfolioTest.java
                        └── StrategyTest.java
```

## Class Diagram

```mermaid
classDiagram
    class User {
        -String userId
        -String username
        -String email
        -String kycStatus
        -Wallet wallet
        -Portfolio portfolio
        +User(id, username, email)
        +getUserId() String
        +getWallet() Wallet
        +getPortfolio() Portfolio
        +isKycVerified() boolean
    }
    
    class Stock {
        -String symbol
        -String companyName
        -String sector
        -BigDecimal currentPrice
        -LocalDateTime lastUpdated
        +Stock(symbol, companyName)
        +getSymbol() String
        +getCurrentPrice() BigDecimal
        +updatePrice(BigDecimal price) void
    }
    
    class Order {
        -String orderId
        -String userId
        -Stock stock
        -OrderSide side
        -OrderType type
        -OrderStatus status
        -int quantity
        -BigDecimal price
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +Order(userId, stock, side, type, qty, price)
        +getOrderId() String
        +getStatus() OrderStatus
        +execute(int qty, BigDecimal price) void
        +cancel() void
        +getRemainingQuantity() int
    }
    
    class Trade {
        -String tradeId
        -String buyOrderId
        -String sellOrderId
        -int quantity
        -BigDecimal price
        -LocalDateTime executedAt
        +Trade(buyOrderId, sellOrderId, qty, price)
        +getTradeId() String
        +getPrice() BigDecimal
        +getQuantity() int
    }
    
    class Portfolio {
        -String portfolioId
        -String userId
        -ConcurrentHashMap holdings
        -AtomicReference cashBalance
        -ReadWriteLock lock
        +Portfolio(userId, initialBalance)
        +buyStock(stock, qty, price) Holding
        +sellStock(symbol, qty, price) BigDecimal
        +getTotalValue(prices) BigDecimal
        +getHolding(symbol) Holding
    }
    
    class OrderBook {
        -String symbol
        -ConcurrentSkipListMap bidLevels
        -ConcurrentSkipListMap askLevels
        -List recentTrades
        -ReentrantLock lock
        +OrderBook(symbol)
        +addOrder(Order) void
        +matchOrders() List~Trade~
        +getBestBid() BigDecimal
        +getBestAsk() BigDecimal
        +cancelOrder(orderId) boolean
    }
    
    class TradingStrategy {
        <<interface>>
        +analyze(Stock, List~StockQuote~) Signal
        +getStrategyName() String
    }
    
    class MovingAverageStrategy {
        -int shortPeriod
        -int longPeriod
        +analyze(Stock, List~StockQuote~) Signal
    }
    
    class RSIStrategy {
        -int period
        -double overboughtLevel
        -double oversoldLevel
        +analyze(Stock, List~StockQuote~) Signal
    }
    
    class MarketEventManager {
        -Map observers
        +subscribe(observer) void
        +unsubscribe(observer) void
        +notifyPriceUpdate(Stock) void
        +notifyTradeExecuted(Trade) void
    }
    
    User --> Wallet
    User --> Portfolio
    Portfolio --> Holding
    Order --> Stock
    Order --> OrderSide
    Order --> OrderType
    Order --> OrderStatus
    Trade --> Order
    OrderBook --> Order
    OrderBook --> Trade
    TradingStrategy <|.. MovingAverageStrategy
    TradingStrategy <|.. RSIStrategy
    MarketEventManager --> MarketObserver
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**