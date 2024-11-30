Feature: Portfolio Updater

  Scenario: Initialize and update portfolio
    Given the positions file "src/test/resources/positions.csv" contains:
      | Symbol | PositionSize |
      | AAPL   | 10           |
      | GOOG  | 5            |
    When I run the PortfolioUpdater
    Then the database should be initialized
    And the securities should be inserted
    And the security "AAPL" should be queried
    And the security "GOOG" should be queried
    And the market data provider should start
    And the market prices should be updated