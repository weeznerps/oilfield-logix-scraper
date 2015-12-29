#!/usr/bin/env bash
java -Xms64m -Xmx256m -jar ./jars/${project.artifactId}-${project.version}.jar 1/1/2015 1/31/2015 02 ./wells.csv ./forms.csv http://webapps.rrc.state.tx.us/CMPL/