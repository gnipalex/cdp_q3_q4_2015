title PRODUCER

# message sending interval in ms
set SENDING_INTERVAL=2000

mvn install exec:java -Dapp.messages.sendingInterval=%SENDING_INTERVAL%
