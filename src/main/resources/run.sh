#!/usr/bin/env bash
java -Xms64m -Xmx256m -jar ./jars/${project.artifactId}-${project.version}.jar 1-1-2015 `date +'%m-%d-%Y'` ./wells.csv http://webapps.rrc.state.tx.us/CMPL/
mkdir archives
mv ./wells.csv ./archives/wells-`date +'%m-%d-%Y'`.csv
mv ./newWells.csv ./wells.csv