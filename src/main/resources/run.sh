#!/usr/bin/env bash
java -Xms64m -Xmx256m -jar ./jars/${project.artifactId}-${project.version}.jar `date +'%Y-%m-%d' -d 'last month'` `date +%Y-%m-%d` http://webapps.rrc.state.tx.us/CMPL/
mkdir -p archives
mv ./wells.csv ./archives/wells-`date +%m-%d-%Y`.csv
mv ./newWells.csv ./wells.csv
echo "Attached are the scraper results for `date +%m-%d-%Y`" | mutt -s "Scraper results for `date +%m-%d-%Y`" cbspears@oilfieldlogix.com -a wells.csv
