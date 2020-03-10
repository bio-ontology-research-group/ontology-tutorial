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
    is longOpt:'input-string', 'input STRING file', args:1, required:true
    ia longOpt:'input-annotations', 'input annotations file', args:1, required:true
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


// Load proteins
def proteins = new HashMap<String, Set<String> >()
new File(opt.is).splitEachLine('\t') { items ->
    if (!proteins.containsKey(items[0])) {
	proteins[items[0]] = new HashSet<String>()
    }
    if (!proteins.containsKey(items[1])) {
	proteins[items[1]] = new HashSet<String>()
    }
    
}
// Load protein annotations
new File(opt.ia).splitEachLine('\t') { items ->
    protID = items[0] + "." + items[1]
    if (proteins.containsKey(protID)) {
	protURI = factory.getURI("http://" + protID)
	goURI = getURIfromGO(items[3])
	if (graph.containsVertex(goURI)) {
	    proteins[protID].add(goURI)
	    // Add annotations to graph
	    Edge e = new Edge(protURI, RDF.TYPE, goURI);
	    graph.addE(e);
	}
    }
}

def sim_id = 2 //this.args[0].toInteger()

SM_Engine engine = new SM_Engine(graph)

// BMA+Resnik, BMA+Schlicker2006, BMA+Lin1998, BMA+Jiang+Conrath1997,
// DAG-GIC, DAG-NTO, DAG-UI

String[] flags = [
    // SMConstants.FLAG_SIM_GROUPWISE_AVERAGE,
    // SMConstants.FLAG_SIM_GROUPWISE_AVERAGE_NORMALIZED_GOSIM,
    SMConstants.FLAG_SIM_GROUPWISE_BMA,
    SMConstants.FLAG_SIM_GROUPWISE_BMM,
    SMConstants.FLAG_SIM_GROUPWISE_MAX,
    SMConstants.FLAG_SIM_GROUPWISE_MIN,
    SMConstants.FLAG_SIM_GROUPWISE_MAX_NORMALIZED_GOSIM
]

// List<String> pairFlags = new ArrayList<String>(SMConstants.PAIRWISE_MEASURE_FLAGS);
String[] pairFlags = [
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_RESNIK_1995,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_SCHLICKER_2006,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998,
    SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_JIANG_CONRATH_1997_NORM
]

// ICconf icConf = new IC_Conf_Topo("Sanchez", SMConstants.FLAG_ICI_SANCHEZ_2011);
ICconf icConf = new IC_Conf_Corpus("ResnikIC", SMConstants.FLAG_IC_ANNOT_RESNIK_1995_NORMALIZED);
String flagGroupwise = flags[sim_id.intdiv(pairFlags.size())];
String flagPairwise = pairFlags[sim_id % pairFlags.size()];
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

def c = 0

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
		if (c % 100000 == 0) println(c)
		c++
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
