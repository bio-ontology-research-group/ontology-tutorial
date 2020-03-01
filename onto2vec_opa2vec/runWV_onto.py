import gensim
import gensim.models
import os
import sys

# Define arguments
text_corpus = str (sys.argv[1])
mywindow= int(sys.argv[2])
mysize= int(sys.argv[3])
mincount=int(sys.argv[4])
model =str (sys.argv[5])
myclasses=str(sys.argv[6])
outfile=str(sys.argv[7])

# Pre-train model
sentences =gensim.models.word2vec.LineSentence(text_corpus) # a memory-friendly iterator
#print ("vocabulary is ready \n");

if (model =="sg"):
	mymodel =gensim.models.Word2Vec(sentences, sg=1, min_count=mincount, size=mysize ,window=mywindow, sample=1e-3)
else:
	mymodel =gensim.models.Word2Vec(sentences,sg=0,min_count=mincount, size=mysize ,window=mywindow)

# Store vectors for each given class
word_vectors=mymodel.wv
file= open (outfile, 'w')
with open(myclasses) as f:
	for line in f:
		myclass1=line.rstrip()
		if myclass1 in word_vectors.vocab:		
			file.write (str(myclass1) + ' '+ str(mymodel[myclass1]) +'\n')
	file.close()

