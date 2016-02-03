Steps to perform:
1) install activemq brocker
http://activemq.apache.org/activemq-5130-release.html

2) start activemq brocker, go to instalation folder and perform
{instal-folder}/./bin/activemq start

3) build and run producer application, go to
	integration-producer-app/

run
	run-producer.bat

4) build and run consumer application
there are 4 types of consumers and each has its own *.bat file

go to 
	integration-consumer-app/

run of one of the
	run-consuming-queue-add.bat
	run-consuming-queue-sub.bat
	run-consuming-queue-mul.bat
	run-consuming-queue-div.bat
