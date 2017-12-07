CS-6367.501-course-project - Fault Localization
----------------
The implementation of software testing, V&amp;V course project

Requirements
----------------
 - Java >= 1.7
 - Maven 3.5
 - Perl >= 5.0.10
 - Git >= 1.9
 - SVN >= 1.8
 - Only on Mac or Linux (Sorry Windows)
 
Defects4J
----------------
#### Setting up Defects4J (From https://github.com/rjust/defects4j)
1. Clone Defects4J:
   
   - `git clone https://github.com/rjust/defects4j`

2. Initialize Defects4J (download the project repositories and external libraries, which are not included in the git repository for size purposes and to avoid redundancies):
   
   - `cd defects4j`
    - `./init.sh`

3. Add Defects4J's executables to your PATH:
    - `export PATH=$PATH:"path2defects4j"/framework/bin`
#### Extracting buggy version of Time
0. Note that you can use buggy versions that I have already extracted by unzipping `time_buggy.zip` OR you definitely have to: 
    
    - `cd PROJECT_PATH`
    - Edit the location you want to save buggy version in  `defect4jcheckout.sh`
    - `./defect4jcheckout.sh`
    
Phosphor 
----------------
#### Setting up Phosphor (From https://github.com/gmu-swe/phosphor)
1. Unzip `phosphor.zip`
2. Initialize 
   
   - `cd phosphor`
    - `mvn verify`
    
Expecting that `./phosphor/Phosphor/target/{jre-inst-implicit, jre-inst-int, jre-inst-obj, Phosphor-0.0.3-SNAPSHOT.jar}`.
Note that because I have modified the original Phosphor to my customized version, to make sure that you get the same experimental result, you must use my version to run my project.
#### Running my example with Phosphor
I have created a simple example (as shown in the report) by reusing a Phosphor's example. To see the results of Methods' output Tainter, please execute `run_examples.sh`
	
   - `cd phosphor-examples`
   - `./run_examples.sh PATH_TO_PHOSPHOR/Phosphor/target/`
    
The expected results:

`Void: edu.utd.Test.<clinit>
Void: edu.utd.Test.<init>
Return: edu.utd.Test.getValue1
Return: edu.utd.Test.getValue2
Taint [lbl=edu.utd.Test.getValue2  deps = []]
Return: edu.utd.Test.getValue3
Taint [lbl=edu.utd.Test.getValue3  deps = []]
Return: edu.utd.Test.getValue4
Return: edu.utd.Test.getValue3
Taint [lbl=null  deps = [edu.utd.Test.getValue1 edu.utd.Test.getValue3 ]]
Return: edu.utd.Test.testMe
Void: edu.utd.Test.main`

Run my project
----------------

0. Clone and build this project (using Maven)

1. Build customized Phosphor jar file
  - `cd PATH_TO_PHOSPHOR/Phosphor`
  - `mvn package`
  
2. Go to `PROJECT_PATH` and configure some options in `configuration/src/main/java/edu/utd/configuration/Config.java`:
  - `DATA=YOUR_LOCATION_TO_TIME_BUGGY`
  - `PHOSPHOR_HOME=YOUR_LOCALTION_TO_PHOSPHOR`

3. Run Defects4J test to extract failing tests. If you USED my buggy versions, you DO NOT need to run Defect4J test because I have run that step, you can se the result in each version in files `./time_buggy/time_bug_X/{all-tests.txt, failing_tests}`. If not, you need to do by the following steps:

 - `cd PROJECT_PATH/shells`
 - Modify the path to buggy versions in `defect4jtest.sh`
 - `./defect4jtest`
 
4. Set up configurable variables in Config.java (`PHOSPHOR=false/true` if you want to run approach 1 or approach 2)

5. Run `edu.utd.preprocess.PreProcess` to update `pom.xml` files in buggy versions: Junit 4.11, Maven Surefire 2.19, and Java Agent

6. Run Maven test for all buggy version to collect test information (if you do not use my buggy versions)
  
  - `cd PROJECT_PATH/shells`
  - Modify the path to buggy versions in `maventest_phosphor.sh`
  - `./maventest_phosphor`

Expecting that: `./time_buggy/time_bug_X/{result_phosphor.txt}`. `result_phosphor.txt` contains all method calls and their direct related methods (at a certain moment).

7. Run class `edu.utd.localization.FaultLocator2` to have final result. Expecting that: `./time_buggy/time_bug_X/{result2.txt}`. `result2.txt` shows:
	 
	 - Each row is the results of a failing test
	 - By collumns, the numbers of method calls, individual methods (the results of the first approach) and the number of methods after I applied the second approach

For example: In the first failing test of the first buggy version, there ar 3077 method calls, and 258 methods executed, and there are 78 methods that actually relate to the failing test.



