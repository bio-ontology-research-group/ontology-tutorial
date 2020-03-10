def cli = new CliBuilder()
cli.with {
usage: 'Self'
  h longOpt:'help', 'this information'
  i longOpt:'input', 'input STRING file', args:1, required:true
  o longOpt:'output', 'output file containing generated ontology',args:1, required:true
  t longOpt:'test-output', 'output file containing generated testing data',args:1, required:true
  v longOpt:'valid-output', 'output file containing generated validation data',args:1, required:true
}
def opt = cli.parse(args)
if( !opt ) {
  //  cli.usage()
  return
}
if( opt.h ) {
    cli.usage()
    return
}

PrintWriter fout1 = new PrintWriter(new FileWriter(opt.o))
PrintWriter fout2 = new PrintWriter(new FileWriter(opt.v))
PrintWriter fout3 = new PrintWriter(new FileWriter(opt.t))
Set<String> set = new HashSet<String>()

new File(opt.i).splitEachLine(" ") { line ->
    if (!line[0].startsWith("protein")) {
	def id1 = line[0]
	def id2 = line[1]
	def score = new Integer(line[-1])
	if (score >= 700 && !set.contains("$id1\t$id2") && !set.contains("$id2\t$id1")) {  // only use high-confidence predictions
	    set.add("$id1\t$id2")
	}
    }
}
set.each { line ->
    def it = line.split("\t")
    def id1 = it[0]
    def id2 = it[1]
    def rand = Math.random()
    if (rand <= 0.8) {
	fout1.println("$id1\t$id2")
	fout1.println("$id2\t$id1")
    } else if (rand <= 0.9) {
	fout2.println("$id1\t$id2")
	fout2.println("$id2\t$id1")	
    } else {
	fout3.println("$id1\t$id2")
	fout3.println("$id2\t$id1")
    }
}
fout1.flush()
fout2.flush()
fout1.close()
fout2.close()
fout3.flush()
fout3.close()
