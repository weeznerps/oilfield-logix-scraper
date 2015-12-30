#!/usr/bin/env bash
java -Xms64m -Xmx256m -jar ./jars/${project.artifactId}-${project.version}.jar 2015-12-20 `date +%Y-%m-%d` http://webapps.rrc.state.tx.us/CMPL/
mkdir -p archives
mv ./wells.csv ./archives/wells-`date +%m-%d-%Y`.csv
mv ./newWells.csv ./wells.csv