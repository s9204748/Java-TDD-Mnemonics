Kevin Flattery, 5/7/15; Aconex Java Test

I chose the 1800 Challenge as it appeared more straightforward upon 
first read of the two options. This is not to say I don't like a challenge but
favour pragmatism.

After a conceptualisation of two main classes (converter and 
dictionary) I created those two test classes & started to evolve
the setUp and method names of behaviour to test for (based on 
business rules). 

I created the related implementation classes (initially with 
IBM IIT (Ecplise), but didn't implement accessible/service methods and 
delegates until related (failing) tests were incrementally written.

A results/filter class evolved as it became
apparent that various rules on return results could either be handled by 
additional code complexity or filtering unwanted permutations with an
Expression Matcher.

Note: I could not intepret the sentence:

'The dictionary is expected to have one word per line.'

as I understand the dictionary as mapping a single digit to multiple 
characters (not words); hence have implmented the model as per the 
tabular layout in the PDF instructions.

I converted to project to command line Maven and allowed for easy
switching between IDE and MVN goals, ultimately for distribution.

Assumption that Maven is installed! JUnit and other supporting
libraries will be downloaded during goal execution (requires network)

Generate Cobertura code coverage generated with:
mvn site
and available from target/site/cobertura/index.html

Note: coverage metrics for NumberToLetterConverter includes the main 
method and constructors (which are not unit tested), hence lower coverage score.
---------------
mvn clean compile test package
cd target

Deployed on Windows 8.1; see convertExample.bat (copy to target 
folder) to make it easier to run. 

Note: file URL protocol is required for runtime JAR to access external files