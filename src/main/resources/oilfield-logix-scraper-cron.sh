#!/bin/bash
#run daily
PATH=/usr/lib/jvm/jdk1.8.0_71/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games
mkdir -p ./logs
mv ./scraper.log ./logs/scraper-`date +%m-%d-%Y`.log
./run.sh > ./scraper.log &
echo "Attached are the scraper results for `date +%m-%d-%Y`" | mutt -s "Scraper results for `date +%m-%d-%Y`" cbspears@oilfieldlogix.com -a wells.csv