package automated;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FMeasureFinder {

	// here input file path has to be given
	private static String INPUT_FILE_PATH = "C:/";

	// here output file path has to be given
	private static String OUTPUT_FILE_PATH = "C:/";

	private static String SLASH = "/";
	private static String UNDERSCORE = "_";
	private static String ERROR_ON_TEST_DATA = "Error on test data";
	private static String DETAILED_ACCURACY_BY_CLASS = "Detailed Accuracy By Class";
	private static String CONFUSION_MATRIX = "Confusion Matrix";
	private static String NEWLINE = "\r\n";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String fMeasure = null;
			FileInputStream fStreamTrain = new FileInputStream(INPUT_FILE_PATH
					+ SLASH + args[0] + UNDERSCORE + args[1]);
			DataInputStream inTrain = new DataInputStream(fStreamTrain);
			BufferedReader brTrain = new BufferedReader(new InputStreamReader(
					inTrain));
			FileWriter writer = new FileWriter(OUTPUT_FILE_PATH + SLASH
					+ args[0] + UNDERSCORE + args[1]);
			String strTrain = null;
			int classCount = Integer.parseInt(args[2]);
			
			while ((strTrain = brTrain.readLine()) != null) {
				if (strTrain.contains(ERROR_ON_TEST_DATA)) {
					writer.write(strTrain + NEWLINE);
					for (int index = 1; index <= 8; index++) {
						writer.write(brTrain.readLine() + NEWLINE);
					}
				}
				if (strTrain.contains(DETAILED_ACCURACY_BY_CLASS)) {
					writer.write(strTrain + NEWLINE);
					for (int index = 1; index <= (classCount + 4); index++) {
						if (index == (classCount + 3)) {
							String fmString = brTrain.readLine();
							writer.write(fmString + NEWLINE);
							String fmStr[] = fmString.split("\\s+");
							fMeasure = fmStr[6];
						} else {
							writer.write(brTrain.readLine() + NEWLINE);
						}
					}
				}
				if (strTrain.contains(CONFUSION_MATRIX)) {
					writer.write(strTrain);
					for (int index = 1; index <= (classCount + 3); index++) {
						writer.write(brTrain.readLine());
					}
				}
			}
			writer.close();
			brTrain.close();
			inTrain.close();
			fStreamTrain.close();
			System.out.println(fMeasure);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
