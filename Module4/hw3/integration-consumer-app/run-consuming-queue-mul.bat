title CONSUMER_MUL

set QUEUE=operation.mul

mvn install exec:java -Dapp.message.queue.incoming=%QUEUE%
