import gensim
import gensim.models
import os
import sys
myclasses = str(sys.argv[1])
mywindow= int(sys.argv[2])
mysize= int(sys.argv[3])
mincount=int(sys.argv[4])
model =str (sys.argv[5])
pretrain=str (sys.argv[6])
ontology_corpus = str (sys.argv[7])
outfile=str(sys.argv[8])
mymodel=gensim.models.Word2Vec.load (pretrain)
mymodel.min_count = mincount
sentences =gensim.models.word2vec.LineSentence(ontology_corpus)
mymodel.build_vocab(sentences, update=True)
mymodel.train (sentences,total_examples=mymodel.corpus_count, epochs=100)
# Store vectors for each given class
word_vectors=mymodel.wv
file= open (outfile, 'w')
with open(myclasses) as f:
	for line in f:
		myclass1=line.rstrip()
		if myclass1 in word_vectors.vocab:		
			#myvectors[myclass1]=mymodel[myclass1];
			file.write (str(myclass1) + ' '+ str(mymodel[myclass1]) +'\n')
	file.close()

