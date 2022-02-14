# housie

This is my implementation of the housie assignment for the shared-services role.

You will need maven installed on your machine to build the executable jar

In order to build it go into the root directory, (which this readme should be in). The pom.xml should also be visible in it as well. Then simply run 

mvn clean package

afterward cd into the target/ folder

and run the jar from command line with

java -jar housie-1.0-SNAPSHOT-jar-with-dependencies.jar


Some TODOS:
Need to add edge test cases for defending against hostile inputs
Mock depedency behaviours
