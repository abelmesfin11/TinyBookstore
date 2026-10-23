# TinyBookstore

A small client/server bookstore built with Java, XML-RPC, and SQLite. The server exposes operations to search books by topic, look up details, and buy a book; clients are provided in both Java and Python.

## Components

- `Server.java` — XML-RPC server exposing the bookstore API
- `BookstoreHandler.java` — handler implementing search, lookup, and buy against `books.db`
- `Client.java` / `Client.py` — Java and Python clients
- `PopulateDB.java` — creates and seeds the SQLite database

## Usage

```bash
make        # compile
make db     # create/seed books.db
make server # start the server
make client # run the Java client (in another terminal)
make pyclient # or run the Python client
```

Dependencies (Apache XML-RPC, SQLite JDBC) are included in `lib/`.
