title CONSUMER_ADD

set QUEUE=operation.add

mvn install exec:java -Dapp.message.queue.incoming=%QUEUE%
