CREATE KEYSPACE IF NOT EXISTS cryptodatakeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
CREATE TABLE cryptodatakeyspace.crypto_data (
    ticker text,
    timestamp timestamp,
    open double,
    close double,
    high double,
    low double,
    volume double,
    PRIMARY KEY (ticker, timestamp)
);
CREATE TABLE cryptodatakeyspace.average_data (
    ticker text,
    timestamp timestamp,
    average_price double,  -- This could be calculated as the average of open, close, high, and low
    volume double,         -- You may also want to store the volume average
    PRIMARY KEY (ticker, timestamp)
);
CREATE TABLE cryptodatakeyspace.aggregated_data (
    ticker text,
    period_start timestamp,  -- Start of the period (e.g., start of the day/week/month)
    average_price double,
    total_volume double,
    PRIMARY KEY (ticker, period_start)
);
