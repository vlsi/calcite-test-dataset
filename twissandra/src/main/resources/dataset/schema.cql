CREATE KEYSPACE twissandra
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

CREATE TABLE twissandra.users (
    username text PRIMARY KEY,
    password text
);

CREATE TABLE twissandra.friends (
    username text,
    friend text,
    since timestamp,
    PRIMARY KEY (username, friend)
);

CREATE TABLE twissandra.followers (
    username text,
    follower text,
    since timestamp,
    PRIMARY KEY (username, follower)
);

CREATE TABLE twissandra.tweets (
    tweet_id uuid PRIMARY KEY,
    username text,
    body text
);

CREATE TABLE twissandra.userline (
    username text,
    time timeuuid,
    tweet_id uuid,
    PRIMARY KEY (username, time)
) WITH CLUSTERING ORDER BY (time DESC);

CREATE TABLE twissandra.timeline (
    username text,
    time timeuuid,
    tweet_id uuid,
    PRIMARY KEY (username, time)
) WITH CLUSTERING ORDER BY (time DESC);
