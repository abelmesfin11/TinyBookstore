CP = .:lib/*

SOURCES = BookstoreHandler.java Server.java Client.java PopulateDB.java
CLASSES = $(SOURCES:.java=.class)

all: $(CLASSES)

%.class: %.java
	javac -cp "$(CP)" $(SOURCES)

# create/refresh books.db
db: PopulateDB.class
	java -cp "$(CP)" PopulateDB

server: Server.class
	java -cp "$(CP)" Server

client: Client.class
	java -cp "$(CP)" Client

pyclient:
	python3 Client.py localhost

clean:
	rm -f *.class books.db

.PHONY: all db server client pyclient clean
