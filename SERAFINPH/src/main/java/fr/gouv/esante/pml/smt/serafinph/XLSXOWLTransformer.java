package fr.gouv.esante.pml.smt.serafinph;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
import fr.gouv.esante.pml.smt.utils.ChargerMapping;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.DCVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;



public class XLSXOWLTransformer {
	
	//*********
	private static String xlsxSerafinPhFile = PropertiesUtil.getProperties("xlsxSerafinPhFile");
	  private static String owlSerafinPhFileName = PropertiesUtil.getProperties("owlSerafinPhFileName");

	  private static OWLOntologyManager man = null;
	  private static OWLOntology onto = null;
	  private static OWLDataFactory fact = null;
	  
	  private static OWLAnnotationProperty skosNotation  = null;
	  private static OWLAnnotationProperty rdfsLabel  = null;
	  private static OWLAnnotationProperty skosNote  = null;
	  private static OWLAnnotationProperty dctCreated  = null;
	  private static OWLAnnotationProperty dctModified  = null;
	  private static OWLAnnotationProperty admsStatus  = null;
	  private static OWLAnnotationProperty dcType  = null;
	  private static OWLAnnotationProperty owlDeprecated  = null;


	  
	
	public static void main(String[] args) throws Exception {
		
		ChargerMapping.chargeExcelConceptToList(xlsxSerafinPhFile);
		  
		final OutputStream fileoutputstream = new FileOutputStream(owlSerafinPhFileName);
		 man = OWLManager.createOWLOntologyManager();
		 onto = man.createOntology(IRI.create(PropertiesUtil.getProperties("terminologie_IRI")));
		 fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		 skosNotation =  fact.getOWLAnnotationProperty(SKOSVocabulary.NOTATION.getIRI());
		 rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		 skosNote =  fact.getOWLAnnotationProperty(SKOSVocabulary.NOTE.getIRI());
		 dctCreated = fact.getOWLAnnotationProperty(DCTVocabulary.created.getIRI());
	     dctModified = fact.getOWLAnnotationProperty(DCTVocabulary.modified.getIRI());
	     admsStatus = fact.getOWLAnnotationProperty(ADMSVocabulary.status.getIRI());
	     dcType = fact.getOWLAnnotationProperty(DCVocabulary.type.getIRI());
	     owlDeprecated =  fact.getOWLAnnotationProperty(fact.getOWLDeprecated());

		 
		 
		    OWLClass owlClass = null;
		    
		    
		    createPrincipalNoeud();
		    createConceptRetiresNoeud();
		    createConceptRetires2023Noeud();
		    

		    
		    for(String id: ChargerMapping.listConceptsSerafin.keySet()) {
		    	final String about = PropertiesUtil.getProperties("terminologie_URI") + id;
		        owlClass = fact.getOWLClass(IRI.create(about));
		        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		        man.applyChange(new AddAxiom(onto, declare));
		        
		        
		        if(!"BLOC".equals(ChargerMapping.listConceptsSerafin.get(id).get(0))) {
		         
		        	if("true".equals(ChargerMapping.listConceptsSerafin.get(id).get(6))) {	
		              String aboutSubClass = null;
		              aboutSubClass = PropertiesUtil.getProperties("terminologie_URI")+ ChargerMapping.listConceptsSerafin.get(id).get(3)  ;
		              OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
		              OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
		              man.applyChange(new AddAxiom(onto, axiom));
		        	
		            }else {
			        	
			        	
			        	String aboutSubClass = null;
				        aboutSubClass = PropertiesUtil.getProperties("terminologie_URI")+ "Concept_retirés_2023"  ;
				        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
				        
				        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
				        man.applyChange(new AddAxiom(onto, axiom));
			        	
			        	
			        }
		        
		          
		          if(!"".equals(ChargerMapping.listConceptsSerafin.get(id).get(0)))
		            addLateralAxioms(dcType, "Niveau "+ChargerMapping.listConceptsSerafin.get(id).get(0), owlClass);
		        
		        
		        
		        }else {
		        	
		        	String aboutSubClass = null;
			        aboutSubClass = PropertiesUtil.getProperties("URI_parent")  ;
			        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
			        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
			        man.applyChange(new AddAxiom(onto, axiom));
			        addLateralAxioms(dcType, ChargerMapping.listConceptsSerafin.get(id).get(0), owlClass);
		        	
		        }
		        
		        
		       

		        
		        
		       addLateralAxioms(skosNotation, id, owlClass);
		       
		      

		       addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsSerafin.get(id).get(1), owlClass, "fr");
		       
		       if(!"".equals(ChargerMapping.listConceptsSerafin.get(id).get(2)))
		        addLateralAxioms(skosNote, ChargerMapping.listConceptsSerafin.get(id).get(2), owlClass, "fr");
		       
		       addDatelAxioms(dctCreated, ChargerMapping.listConceptsSerafin.get(id).get(4), owlClass);
		       
		       if(!"".equals(ChargerMapping.listConceptsSerafin.get(id).get(5)))
		         addDatelAxioms(dctModified, ChargerMapping.listConceptsSerafin.get(id).get(5), owlClass);
		       
		       if("true".equals(ChargerMapping.listConceptsSerafin.get(id).get(6))) {
		    	   
		          addURIAxioms(admsStatus, "actif", owlClass);  
		         // addBooleanAxioms(owlDeprecated, "false", owlClass);
		        
		       }else if("false".equals(ChargerMapping.listConceptsSerafin.get(id).get(6))) {
		    	   addURIAxioms(admsStatus,"inactif", owlClass);  
			       addBooleanAxioms(owlDeprecated, "true", owlClass);
		       }
		     
		       
		    
		      
		       
		    
		       
		       
		      
		        
		        
		    }
		    
		    
		    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		    ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		    
		    
