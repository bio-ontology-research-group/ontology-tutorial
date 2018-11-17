#!/usr/bin/env bash
export JAVA_OPTS="-Xmx12G"
source activate java_env
jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='0.0.0.0' --port=8888 --no-browser

