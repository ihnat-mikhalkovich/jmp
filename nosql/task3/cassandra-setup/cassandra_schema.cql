CREATE KEYSPACE IF NOT EXISTS expanses_monitor
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 4 };

use expanses_monitor;

CREATE TYPE IF NOT EXISTS expanses_monitor.account (
  email text
);

CREATE TYPE IF NOT EXISTS expanses_monitor.engagement (
  simple_name text,
  product_name text, 
  product_link text, 
  shop_name text, 
  shop_link text, 
  location tuple<double, double>
);

CREATE TYPE IF NOT EXISTS expanses_monitor.money (
  currency text,
  price double,
  measure text,
  amount double
);

CREATE TABLE expanse (
  creation_date timestamp,
  user_account frozen<account>,
  engagement frozen<engagement>,
  engagement_types SET<TEXT>,
  cost frozen<money>,
  period text,
  PRIMARY KEY(user_account, creation_date)
)
WITH CLUSTERING ORDER BY (creation_date DESC);

BEGIN BATCH
  INSERT INTO expanse 
  (creation_date, user_account, engagement, engagement_types, cost, period)
  values 
  (
    '2021-07-04 18:00:00+0000', 
    {email : 'user@test.com'}, 
    {simple_name : 'chocolate', 
      product_name : 'alpin gold', 
      product_link : 'http://...', 
      shop_name : 'prostore', 
      shop_link : 'http://prostore.by', 
      location : (53.84751657227212, 27.47117452214355)
    }, 
    {'shop', 'expanses'}, 
    {currency : 'BYN',
      price : 5.14, 
      measure : 'number', 
      amount : 1
    }, 
    '0 1 * * *'
  );
  INSERT INTO expanse 
  (creation_date, user_account, engagement, engagement_types, cost, period)
  values 
  (
    '2021-07-04 18:00:00+0000', 
    {email : 'user@test.com'}, 
    {simple_name : 'water', 
      product_name : 'vitalur water', 
      product_link : 'http://...', 
      shop_name : 'vitalur', 
      shop_link : 'http://vitalur.by', 
      location : (53.853422294681096, 27.466877342297522)
    }, 
    {'shop', 'expanses'}, 
    {currency : 'BYN',
      price : 4.04, 
      measure : 'number', 
      amount : 2
    }, 
    '* 0 12 ? * 2/3 *'
  );
  INSERT INTO expanse 
  (creation_date, user_account, engagement, engagement_types, cost, period)
  values 
  (
    '2021-07-04 18:00:00+0000', 
    {email : 'user@test.com'}, 
    {simple_name : 'tomato', 
      product_name : 'plum tomato', 
      product_link : 'http://...', 
      shop_name : 'vitalur', 
      shop_link : 'http://vitalur.by', 
      location : (53.853422294681096, 27.466877342297522)
    }, 
    {'shop', 'expanses'}, 
    {currency : 'BYN',
      price : 3.48, 
      measure : 'weight', 
      amount : 1
    }, 
    'NONE'
  );
  INSERT INTO expanse 
  (creation_date, user_account, engagement, engagement_types, cost, period)
  values 
  (
    '2021-07-04 18:00:00+0000', 
    {email : 'user@test.com'}, 
    {simple_name : 'tomato', 
      product_name : 'plum tomato', 
      product_link : 'http://...', 
      shop_name : 'vitalur', 
      shop_link : 'http://vitalur.by', 
      location : (53.853422294681096, 27.466877342297522)
    }, 
    {'shop', 'expanses'}, 
    {currency : 'BYN',
      price : 3.48, 
      measure : 'weight', 
      amount : 1
    }, 
    'NONE'
  );
APPLY BATCH;
