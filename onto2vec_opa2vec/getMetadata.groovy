package groovy.owl;

@Grapes ([
    @Grab(group='org.semanticweb.elk', module='elk-owlapi', version='0.4.3'),
    @Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
    @Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5'),
    @Grab(group='net.sourceforge.owlapi', module='owlapi-impl', version='4.2.5'),
    @Grab(group='net.sourceforge.owlapi', module='owlapi-parsers', version='4.2.5'),
    @Grab(group='net.sourceforge.owlapi', module='org.semanticweb.hermit', version='1.3.8.413'),
    @GrabConfig(systemClassLoader=true)
])

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.search.EntitySearcher.*;
import static org.semanticweb.owlapi.search.EntitySearcher.getAnnotationObjects;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;
import org.semanticweb.owlapi.manchestersyntax.renderer.*;
import org.semanticweb.owlapi.model.providers.*;
import java.io.*;
import java.util.Scanner;
import org.semanticweb.owlapi.search.EntitySearcher;


OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
String ontostring = args[0];
annotations = args[1].split(',');
String metadata =args[2];

OWLOntology MyOntology = manager.loadOntologyFromOntologyDocument(new File(ontostring));
OWLObjectRenderer rend =new ManchesterOWLSyntaxOWLObjectRendererImpl ();
OWLDataFactory factory=manager.getOWLDataFactory();
OWLAnnotationProperty  p=factory.getRDFSLabel();

FileWriter fw= new FileWriter (metadata,true); BufferedWriter bw =new BufferedWriter (fw); PrintWriter out =new PrintWriter (bw);
if (annotations[0].toLowerCase().equals("all"))
    {
    Set<OWLClass> classes = MyOntology.getClassesInSignature();
    //rdfsLabels = new HashMap<String,String>();
    for (OWLClass cls:classes)
	{
	for(OWLAnnotation a : EntitySearcher.getAnnotations(cls, MyOntology))
	    {
	    // properties are of several types: rdfs-label, altLabel or prefLabel
	    OWLAnnotationProperty prop = a.getProperty();
	    OWLAnnotationValue val = a.getValue();
	    if(val instanceof OWLLiteral)
		{
		myproperty=prop.toString();
		class11=cls.toString();
		out.println((class11.replaceAll("\n", " ")+ " "+ myproperty.replaceAll("\n", " ")+" " + ((OWLLiteral) val).getLiteral()).replaceAll("\n"," "));
	    }
	}
    }
} else if (annotations[0].toLowerCase().equals("none"))
    {
    out.println(" ");
    //break;
}
else
    {
    Set<OWLClass> classes = MyOntology.getClassesInSignature();
    //rdfsLabels = new HashMap<String,String>();
    for (OWLClass cls:classes)
	{
	for(OWLAnnotation a : EntitySearcher.getAnnotations(cls, MyOntology))
	    {
	    // properties are of several types: rdfs-label, altLabel or prefLabel
	    OWLAnnotationProperty prop = a.getProperty();
	    OWLAnnotationValue val = a.getValue();
	    if(val instanceof OWLLiteral)
		{
		myproperty=prop.toString();
		class11=cls.toString();
		for (i=0;i<annotations.length;i++)
		    {
		    if (annotations[i].equals(myproperty))
			{
			out.println((class11.replaceAll("\n", " ")+ " "+ myproperty.replaceAll("\n", " ")+" " + ((OWLLiteral) val).getLiteral()).replaceAll("\n"," "));

		    }
		}
	    }
	}
    }
}
out.flush()
out.close()
