import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.Math;

public class ExtractTemporalInstancesFromResultOfFirstStep {

	public static void ETIFROFS(String f1, String f2, String f3)
			throws Exception {
		BufferedReader prediction_FileToBeClassified = null;
		Writer SecondStepFullTrainingFile = null;
		Writer SecondStepFullSeedWordFile = null;

		prediction_FileToBeClassified = new BufferedReader(
				new InputStreamReader(new FileInputStream(f1), "UTF8"));
		String wordsArrayForprediction_FileToBeClassified[];
		int sn;
		String lP;
		String wordOffset, word, POS, senseno, Gloss, classlabel, predictionValue;
		SecondStepFullTrainingFile = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f2 + "TestSet.arff"), "UTF8"));
		SecondStepFullSeedWordFile = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f3 + "SeedSet.txt"), "UTF8"));
		SecondStepFullTrainingFile
				.write("@relation timetagging\n\n@attribute gloss String\n@attribute tense {past, present, future, neutral}\n\n\n\n@data\n");
		prediction_FileToBeClassified.readLine(); // Ignore first Line

		while ((lP = prediction_FileToBeClassified.readLine()) != null) {

			wordsArrayForprediction_FileToBeClassified = lP.split("\t");
			wordOffset = wordsArrayForprediction_FileToBeClassified[0];
			word = wordsArrayForprediction_FileToBeClassified[1];
			POS = wordsArrayForprediction_FileToBeClassified[2];
			senseno = wordsArrayForprediction_FileToBeClassified[3];
			Gloss = wordsArrayForprediction_FileToBeClassified[4];
			classlabel = wordsArrayForprediction_FileToBeClassified[5];
			predictionValue = wordsArrayForprediction_FileToBeClassified[6];
			if (classlabel.equals("temporal")) {
				SecondStepFullTrainingFile.write("\'" + Gloss + "',?\n");
				SecondStepFullSeedWordFile.write(word + "\t" + wordOffset
						+ "\t" + POS + "\t" + senseno + "\t" + Gloss + "\n");
			}
		}
		prediction_FileToBeClassified.close();
		SecondStepFullSeedWordFile.close();
		SecondStepFullTrainingFile.close();

	}

	public static void main(String args[]) throws Exception {

		ETIFROFS(args[0], args[1], args[2]);
	}

}
