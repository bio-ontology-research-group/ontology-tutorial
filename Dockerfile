# We will use Ubuntu for our image
FROM continuumio/anaconda3:latest

# Updating Ubuntu packages
# RUN apt-get update && apt-get install -y --no-install-recommends apt-utils
RUN apt-get update && yes|apt-get upgrade
RUN apt-get install -y emacs


# # Adding wget and bzip2
RUN apt-get install -y wget bzip2

# # Add sudo
RUN apt-get -y install sudo

# # Add user ubuntu with no password, add to sudo group
RUN adduser --disabled-password --gecos '' bioonto
RUN adduser bioonto sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
USER bioonto
WORKDIR /home/bioonto/
RUN chmod a+rwx /home/bioonto/
RUN echo `pwd`

# install JDK
RUN sudo apt-get install -y default-jdk

# Add the conda-forge channel
RUN conda config --add channels conda-forge

# Create an isolated environment called `java_env` and install the kernel
RUN conda create --name java_env scijava-jupyter-kernel

# # Activate the `java_env` environment
# ak RUN source activate java_env

# install groovy
RUN sudo apt-get install -y groovy2


# download tutorial 
RUN git clone https://github.com/bio-ontology-research-group/ontology-tutorial.git

# into the directory and update
RUN cd ontology-tutorial && git pull 

# get data 
RUN cd /home/bioonto/ontology-tutorial; wget http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz; tar xvfz ontology-tutorial.tar.gz

RUN cd /home/bioonto/ontology-tutorial; gunzip phenomenet-inferred.owl.gz


# install grovy dependencies
RUN groovy /home/bioonto/ontology-tutorial/downloadDependencies.groovy
RUN cd ontology-tutorial && git pull 


# # Check the kernel has been installed
RUN jupyter kernelspec list

# trust the notebook
RUN jupyter trust /home/bioonto/ontology-tutorial/ontology-analysis.ipynb


# Launch in Juyter notebook

# docker run -i -t -p 8888:8888  altermeister/bio-ontology-ontology-tutorial-docker /bin/bash -c "_JAVA_OPTIONS=-Xmx12G jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='0.0.0.0' --port=8888 --no-browser"

# docker run -i -t -p 8888:8888  altermeister/bio-ontology-ontology-tutorial-docker /bin/bash -c "source activate java_env && export JAVA_OPTS=-Xmx12G && _JAVA_OPTIONS=-Xmx12G jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='0.0.0.0' --port=8888 --no-browser"


