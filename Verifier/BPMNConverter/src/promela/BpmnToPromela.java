package promela;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import bpmnStructure.BpmnDiagram;

import bpmnStructure.PrintMessages.PrintMessageManager;

import tests.GeneratePurchaseCWP;
import tests.SimpleFlow;
import xmlConversion.XMLConverter;

public class BpmnToPromela {

	public static void main(String[] args) {
//		promelaGeneratorTest();
		importBpmnTest();
	}
	
	public static void importBpmnTest() {
				System.out.println("Starting");
				int default_number_of_instances = 1;
				

				String beginningPath = "/Users/iainlee/Documents/workspace/BPMN_project";
				String pathToDiagram = "/cwp-verification/Verifier/BPMNConverter/";
				String pathToResults = "";
				
				XMLConverter xml = new XMLConverter();
				BpmnDiagram bpmnMemoryStructure = xml.importXML(beginningPath + pathToDiagram, "diagrams/online_purchase2.bpmn");
				
				PromelaGenerator pg = new PromelaGenerator(bpmnMemoryStructure);
				
				
				int dataStoreSize = bpmnMemoryStructure.getDataStoreSize();

				String promelaString = pg.generatePromela(dataStoreSize>0?dataStoreSize:default_number_of_instances);
				String awkScript = PrintMessageManager.getInstance().generateAwkScript();

				writeStringToFile(promelaString, beginningPath + pathToResults + "generatedPromela.pml");
				writeStringToFile(awkScript, beginningPath + pathToResults + "messageTranslation.awk");
				
				System.out.println("finished");
	}
	
	public static void promelaGeneratorTest() {
		// directory to write to
				// get BpmnDiagram / give files needed for BPMN import
				// ask for number of instances you want to run it for
				
				/*number of instances to run if no datastore is set*/
				int default_number_of_instances = 1;
				

		String beginningPath = "C:\\Users\\jvisker\\Documents\\";
		
		GeneratePurchaseCWP cwp = new GeneratePurchaseCWP();
//		SimpleFlow cwp = new SimpleFlow();
		BpmnDiagram bpmnMemoryStructure = cwp.getManualBpmn();
		
		PromelaGenerator pg = new PromelaGenerator(bpmnMemoryStructure);
		
		
		int dataStoreSize = bpmnMemoryStructure.getDataStoreSize();

				String promelaString = pg.generatePromela(dataStoreSize>0?dataStoreSize:default_number_of_instances);
				String awkScript = PrintMessageManager.getInstance().generateAwkScript();

				writeStringToFile(promelaString, beginningPath + "generatedPromela.pml");
				writeStringToFile(awkScript, beginningPath + "messageTranslation.awk");
	}

	public static void writeStringToFile(String fileString, String path) {

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(fileString); // Replace with the string
									// you are trying to write
			out.close();
		} catch (IOException e) {
			System.out.println("Exception ");

		}
	}



}
