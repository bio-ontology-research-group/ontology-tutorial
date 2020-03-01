import os
import sys
import argparse
import sys
import tempfile
path = "opa2vec/"
#####################################################################################################################
# create a parser object
parser = argparse.ArgumentParser(description = "OPA2Vec is a tool to produce feature vectors for biological entities from an ontology.")
 
# add argument
parser.add_argument("-ontology", nargs = '?', metavar = "ontology OWL file", type = str, 
                     help = "File containing ontology in OWL format",default='')
parser.add_argument("-associations", nargs = '?', metavar = "association file", type = str, 
                     help = "File containing entity-class associations",default='')
parser.add_argument("-outfile", nargs = '?', metavar = "output file", type = str, 
                     help = "Output file with ontology embeddings",default='')
parser.add_argument("-embedsize", nargs = '?', metavar = "embedding size", type = int,default=200, 
                     help = "Size of obtained vectors")
parser.add_argument("-windsize", nargs = '?', metavar = "window size", type = int,default=5, 
                     help = "Window size for word2vec model")
parser.add_argument("-mincount", nargs = '?', metavar = "min count", type = int,default=0, 
                     help = "Minimum count value for word2vec model") 
parser.add_argument("-model", nargs = '?', metavar = "model", type = str,default='sg', 
                     help = "Preferred word2vec architecture, sg or cbow") 
parser.add_argument("-entities", nargs = '?', metavar = "entities file", type = str, default="finalclasses.lst",
                     help = "File containing list of biological entities for which you would like to get the feature vectors (each entity in a separate line)")
parser.add_argument("-reasoner", nargs = '?', metavar = "reasoner", type = str, default='elk', 
                     help = "Preferred reasoner, hermit or elk")

# parse the arguments from standard input
args = parser.parse_args()
ontology_file =args.ontology
association_file=args.associations
entities_file=args.entities
window=args.windsize
embedding=args.embedsize
mincoun=args.mincount
model=args.model
reasoner=args.reasoner 
outfile =args.outfile

if (ontology_file is '' ):
	print ("\nError:Mandatory ontology file missing. For help, run: python runOnto2Vec.py --help\n")
	sys.exit()
if (association_file is ''):
	print ("\nError:Mandatory association file missing. For help, run: python runOnto2Vec.py --help\n")
	sys.exit()
if (outfile is ''):
	print ("\nError:Mandatory output-file name missing. For help, run: python runOnto2Vec.py --help\n")
	sys.exit()

if (model != 'sg' and model != 'cbow'):
	model ='sg'
####################################################################################################################
# Create temporary files 
#1. For ontology processing
axiomsinf = tempfile.NamedTemporaryFile()
axiomsorig =tempfile.NamedTemporaryFile()
classesfile =tempfile.NamedTemporaryFile()
axioms =tempfile.NamedTemporaryFile()
#3. For class extraction
annot_classes =tempfile.NamedTemporaryFile()
allclasses = tempfile.NamedTemporaryFile()
finalclasses = tempfile.NamedTemporaryFile()
#4. For associations
AssoAxiomInferred = tempfile.NamedTemporaryFile()
Assoc1 = tempfile.NamedTemporaryFile()
Assoc2 = tempfile.NamedTemporaryFile()
#5. For vector production
ontology_corpus = tempfile.NamedTemporaryFile()
tempvecs = tempfile.NamedTemporaryFile()

#####################################################################################################################
print ("	\n		*********** Onto2Vec Running ... ***********\n")

#1. Extract ontology axioms and classes 
print ("\n		1.Reasoning over ontology ...\n")
cmdonto ="groovy opa2vec/ProcessOntology.groovy " + str(ontology_file) +" "+str(reasoner) + " "+ str(axiomsinf.name)+" "+str(axiomsorig.name)+" "+str(classesfile.name)
os.system(cmdonto)
cmdMerge ="cat " +str(axiomsorig.name)+" "+str(axiomsinf.name)+" >"+str(axioms.name)
os.system(cmdMerge)
cmdgetclas="perl opa2vec/getclasses.pl "+str(association_file)+" "+str(annot_classes.name)
os.system(cmdgetclas)
cmdclas1="cat "+str(classesfile.name)+" "+str(annot_classes.name)+" > "+ str(allclasses.name)
os.system(cmdclas1)
cmdclas2 ="sort -u "+str(allclasses.name)+ " > " + str (finalclasses.name)
os.system(cmdclas2)
print ("\n   ######################################################################\n")

#2. Process associations 
print ("\n		2.Propagate associations through hierarchy ...\n")
cmdanc="perl opa2vec/AddAncestors.pl "+ str(association_file)+ " "+str(axioms.name)+" "+str(classesfile.name)+" "+ str(AssoAxiomInferred.name)
os.system(cmdanc)
cmdasso= "cat "+str(association_file)+" "+str(AssoAxiomInferred.name) +" > "+ str(Assoc1.name)
os.system(cmdasso)
cmdsort="sort -u "+ str(Assoc1.name) +" > "+ str(Assoc2.name)
os.system(cmdsort)
print ("\n   ######################################################################\n")

#3. Create corpus
print ("\n		3.Corpus creation ...\n")
cmdcorpus="cat "+str(axioms.name)+" "+str(Assoc2.name)+" > "+ str(ontology_corpus.name)
os.system(cmdcorpus)
print ("\n  ######################################################################\n")

#4. Run Word2Vec
tempoutfile ="tempout"
print ("\n		4.Running word2Vec ... \n")
if (entities_file is ''):
	entities_file = finalclasses.name
cmdwv="python2.7 opa2vec/runWV_onto.py "+str(ontology_corpus.name)+" "+str(window)+" "+str(embedding)+" "+str(mincoun)+" "+str(model)+" "+str(entities_file)+" "+ str(tempvecs.name)
os.system(cmdwv)

print ("\n  ######################################################################\n")

#5. Process vectors
print ("\n		5.Processing vectors ... \n")
cmdproc="python opa2vec/process_vectors.py " + str(tempvecs.name)+" > "+ str(outfile)
os.system (cmdproc)














