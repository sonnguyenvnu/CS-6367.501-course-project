#!/bin/bash
for i in {1..27}
do
   cd /Users/sonnguyen/Workspace/2017/Data/time_bug_$i
   mvn test > result.txt
done
