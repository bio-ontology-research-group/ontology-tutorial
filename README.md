
# Ontology Tutorial at ISMB 2017

Ontologies have long provided a core foundation in the organization of biomedical entities, their attributes, and their relationships. With over 500 biomedical ontologies currently available there are a number of new and exciting new opportunities emerging in using ontologies for large scale data sharing and data analysis. This tutorial will help you understand what ontologies are and how they are being used in computational biology and bioinformatics. 

*Intended audience and level*: The tutorial will be of interest to any researcher who will use or produce large structured datasets in computational biology. The tutorial will be at an introductory level, but will also describe current research directions and challenges that will be of broad interest to researchers in computational biology.

# Learning objectives

This is an introductory-level course to ontologies and ontology-based data analysis in bioinformatics. In this tutorial, participants will learn:
 * what ontologies are and where to find them
 * how to understand and use ontology semantics through automated reasoning
 * how to measure semantic similarity
 * how to incorporate ontologies and semantic similarity measures in bioinformatics analyses

# Before the tutorial (important)

The tutorial will contain a hands-on part. If you want to
participate (instead of just watching the presentation), please download
and install Jupyter Notebook (http://jupyter.org/) with a SciJava kernel (follow instructions [here](https://github.com/scijava/scijava-jupyter-kernel)) or Beaker Notebook (http://beakernotebook.com/) and _run_ the first cell in either https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.ipynb (on Jupyter) or https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.bkr (on Beaker). This will download the required dependencies (OWLAPI, ELK,
SML) which are quite large. ~~It also downloads a 400MB OWL file which we will use for demonstration purposes. Some basic Java/Groovy knowledge is beneficial but not required.~~ You must also download our data package from [here](http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz).

It is fine to skip this step and still follow the tutorial, but if you
want to play with the methods yourself, and go away with some running code examples that you can build on, downloading and running the code
is necessary.

Detailed instructions (Jupyter Notebook):
 * Download Jupyter Notebook from http://jupyter.org/ and install
 * Install the SciJava Jupyter kernel following instructions on https://github.com/scijava/scijava-jupyter-kernel
 * Download the [Jupyter Notebook file](https://github.com/bio-ontology-research-group/ontology-tutorial/blob/master/ontology-analysis.ipynb) ([direct download](https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.ipynb)): and store on your disk.
 * Run Jupyter Notebook using `jupyter notebook`
   * This will open a web browser, or browser window, with the Jupyter environment.
 * In your browser, find the ontology-analysis.ipynb file and open
 * In the first Groovy box, press Shift+Return
   * Depending on your Internet connection, this may take some time!
   * This code will download the libraries necessary to run the remaining code.
 * Download the [data package](http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz) and store on your disk


Detailed instructions (Beaker Notebook):
 * Download Beaker Notebook from http://beakernotebook.com/ (for Win, Mac, or GNU/Linux)
 * Install Beaker Notebook; follow the instructions provided and download the required dependencies
   * Alternatively, download the Beaker Notebook Docker image from https://hub.docker.com/r/beakernotebook/beaker/ or using `docker pull beakernotebook/beaker`
 * Download the [ontology-analysis.bkr](https://github.com/bio-ontology-research-group/ontology-tutorial/raw/master/ontology-analysis.bkr) file and store on your disk.
 * Run Beaker Notebook using `beaker.command`
   * This will open a web browser, or browser window, with the Beaker environment.
 * In your browser, File -> Open (.bkr) and open the file `ontology-analysis.bkr` that you downloaded
 * Below the first Groovy box, click on `Run`
   * Depending on your Internet connection, this may take some time!
   * This code will download the libraries necessary to run the remaining code.
 * Download the [data package](http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz) and store on your disk
   

# Schedule

1. Introduction: ontologies and their role in computational biology
2. Semantic Web: basic technologies underlying ontologies; understanding ontologies through OWL
3. Ontology-based analysis: semantic similarity and unsupervised machine learning methods
4. Hands-on: demonstration of some basic concepts

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

# Slides

Draft slides are in this repository. We will link to the final slides before the tutorial.

# Questions and Requests

You can use the [Issue Tracker](https://github.com/bio-ontology-research-group/ontology-tutorial/issues) for questions and requests.

# Organizers
Michel Dumontier is a Distinguished Professor of Data Science at Maastricht University. His research focuses on the development of computational methods for scalable integration and reproducible analysis of FAIR (Findable, Accessible, Interoperable and Reusable) data across scales - from molecules, tissues, organs, individuals, populations to the environment. His group combines semantic web technologies with effective indexing, machine learning and network analysis for drug discovery and personalized medicine. Dr. Dumontier leads a new inter-faculty Institute for Data Science at Maastricht University with a focus on accelerating discovery science, empowering communities, and improving health and well being. He is the editor-in-chief for the IOS press journal Data Science and an associate editor for the IOS press journal Semantic Web. He is the scientific director for Bio2RDF, an open source project to generate Linked Data for the Life Sciences and is a technical lead for the FAIR (Findable, Accessible, Interoperable, Re-usable) data initiative. He has published over 125 articles in top rated journals and international conferences. He is internationally recognized for his contributions in bioinformatics, biomedical informatics, and semantic technologies as evidenced by awards, keynote talks at international conferences, and collaborations on international projects.

[Robert Hoehndorf](https://www.kaust.edu.sa/en/study/faculty/robert-hoehndorf) is an Assistant Professor in Computer Science at [King Abdullah University of Science and Technology](https://www.kaust.edu.sa/en) in Thuwal. His [research](https://borg.kaust.edu.sa/) focuses on the applications of ontologies in biology and biomedicine, with a particular emphasis on integrating and analyzing heterogeneous, multimodal data. Dr. Hoehndorf has developed the [PhenomeNET](https://academic.oup.com/nar/article-lookup/doi/10.1093/nar/gkr538) system for ontology-based prioritization of disease genes using model organism phenotypes, and contributed to the development of the [AberOWL](http://aber-owl.net) ontology repository. He is an associate editor for the Journal of Biomedical Semantics and editorial board member of the IOS press journal Data Science. He published over 80 papers in journals and international conferences, and presented previous tutorials on ontologies and their applications at ISMB, OWL-ED, and ECCB.

# License

The tutorial materials are under a CC-BY license.
