CREATE TABLE ledger (
    day_code text,
    id text,
    plate text,
    booked timestamp,
    released timestamp,
    minute_rate int,
    total int,
    PRIMARY KEY (day_code, id)
);

CREATE TABLE slot (
    plate text PRIMARY KEY,
    booked timestamp,
    ledger_id text
);