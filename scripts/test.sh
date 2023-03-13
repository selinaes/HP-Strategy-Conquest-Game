#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
# Change 1: run cloverAggregateReport
./gradlew cloverAggregateReport || exit 1
scripts/coverage_summary.sh
# Change 2: remove "app/" from this path
cp -r build/reports/clover/html/* /coverage-out/ || exit 1
