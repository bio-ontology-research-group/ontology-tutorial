export JAVA_OPTS="-Xmx12G";
source activate java_env;
jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='*' --port=8888 --no-browser

