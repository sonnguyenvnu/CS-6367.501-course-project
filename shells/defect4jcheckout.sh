#!/bin/bash
for i in {1..27}
do
   defects4j checkout -p Time -v ${i}b -w /Users/sonnguyen/Workspace/2017/Data_test/time_bug_${i}
done
