#!/usr/bin/env bash
#run daily
mkdir -p ./logs
mv ./scraper.log ./logs/scraper-`date +%m-%d-%Y`.log
./run.sh > ./scraper.log &