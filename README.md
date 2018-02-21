# wumpus_world
Implementation of a knowledge base and an inference engine for the Wumpus world. 

Programming Language : Java

Code Structure:
1. The code is split in to two classes named CheckTrueFalse, LogicalExpressions and TTEntailsAlgorithm.
2. The classes are in the format of .java file extensions.
3. The Class TTEntailsAlgorithm performs the TT Entails algorithm to find out if given statment is true or false based on given Knowledge base and inference. 
4. We load the wumpus rules from wumpus_rules.txt, the knowledge base from kb.txt and the statement file from statement.txt. 
5. Then we perform the TT_entails on the statement from the statement file and negation of the statement.
6. Finally we compare the output of the two entails and store the result in result.txt.

Run Code:
Please follow below steps to run the code:
1. The code is kept in folder: wumpus_world
2. Folder contains CheckTrueFalse.java, LogicalExpressions.java, TTEntailsAlgorithm.java, wumpus_rules.txt, kb.txt and statement.txt. 
3. Run and compile code:
javac CheckTrueFalse.java LogicalExpression.java TTEntailsAlgorithm.java
java CheckTrueFalse [wumpus_rules_file] [additional_knowledge_file] [statement_file]

For example:

java CheckTrueFalse wumpus_rules.txt kb.txt statement.txt
