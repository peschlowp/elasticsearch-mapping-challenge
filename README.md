elasticsearch-mapping-challenge
===============================

Test your Elasticsearch mapping skills and satisfy the requirements specified by a unit test suite!

### Scenario

You will implement search features for an e-mail inbox. The project provides domain objects for e-mails, search queries, and search results, as well as a search facade interface defined on these objects. Your task is to implement the search facade, backing its indexing and querying capabilities by Elasticsearch and the Java client. A unit test suite specifies the search features to implement and serves to test and check your implementation.

### Setup

First of all, clone this repository. Then, in the repository root directory, run

`mvn clean compile`

to make sure everything compiles fine. Run 

`mvn test`

to execute the unit tests - some of which will fail, but that's the challenge! Start coding and re-run the tests. When all tests are green, you are done.

### Using Eclipse

For Eclipse users, just run

`mvn eclipse:eclipse`

to create an Eclipse project. In Eclipse, just import the project as a Java project and enjoy your usual working environment.
