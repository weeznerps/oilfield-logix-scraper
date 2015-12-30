#!/usr/bin/env
# use to rotate logs on a monthly basis so the box doesn't get full
mkdir -p /var/scraper-log-archives
tar cf ~/logs ~/scraper-logs-`date +%m-%Y`.tar
rm -rf ~/logs/*
mv ~/scraper-logs-`date +%m-%Y`.tar /var/scraper-log-archives~/scraper-logs-`date +%m-%Y`.tar