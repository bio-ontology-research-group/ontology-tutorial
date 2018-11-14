import os
import sys
# importing required modules
import argparse
import sys
#ontology_file =sys.argv[1]
#association_file=sys.argv[2]
#classes_file=sys.argv[3]
# create a parser object
parser = argparse.ArgumentParser(description = "OPA2Vec is a tool to produce feature vectors for biological entities from an ontology.")
 
# add argument
parser.add_argument("ontology", nargs = '?', metavar = "ontology file", type = str, 
                     help = "File containing ontology in owl format",default='')
parser.add_argument("association", nargs = '?', metavar = "association file", type = str, 
                     help = "File containing entity class associations",default='')
#parser.add_argument("entities", nargs = '?', metavar = "entities file", type = str, 
             #        help = "File containing list of of biological entities for which you would like to get the feature vectors (each entity in a separate line)")
parser.add_argument("-embedsize", nargs = '?', metavar = "embedding size", type = int,default=200, 
                     help = "Size of obtained vectors")
parser.add_argument("-windsize", nargs = '?', metavar = "window size", type = int,default=5, 
                     help = "Window size for word2vec model")
parser.add_argument("-mincount", nargs = '?', metavar = "min count", type = int,default=25, 
                     help = "Minimum count value for word2vec model") 
parser.add_argument("-model", nargs = '?', metavar = "model", type = str,default='sg', 
                     help = "Preferred word2vec architecture, sg or cbow") 
parser.add_argument("-pretrained", nargs = '?', metavar ="pre-trained model", type =str, default ="RepresentationModel_pubmed.txt", help= "Pre-trained word2vec model for background knowledge")

parser.add_argument("-entities", nargs = '?', metavar = "entities file", type = str, default="finalclasses.lst",
                     help = "File containing list of of biological entities for which you would like to get the feature vectors (each entity in a separate line)")
parser.add_argument("-annotations", nargs='?',metavar="metadata annotations", type = str, default="all", help = "Full URIs of annotation properties to be included in metadata -separated by a comma ,- use 'all' for all annotation properties (default) or 'none' for no annotation property ")
parser.add_argument("-reasoner", nargs = '?', metavar = "reasoner", type = str,default='elk', 
                     help = "Preferred reasoner, hermit or elk")
parser.add_argument("-debug", nargs = '?', metavar = "debug", type = str,default='no', 
                     help = "yes/no, if set to yes, keeps intermediate files for debugging")
# parse the arguments from standard input
args = parser.parse_args()
 
# check if add argument has any input data.
# If it has, then print sum of the given numbers
#if len(args.add) != 0:
 #   print(sum(args.add))
#print ("association ")+str (args.association)
#print ("ontology ")+str (args.ontology)
#print ("entities ")+str(args.entities)
#print ("embedding ")+str (args.embedsize)
#print ("window ")+str (args.windsize)
#print ("model ")+ str (args.model)

ontology_file =args.ontology
association_file=args.association
classes_file=args.entities
window=args.windsize
embedding=args.embedsize
mincoun=args.mincount
model=args.model
pretrained =args.pretrained
listofuri=args.annotations
reasoner=args.reasoner 
debug =args.debug
if (ontology_file is '' ):
	print ("\nError:Mandatory ontology file missing. For help, run: python runOPA2Vec.py --help\n")
	sys.exit()
if (association_file is ''):
	print ("\nError:Mandatory association file missing. For help, run: python runOPA2Vec.py --help\n")
	sys.exit()

if (model != 'sg' and model != 'cbow'):
	model ='sg'
print ("***********OPA2Vec Running ...***********\n")
print ("***********1.Ontology Processing ...***********\n")
commandF ="groovy ProcessOntology.groovy " + str(ontology_file) +" "+str(reasoner)
os.system(commandF)
print ("***********Ontology Processing Complete ...***********\n")
#Needed pre-processng ... just in case !
commandExtra1="perl getclasses.pl "+str(association_file)
os.system(commandExtra1)
commandMerge =("cat axiomsorig.lst axiomsinf.lst > axioms.lst")
os.system(commandMerge)
print ("***********2.Metadata Extraction ...***********\n")
commandS ="groovy getMetadata.groovy "+ str(ontology_file)+" "+str(listofuri)
os.system(commandS)
print ("***********Metadata Extraction Complete ...***********\n")
print ("***********3.Propagate Associations through hierarchy ...***********\n")
commandT="perl AddAncestors.pl "+ str(association_file)
os.system(commandT)
commandF= "cat "+str(association_file)+" associationAxiomInferred.lst > AllAssociations1.lst"
os.system(commandF)
Addacommand="sort -u AllAssociations1.lst > AllAssociations.lst"
os.system(Addacommand)
print ("***********Association propagation Complete ...***********\n")
print ("***********4.Corpus Creation ...***********\n")
commandFif="cat axioms.lst metadata.lst AllAssociations.lst  > ontology_corpus.lst"
os.system(commandFif)
print ("***********Corpus Creation Complete ...***********\n")
print ("***********5.Running Word2Vec ...*********** \n")
commandSix="python runWord2Vec.py "+str(classes_file)+" "+str(window)+" "+str(embedding)+" "+str(mincoun)+" "+str(model)+" "+str(pretrained)
os.system(commandSix)
print ("***********Vector representations available at AllVectorResults.lst ***********\n")
print ("***********OPA2Vec Complete ...***********\n")

#Let's cleanup a bit
if (debug != 'yes' and debug != 'Yes' ):
	cleanup="rm axiom* AllAsso* allclasses.lst inferred* classes.lst finalclasses.lst metadata.lst ontology_corpus.lst annotclasses* associationAxiom*"
	os.system(cleanup)
