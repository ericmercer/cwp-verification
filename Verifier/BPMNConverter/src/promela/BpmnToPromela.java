package promela;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import bpmnStructure.BpmnDiagram;
import bpmnStructure.BpmnProcess;
import bpmnStructure.PrintMessages.PrintMessageManager;
import bpmnStructure.dataTypes.BoolType;
import bpmnStructure.dataTypes.MtypeType;
import bpmnStructure.dataTypes.PositiveIntType;
import bpmnStructure.dataTypes.PromelaTypeDef;
import tests.GeneratePurchaseCWP;

public class BpmnToPromela {

	public static void main(String[] args) {
		// directory to write to
		// get BpmnDiagram / give files needed for BPMN import
		// ask for number of instances you want to run it for
		
		/*number of instances to run if no datastore is set*/
		int default_number_of_instances = 1;
		

		String beginningPath = "C:\\Users\\jvisker\\Documents\\";
		
		GeneratePurchaseCWP cwp = new GeneratePurchaseCWP();
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
