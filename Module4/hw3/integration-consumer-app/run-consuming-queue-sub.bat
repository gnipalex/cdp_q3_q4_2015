title CONSUMER_SUB

set QUEUE=operation.sub

mvn install exec:java -Dapp.message.queue.incoming=%QUEUE%
