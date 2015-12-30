#!/usr/bin/env bash
#run daily
mkdir -p ~/logs
mv ~/scraper.log ~/logs/
~/run.sh > ~/scraper.log &