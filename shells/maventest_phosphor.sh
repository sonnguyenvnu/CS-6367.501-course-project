#!/bin/bash
for i in {1..27}
do
   cd /Users/sonnguyen/Workspace/2017/time_buggy/time_bug_$i
   mvn test > result_phosphor.txt
done
