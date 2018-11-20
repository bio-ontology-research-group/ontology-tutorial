
# Tutorial: Ontologies for biomedical data analysis

Ontologies have long provided a core foundation in the organization of biomedical entities, their attributes, and their relationships. With over 500 biomedical ontologies currently available there are a number of new and exciting new opportunities emerging in using ontologies for large scale data sharing and data analysis. This tutorial will help you understand what ontologies are and how they are being used in computational biology and bioinformatics. 

*Intended audience and level*: The tutorial will be of interest to any researcher who will use or produce large structured datasets in computational biology. The tutorial will be at an intermediate level and will describe current research directions and challenges. A particular focus will be given on the use of ontologies to compute semantic similarity, and the use of ontologies in machine learning.

# Learning objectives

This is an intermediate-level course to ontologies and ontology-based data analysis in bioinformatics. In this tutorial, participants will learn:
 * what ontologies are and where to find them (briefly)
 * how to understand and use ontology semantics through automated reasoning
 * how to measure semantic similarity
 * how to combine ontologies and deep learning
 * how to incorporate ontologies and semantic similarity measures in bioinformatics analyses

# Before the tutorial (important)

The tutorial will contain a hands-on part. If you want to
participate (instead of just watching the presentation), please install the required software locally or use our Doker image. 

## Local installation on your computer:

