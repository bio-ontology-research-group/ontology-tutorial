@Grapes([
    @Grab(group="org.semanticweb.elk", module="elk-owlapi", version="0.4.3"),
    @Grab(group="net.sourceforge.owlapi", module="owlapi-api", version="4.2.5"),
    @Grab(group="net.sourceforge.owlapi", module="owlapi-apibinding", version="4.2.5"),
    @Grab(group="net.sourceforge.owlapi", module="owlapi-impl", version="4.2.5"),
    @Grab(group="net.sourceforge.owlapi", module="owlapi-parsers", version="4.2.5"),
    @GrabConfig(systemClassLoader=true)
])

import org.semanticweb.owlapi.model.parameters.*;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration;
import org.semanticweb.elk.reasoner.config.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.owllink.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.search.*;

def cli = new CliBuilder()
cli.with {
usage: 'Self'
  h longOpt:'help', 'this information'
  i longOpt:'input', 'input STRING file', args:1, required:true
  o longOpt:'output', 'output file containing N3 file',args:1, required:true
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

PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter(opt.o)))

def tid = ""
def map = [:].withDefault { [:].withDefault { new LinkedHashSet() } }
new File("data/go.obo").eachLine { line ->
    if (line.startsWith("id:")) {
	tid = line.substring(3).trim()
    }
    if (tid.startsWith("GO:")) {
	if (line.startsWith("is_a:")) {
	    def id2 = line.substring(6, 16).trim()
	    map[tid]["is_a"].add(id2)
	}
	if (line.startsWith("relationship:")) {
	    def l2 = line.substring(14, line.indexOf("!")).trim()
	    def rel = l2.substring(0, l2.indexOf(" ")).trim()
	    def id2 = l2.substring(l2.indexOf(" ")+1).trim()
	    map[tid][rel].add(id2)
	}
    }
}
map.each { k, map2 ->
    map2.each { k2, s ->
	s.each { el ->
	    fout.println("<http://$k> <http://$k2> <http://$el> .")
	}
    }
}

def idset = new LinkedHashSet()
def rel = 'interacts'

new File(opt.i).splitEachLine("\t") { line ->
    def id1 = line[0]
    def id2 = line[1]
    idset.add(id1)
    idset.add(id2)
    fout.println("<http://$id1> <http://$rel> <http://$id2> .")
}

def hasFunction = "<http://hasFunction>"
new File("data/data/all_go_knowledge_explicit.tsv").splitEachLine("\t") { line ->
    def id = line[0]+"."+line[1]
    def go = line[3]
    if (id in idset) {
	fout.println("<http://$id> $hasFunction <http://$go> .")
    }
}

fout.flush()
fout.close()
