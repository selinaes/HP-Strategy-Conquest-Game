#!/bin/bash
mkdir coverage
docker run --rm -v `pwd`/coverage:/coverage-out  citest scripts/test.sh
# docker run -it --rm -v `pwd`/coverage:/coverage-out citest bash