		    IRI iri = IRI.create(PropertiesUtil.getProperties("terminologie_IRI"));
		    man.applyChange(new SetOntologyID(onto,  new OWLOntologyID(iri)));
		   
		  //  addPropertiesOntology();
		    
		    man.saveOntology(onto, ontologyFormat, fileoutputstream);
		    fileoutputstream.close();
		    System.out.println("Done.");
		
		

	}
	
	public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, lang));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addDatelAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	    		fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,OWL2Datatype.XSD_DATE_TIME));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  public static void addURIAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {

	    IRI iri_creator = IRI.create(PropertiesUtil.getProperties("terminologie_URI")+val);
		   
	    OWLAnnotationProperty prop_creator =fact.getOWLAnnotationProperty(prop.getIRI());
	    
	    OWLAnnotation annotation = fact.getOWLAnnotation(prop_creator, iri_creator);
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	    
	    
	  }
  
  public static void addBooleanAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,OWL2Datatype.XSD_BOOLEAN));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  
  public static void createPrincipalNoeud() {
	  
	  // String noeud_parent = PropertiesUtil.getProperties("noeud_parent");
	   String noeud_parent_label=PropertiesUtil.getProperties("label_noeud_parent");
	   String noeud_parent_notation=PropertiesUtil.getProperties("notation_noeud_parent");
	    
	   final String aboutSubClass1 = PropertiesUtil.getProperties("URI_parent") ;
	   OWLClass subClass1 = fact.getOWLClass(IRI.create(aboutSubClass1));
       addLateralAxioms(skosNotation, noeud_parent_notation, subClass1);
       addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "fr");
       //addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "en");
	  
  }
  
  
  
  public static void createConceptRetires2023Noeud() {
	  
	  
	    
	   final String classRacine = "http://data.esante.gouv.fr/CNSA/SERAFINPH/Concept_retirés_2023" ;
	   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
	   
	   String aboutSubClass = null;
      aboutSubClass = "http://data.esante.gouv.fr/CNSA/SERAFINPH/Concept_retirés";
      OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
      
      OWLAxiom axiom = fact.getOWLSubClassOfAxiom(noeudRacine, subClass);
      man.applyChange(new AddAxiom(onto, axiom));
    
	  addLateralAxioms(skosNotation, "Concept retirés 2023", noeudRacine);
     
	  
}
 
 
  public static void createConceptRetiresNoeud() {
	  
	  
	    
	   final String classRacine = "http://data.esante.gouv.fr/CNSA/SERAFINPH/Concept_retirés" ;
	   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
	    addLateralAxioms(skosNotation, "Concept retirés", noeudRacine);
     
	  
}
  
  
  
  

}
