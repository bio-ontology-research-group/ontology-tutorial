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
RUN git clone --recurse-submodules https://github.com/bio-ontology-research-group/ontology-tutorial.git

# generally switch down to the right working directory
WORKDIR /home/bioonto/ontology-tutorial

# into the directory and update
RUN git pull 

# get data 
RUN wget http://aber-owl.net/aber-owl/diseasephenotypes/ontology/ontology-tutorial.tar.gz
RUN tar xvfz ontology-tutorial.tar.gz

RUN gunzip phenomenet-inferred.owl.gz

# hack to clean grape directoty - as advertised at https://github.com/lappsgrid-incubator/galaxy-appliance/issues/4 and https://stackoverflow.com/questions/16871792/groovy-grab-download-failed
# if this fails, have to remove dependency from downloadDependencies.groovy for now. 
RUN rm -rf /home/bioonto/.groovy/grapes

# # hack to get the correct grapes:
WORKDIR /home/bioonto/.groovy/
RUN sudo wget http://www.karwath.org/wp-content/uploads/groovy/grapes_all.tgz && sudo tar xvf grapes_all.tgz
RUN sudo chown -R bioonto:bioonto /home/bioonto/.groovy


# # Check the kernel has been installed
RUN jupyter kernelspec list

# trust the notebook
RUN jupyter trust /home/bioonto/ontology-tutorial/ontology-analysis.ipynb

# additional data (vectors)
WORKDIR /home/bioonto/ontology-tutorial/misc
RUN wget http://jagannath.pdn.cam.ac.uk/tutorial/phenome-vec-small.txt.gz

WORKDIR /home/bioonto/ontology-tutorial

# install grovy dependencies
RUN groovy downloadDependencies.groovy




# Launch in Juyter notebook

# docker run -i -t -p 8888:8888  -v $PWD:/home/bioonto/ontology-tutorial/ altermeister/bio-ontology-ontology-tutorial-docker /bin/bash -c "source activate java_env && export JAVA_OPTS=-Xmx12G && _JAVA_OPTIONS=-Xmx12G jupyter notebook --notebook-dir=/home/bioonto/ontology-tutorial/ --ip='0.0.0.0' --port=8888 --no-browser"


