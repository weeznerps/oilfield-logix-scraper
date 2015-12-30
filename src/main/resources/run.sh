#!/usr/bin/env bash
java -Xms64m -Xmx256m -jar ./jars/${project.artifactId}-${project.version}.jar 2015-12-19 `date +%m-%d-%Y` http://webapps.rrc.state.tx.us/CMPL/
mkdir -p archives
mv ./wells.csv ./results-archives/wells-`date +%m-%d-%Y`.csv
mv ./newWells.csv ./wells.csv