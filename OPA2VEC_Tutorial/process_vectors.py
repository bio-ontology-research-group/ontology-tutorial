import os
import sys

# process vectors


input_file="AllVectorResults1"

inf =open (input_file)
for line in inf:
	 line.strip().replace ('[',"").replace(']',"\n")
	 print (line.strip().replace ('[',"").replace(']',"\n")),
