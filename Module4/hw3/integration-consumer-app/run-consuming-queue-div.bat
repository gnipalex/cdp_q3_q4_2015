title CONSUMER_DIV

set QUEUE=operation.div

mvn install exec:java -Dapp.message.queue.incoming=%QUEUE%
