#!/bin/bash
PATH=/usr/lib/jvm/jdk1.8.0_71/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games
java -Xms64m -Xmx256m -jar ./jars/oilfield-logix-scraper-1.0-SNAPSHOT.jar `date +'%Y-%m-%d' -d 'last month'` `date +%Y-%m-%d` http://webapps.rrc.state.tx.us/CMPL/
mkdir -p ./archives
mv ./wells.csv ./archives/wells-`date +%m-%d-%Y`.csv
mv ./newWells.csv ./wells.csv