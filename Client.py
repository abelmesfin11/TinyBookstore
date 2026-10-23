import xmlrpc.client, sys

server = xmlrpc.client.ServerProxy("http://" + sys.argv[1] + ":8888")

for line in sys.stdin:
    parts = line.split()
    if not parts:
        continue

    if parts[0] == "search":
        topic = " ".join(parts[1:])
        for book in server.bookstore.search(topic):
            print(book["id"], " ", book["title"])

    elif parts[0] == "lookup":
        book = server.bookstore.lookup(int(parts[1]))
        if not book:
            print("no such item:", parts[1])
        else:
            print(book["title"], " $" + str(book["cost"]), " topic:", book["topic"],
                  " stock:", book["stock"])

    elif parts[0] == "buy":
        bought = server.bookstore.buy(int(parts[1]))
        print("out of stock" if bought == -1 else "bought " + str(bought))

    else:
        print("commands: search <topic> | lookup <id> | buy <id>")
