@Grab(group='com.github.sharispe', module='slib-sml', version='0.9.1')
@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')

import java.net.*
import org.openrdf.model.vocabulary.*
import slib.sglib.io.loader.*
import slib.sml.sm.core.metrics.ic.utils.*
import slib.sml.sm.core.utils.*
import slib.sglib.io.loader.bio.obo.*
import org.openrdf.model.URI
import slib.graph.algo.extraction.rvf.instances.*
import slib.sglib.algo.graph.utils.*
import slib.utils.impl.Timer
import slib.graph.algo.extraction.utils.*
import slib.graph.model.graph.*
import slib.graph.model.repo.*
import slib.graph.model.impl.graph.memory.*
import slib.sml.sm.core.engine.*
import slib.graph.io.conf.*
import slib.graph.model.impl.graph.elements.*
import slib.graph.algo.extraction.rvf.instances.impl.*
import slib.graph.model.impl.repo.*
import slib.graph.io.util.*
import slib.graph.io.loader.*
import groovyx.gpars.GParsPool


def cli = new CliBuilder()
cli.with {
    usage: 'Self'
    h longOpt:'help', 'this information'
    i longOpt:'input-annotations', 'input annotations file', args:1, required:true
    s longOpt:'similarity-id', 'similarity id (0-3)', args:1, required:true
    o longOpt:'output-proteins', 'output file containing proteins and similarity', args:1, required:true
}
def opt = cli.parse(args)
if( !opt ) {
    // cli.usage()
    return
}
if( opt.h ) {
    cli.usage()
    return
}


def factory = URIFactoryMemory.getSingleton()

def getURIfromGO = { go ->
    def id = go.split('\\:')[1]
    return factory.getURI("http://go/" + id)
}

URI graph_uri = factory.getURI("http://go/")
factory.loadNamespacePrefix("GO", graph_uri.toString())
G graph = new GraphMemory(graph_uri)

// Load OBO file to graph "go.obo"
GDataConf goConf = new GDataConf(GFormat.OBO, "data/go.obo")
GraphLoaderGeneric.populate(goConf, graph)

// Add virtual root for 3 subontologies__________________________________
URI virtualRoot = factory.getURI("http://go/virtualRoot")
graph.addV(virtualRoot)

GAction rooting = new GAction(GActionType.REROOTING)
rooting.addParameter("root_uri", virtualRoot.stringValue())
GraphActionExecutor.applyAction(factory, rooting, graph)


def proteins = new HashMap<String, Set<String> >()

// Load protein annotations
new File(opt.i).splitEachLine('\t') { items ->
    protID = items[0]
    if (!proteins.containsKey(protID)) {
	proteins.put(protID, new HashSet<URI>());
    }
    protURI = factory.getURI("http://" + protID)
    goURI = getURIfromGO(items[1])
    if (graph.containsVertex(goURI)) {
	proteins[protID].add(goURI)
	// Add annotations to graph
	Edge e = new Edge(protURI, RDF.TYPE, goURI);
	graph.addE(e);
    }
}

SM_Engine engine = new SM_Engine(graph)

String[] pairFlags = [
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_RESNIK_1995,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_SCHLICKER_2006,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_JIANG_CONRATH_1997_NORM
]

ICconf icConf = new IC_Conf_Corpus("ResnikIC", SMConstants.FLAG_IC_ANNOT_RESNIK_1995_NORMALIZED);
String flagGroupwise = SMConstants.FLAG_SIM_GROUPWISE_BMA;
String flagPairwise = pairFlags[Integer.parseInt(opt.s)];
println("Computing: " + flagGroupwise + " " + flagPairwise);
SMconf smConfGroupwise = new SMconf(flagGroupwise);
SMconf smConfPairwise = new SMconf(flagPairwise);
smConfPairwise.setICconf(icConf);

// Schlicker indirect
ICconf prob = new IC_Conf_Topo(SMConstants.FLAG_ICI_PROB_OCCURENCE_PROPAGATED);
smConfPairwise.addParam("ic_prob", prob);

// Map<URI, Double> ics = engine.computeIC(icConf);
// for (URI go: ics.keySet()) {
//     println(go.toString() + "\t" + ics.get(go))
// }

def n = proteins.size()
def proteinsList = proteins.keySet().toArray(new String[n])
def result = new Double[n][n]
def index = new Integer[n * n]
for (int i = 0; i < index.size(); i++) {
    index[i] = i
}

def c = 0;
def lock = new Object();

GParsPool.withPool {
    index.eachParallel { val ->
	def i = val.toInteger()
	def x = i.intdiv(n)
	def y = i % n
	if (x <= y) {
	    if (proteins[proteinsList[x]].size() > 0 && proteins[proteinsList[y]].size() > 0) {
		result[x][y] = engine.compare(
		    smConfGroupwise,
		    smConfPairwise,
		    proteins[proteinsList[x]],
		    proteins[proteinsList[y]])
		result[y][x] = result[x][y]
	    }
	    synchronized(lock) {
		c++;
		if (c % 100000 == 0) {
		    def progress = (c / (n * (n + 1) / 2)) * 10000;
		    progress = (int)progress / 100;
		    println(progress + "%");
		}
	    }
	}
    }
}

def out = new PrintWriter(new BufferedWriter(
  new FileWriter(opt.o)))
out.print(proteinsList[0]);
for (int i = 1; i < n; i++) {
  out.print("\t" + proteinsList[i]);
}
out.println();
for (int i = 0; i < n; i++) {
  out.print(result[i][0]);
  for (int j = 1; j < n; j++) {
      out.print("\t" + result[i][j]);
  }
  out.println();
}
out.flush()
out.close()
