import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.Math;

public class ExtractTemporalInstancesFromResultOfFirstStep {

	public static void ETIFROFS(String f1, String f2, String f3,String f4,String f5)
			throws Exception {
		BufferedReader prediction_FileToBeClassified = null;
		BufferedReader secondsteptrainingfile = null;
		BufferedReader firststepfulltesttrainfile = null;
		
		Writer SecondStepFullTrainingFile = null;
		Writer SecondStepFullSeedWordFile = null;
		

		prediction_FileToBeClassified = new BufferedReader(
				new InputStreamReader(new FileInputStream(f1), "UTF8"));
		
		secondsteptrainingfile  = new BufferedReader(
				new InputStreamReader(new FileInputStream(f4), "UTF8"));// To fetch first 207 lines 
		firststepfulltesttrainfile  = new BufferedReader(
				new InputStreamReader(new FileInputStream(f5), "UTF8"));  // to fetch vectors
		
		String wordsArrayForprediction_FileToBeClassified[];
		int sn;
		String lP;
		String wordOffset, word, POS, senseno, Gloss, classlabel, predictionValue;
		
		SecondStepFullTrainingFile = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f2 + "TestSet.arff"), "UTF8"));
		SecondStepFullSeedWordFile = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f3 + "SeedSet.txt"), "UTF8"));
		String ljffsstf;
		String s[]=new String[207];
		int i=0;
		while(!(ljffsstf=secondsteptrainingfile.readLine()).equals("@data")){
			s[i]=ljffsstf;
			i++;
			
		}
		s[i]=ljffsstf;
		i++;
		s[i]=secondsteptrainingfile.readLine();
		for(i=0;i<207;i++){
			SecondStepFullTrainingFile
			.write(s[i]+"\n");
		}
		String ljffssttf;
		while(!(ljffssttf=firststepfulltesttrainfile.readLine()).equals("@data"));
		firststepfulltesttrainfile.readLine();
		
		
		prediction_FileToBeClassified.readLine(); // Ignore first Line
		String wa[],va[];

		while ((lP = prediction_FileToBeClassified.readLine()) != null) {

			wordsArrayForprediction_FileToBeClassified = lP.split("\t");
			wordOffset = wordsArrayForprediction_FileToBeClassified[0];
			word = wordsArrayForprediction_FileToBeClassified[1];
			POS = wordsArrayForprediction_FileToBeClassified[2];
			senseno = wordsArrayForprediction_FileToBeClassified[3];
			Gloss = wordsArrayForprediction_FileToBeClassified[4];
			classlabel = wordsArrayForprediction_FileToBeClassified[5];
			predictionValue = wordsArrayForprediction_FileToBeClassified[6];
			ljffssttf=firststepfulltesttrainfile.readLine();
			wa=ljffssttf.split("',");
			
			va=wa[1].split(",");
			if (classlabel.equals("temporal")) {
				SecondStepFullTrainingFile.write("\'" + Gloss + "',");
				
				SecondStepFullSeedWordFile.write(word + "\t" + wordOffset
						+ "\t" + POS + "\t" + senseno + "\t" + Gloss + "\t");
				for(i=0;i<200;i++){
					SecondStepFullTrainingFile.write(va[i]+",");
					SecondStepFullSeedWordFile.write(va[i]+"\t");
					
				}
				SecondStepFullTrainingFile.write("?"+"\n");
				SecondStepFullSeedWordFile.write("\n");
				
			}
		}
		prediction_FileToBeClassified.close();
		SecondStepFullSeedWordFile.close();
		SecondStepFullTrainingFile.close();

	}

	public static void main(String args[]) throws Exception {

		ETIFROFS(args[0], args[1], args[2],args[3],args[4]);
	}

}