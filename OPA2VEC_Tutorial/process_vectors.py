import os
import sys

# process vectors

output_file="processed_vectors2"

input_file="sample_output"

inf =open (input_file)
for line in inf:
	 line.strip().replace ('[',"").replace(']',"\n")
	 print (line.strip().replace ('[',"").replace(']',"\n")),
