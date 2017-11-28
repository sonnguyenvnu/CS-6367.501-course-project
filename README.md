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
Only on Mac or Linux (Sorry Windows)
    - `cd PROJECT_PATH`
Edit the location you want to save buggy version in  `defect4jcheckout.sh`
    - `./defect4jcheckout.sh`
    
Phosphor 
----------------
#### Setting up Phosphor (From https://github.com/gmu-swe/phosphor)
1. Clone Phosphor
    - Option 1: `git clone https://github.com/gmu-swe/phosphor.git`
    --> Then copy my code to the src folder
    - Option 2:

2. Initialize 
    - `cd phosphor`
    - `mvn verify`
Expecting that `.phosphor/Phosphor/target/{jre-inst-implicit, jre-inst-int, jre-inst-obj}`
#### Running my example with Phosphor
    - `./run_examples.sh PATH_TO_PHOSPHOR/Phosphor/target/`
Updating...

Run my project
----------------
1. Build customized Phosphor jar file
  - `cd PATH_TO_PHOSPHOR/Phosphor`
  - `mvn package`
2. Go to `PROJECT_PATH` and configure the buggy project's location 
3. Go to `PROJECT_PATH` and build the MethodCollector 
  - `cd method-coverage`
  - `mvn install`
Expecting that:  `./method-coverage/target/method-coverage-0.1-SNAPSHOT.jar`
3. Run Defects4J test to extract failing tests:
 - `cd PROJECT_PATH`
 - `./defect4jtest`
4. Set up configurable variables in Config.java (`PHOSPHOR=false/true` if you want to run approach 1 or approach 2)
5. Run PreProcess to update `pom.xml` files: Junit 4.11, Maven Surefire 2.19, and Java Agent
6. Edit `maventest.sh` and run Maven test for all buggy version to collect test information
  - `cd PROJECT_PATH`
  - `./maventest`
The test result: `time_bug_x\result.txt`
7. Run `FaultLocator.java` to get final result `time_bug_x\result_{1/2}.txt` (approach 1 or approach 2)
Updating...



