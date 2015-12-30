#!/usr/bin/env
# use to rotate logs on a monthly basis so the box doesn't get full
mkdir -p /var/scraper-log-archives
mkdir -p /var/scraper-results-archives
tar cf ~/logs ~/scraper-logs-`date +%m-%Y`.tar
tar cf ~/archives ~/scraper-results-`date +%m-%Y`.tar
rm -rf ~/logs/*
rm -rf ~/archives/*
mv ~/scraper-logs-`date +%m-%Y`.tar /var/scraper-log-archives/scraper-logs-`date +%m-%Y`.tar
mv ~/scraper-results-`date +%m-%Y`.tar /var/scraper-results-archives/scraper-logs-`date +%m-%Y`.tar