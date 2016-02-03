#!/bin/bash
# use to rotate logs on a monthly basis so the box doesn't get full
mkdir -p scraper-log-archives
mkdir -p scraper-results-archives
tar cf ./scraper-logs-`date +%m-%Y`.tar ./logs
tar cf ./scraper-results-`date +%m-%Y`.tar ./archives
rm -rf ./logs/*
rm -rf ./archives/*
mv ./scraper-logs-`date +%m-%Y`.tar ./scraper-log-archives/scraper-logs-`date +%m-%Y`.tar
mv ./scraper-results-`date +%m-%Y`.tar ./scraper-results-archives/scraper-logs-`date +%m-%Y`.tar