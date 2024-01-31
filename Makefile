compile:
	javac Server.java
	javac Client.java

server:
	java Server

client:
	java Client

clean:
	rm -rf *.class