Download
and install Jupyter Notebook (http://jupyter.org/) with a SciJava kernel (follow instructions [here](https://github.com/scijava/scijava-jupyter-kernel)), and _run_ the first cell in https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.ipynb (on Jupyter). This will download the required dependencies (OWLAPI, ELK,
SML) which are quite large. You must also download our data package from [here](http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz) and for the last part of the tutorial some vectors from [here](http://jagannath.pdn.cam.ac.uk/tutorial/phenome-vec-small.txt.gz).

It is fine to skip this step and still follow the tutorial, but if you
want to play with the methods yourself, and go away with some running code examples that you can build on, downloading and running the code is necessary.

Detailed instructions:
 * Download Jupyter Notebook from http://jupyter.org/ and install
 * Install the SciJava Jupyter kernel following instructions on https://github.com/scijava/scijava-jupyter-kernel
 * Download the [Jupyter Notebook file](https://github.com/bio-ontology-research-group/ontology-tutorial/blob/master/ontology-analysis.ipynb) ([direct download](https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.ipynb)): and store on your disk.
 * Run Jupyter Notebook using `jupyter notebook`
   * This will open a web browser, or browser window, with the Jupyter environment.
   * We have received some reports of memory problems. To resolve them, try running `JAVA_OPTS="-Xmx12G" jupyter notebook` or `_JAVA_OPTIONS="-Xmx12G" jupyter notebook` to set allowed memory for jupyter to 12GB (adjust for your particular environment).
 * In your browser, find the ontology-analysis.ipynb file and open
 * In the first Groovy box, press Shift+Return
   * Depending on your Internet connection, this may take some time!
   * This code will download the libraries necessary to run the remaining code.
 * Download the [data package](http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz) and store on your disk. Unzip it with gunzip: `gunzip ontology-tutorial.tar.gz`.
 * Download the [ontology embeddings](http://jagannath.pdn.cam.ac.uk/tutorial/phenome-vec-small.txt.gz) and store on your disk (ideally in the `/misc` subdirectory.

## Using the Doker image:

* Download and install [Docker](https://www.docker.com).
* Assign at least 8 GB (better 10 or 12GB) to the Docker engine after the installation. (the following is required in the Mac OSX version: Docker -> Preferences -> Advanced -> Memory followed by `Apply & Restart`)
* Pull the tutorial image: altermeister/bio-ontology-ontology-tutorial-docker:latest (in Linux/OSX using a terminal: `docker pull altermeister/bio-ontology-ontology-tutorial-docker:latest`).
* Run the image and jump directly into the Jupyter notebook: `docker run -i -t -p 8888:8888  altermeister/bio-ontology-ontology-tutorial-docker /bin/bash -c "source activate java_env && export JAVA_OPTS=-Xmx12G && _JAVA_OPTIONS=-Xmx12G jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='0.0.0.0' --port=8888 --no-browser"`
* After the notebook started, copy the address and the token and paste it into you web browser. The URL should look like the following: `127.0.0.1:8888/?token=f14e6316a48016b2cf4cbed5dd7b89d9dc524da49f553dc7`.
* All is now set up. There is no need to download and install any additional packages or data files.  Just step through the Groovy boxes. 



# Schedule

1. General overview: what are ontologies, where to find them (ontology portals), how they are used (for annotation)
2. Semantic Web: basic technologies underlying ontologies; understanding ontologies through OWL
3. Ontologies and graphs: how to go from ontologies to graphs and back (preliminary step for computing semantic similarity)
4. Semantic similarity: computing similarity between classes, sets of classes, and between biological entities (genes, diseases, drugs)
5. Machine learning and ontologies: using deep learning to encode knowledge graphs, ontologies, and connections between ontologies
4. Applications: how to apply the methods for biomedical data analysis: finding protein-protein interaction, prioritize disease genes, and more

# Reading materials

 * Introductory review papers:
   * [Semantic Similarity in Biomedical Ontologies](http://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1000443)
   * [The OBO Foundry: coordinated evolution of ontologies to support biomedical data integration](http://www.nature.com/nbt/journal/v25/n11/full/nbt1346.html)
   * [A Review of Relational Machine Learning for Knowledge Graphs](https://arxiv.org/abs/1503.00759)
   * [The role of ontologies in biological and biomedical research: a functional perspective](https://academic.oup.com/bib/article-lookup/doi/10.1093/bib/bbv011) (shameless self-promotion warning)
   * [Evaluation of research in biomedical ontologies](https://academic.oup.com/bib/article-lookup/doi/10.1093/bib/bbs053) (shameless self-promotion warning)

 * Technical documentation and tutorials:
   * The OWL2 Primer https://www.w3.org/TR/owl2-primer/
   
 * Books
   * [Semantic Similarity from Natural Language and Ontology Analysis](http://www.morganclaypool.com/doi/10.2200/S00639ED1V01Y201504HLT027)
   * [Data Mining with Ontologies: Implementations, Findings, and Frameworks](https://www.igi-global.com/book/data-mining-ontologies/234)

# Resources

 * Ontology portals:
   * [OBO Library](http://www.obofoundry.org/)
   * [BioPortal](https://bioportal.bioontology.org/)
   * [AberOWL](http://aber-owl.net)
   * [Ontology Lookup Service](https://www.ebi.ac.uk/ols/)
   * [OntoBee](http://www.ontobee.org/)
 * Ontology tools:
   * [Protégé](http://protege.stanford.edu/)
 * Libraries:
   * [OWL API](https://github.com/owlcs/owlapi)
   * [ELK Reasoner](https://github.com/liveontologies/elk-reasoner)
   * [Semantic Measures Library](http://www.semantic-measures-library.org/) (and the [paper](https://academic.oup.com/bioinformatics/article-lookup/doi/10.1093/bioinformatics/btt581))
 * Knowledge graph embeddings and trainable similarity measures:
   * https://github.com/bio-ontology-research-group/walking-rdf-and-owl
   * [Onto2Vec](https://github.com/bio-ontology-research-group/onto2vec/)
   * [OPA2Vec](https://github.com/bio-ontology-research-group/opa2vec/)
   * https://github.com/thunlp/KB2E


# Slides
Slides will be updated on demand. The latest version (source and PDF) are in the (/slides/) folder.
 
# Questions and Requests

You can use the [Issue Tracker](https://github.com/bio-ontology-research-group/ontology-tutorial/issues) for questions and requests.

# Contributors

The set of slides has been developed by Michel Dumontier (part 1) and Robert Hoehndorf (part 2).

# License

The tutorial materials are under a CC-BY license.
