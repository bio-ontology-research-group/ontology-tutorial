// no individuals, only classes [this implements one of the normalization steps in Baader (2005), i.e., introduction of singleton classes for individuals.]!
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
import org.semanticweb.owlapi.search.*
import org.semanticweb.owlapi.formats.*


def cli = new CliBuilder()
cli.with {
usage: 'Self'
  h longOpt:'help', 'this information'
  i longOpt:'input', 'input STRING file', args:1, required:true
  a longOpt:'annot', 'input annotations file', args:1, required:true
  o longOpt:'output', 'output file containing generated ontology',args:1, required:true
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

OWLOntologyManager outputManager = OWLManager.createOWLOntologyManager()
OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLOntology ont = manager.loadOntologyFromOntologyDocument(new File("data/go.owl"))
OWLDataFactory fac = manager.getOWLDataFactory()



def idset = new LinkedHashSet()
def rel = 'interacts'
new File(opt.i).splitEachLine("\t") { line ->
    def id1 = line[0]
    def id2 = line[1]
    idset.add(id1)
    idset.add(id2)
    def ind1 = fac.getOWLClass(IRI.create("http://$id1"))
    def ind2 = fac.getOWLClass(IRI.create("http://$id2"))
    def rel1 = fac.getOWLObjectProperty(IRI.create("http://$rel"))
    def ax = fac.getOWLSubClassOfAxiom(ind1, fac.getOWLObjectSomeValuesFrom(rel1, ind2))
    manager.addAxiom(ont, ax)
}

def hasFunction = fac.getOWLObjectProperty(IRI.create("http://hasFunction"))
new File(opt.a).splitEachLine("\t") { line ->
    def id = line[0]
    def go = "http://purl.obolibrary.org/obo/" + line[1]?.replaceAll(":", "_")
    def goclass = IRI.create(go)
    if (id in idset) {
	def ind1 = fac.getOWLClass(IRI.create("http://$id"))
	def ax = fac.getOWLSubClassOfAxiom(ind1, fac.getOWLObjectSomeValuesFrom(hasFunction, fac.getOWLClass(goclass)))
	manager.addAxiom(ont,ax)
    }
}

File f = new File(opt.o)
manager.saveOntology(ont,  IRI.create("file:" + f.getAbsolutePath()))
