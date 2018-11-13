import os
import sys
import numpy
from sklearn.metrics import pairwise_distances
from scipy.spatial.distance import cosine 
#from sklearn.metrics.pairwise import cosine_similarity
from itertools import islice

query =str(sys.argv[1])
n = int (sys.argv[2])

vectors=numpy.loadtxt("sample_vectors");
text_file="sample_entities"
classfile=open (text_file)
mylist=[]
for linec in classfile:
	mystr=linec.strip()
	mylist.append(mystr)

#print (mylist)

vectors_map={}

for i in range(0,len(mylist)):
	vectors_map[mylist[i]]=vectors[i,:]
	#print (str(mylist[i])+" *** "+ str(vectors_map[mylist[i]])+"\n")

#inv_map = {v:k for k, v in vectors_map.iteritems()}
cosine_sim={}
for x in range(0,len(mylist)):
	if (mylist[x]!=query): 	
		v1=vectors_map[mylist[x]]
		v2=vectors_map[query]
		value=cosine(v1,v2)
		#print (str(mylist[x])+" distance is:"+str(value)+"\n")
		cosine_sim[mylist[x]]=value

ss=sorted(cosine_sim,key=cosine_sim.get, reverse=True)
#sss={key:rank for  rank, key in enumerate(sorted(cosine_sim, key=cosine_sim.get, reverse=True),1)}
#n_items = take (n,ss.iteritems())
iterator=islice(ss,n)
for d in iterator:
	print (str(d) +" "+str(cosine_sim[d])+"\n")

#for key in n_items:
#	print (str(key) +" "+str(cosine_sim[key])+"\n")
