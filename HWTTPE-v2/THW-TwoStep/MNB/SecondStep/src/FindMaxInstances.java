import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FindMaxInstances {

	public static void FMI() {
		BufferedReader IWMV = null;
		BufferedReader FTBCD = null;

		try {
			IWMV = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							"predictionsNBx10trigrams,bigrams.txt"), "UTF8"));
			FTBCD = new BufferedReader(new InputStreamReader(
					new FileInputStream("fileToBeClassifiedDetailed.txt"),
					"UTF8"));

		} catch (FileNotFoundException e) {
			System.err.println("Error opening seed words file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		Writer WIWMV = null;

		try {
			WIWMV = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("IWMV.txt"), "UTF8"));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening trainingset file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}

		String lineJustFetched = null;
		String LJFFFTBCD = null;
		String wordsArray[];
		int NoOfLinesread, count;
		int c = 0;
		try {
			while (true) {
				lineJustFetched = IWMV.readLine();
				if (lineJustFetched == null) {
					break;
				} else {
					c++;
					if (c == 1)
						WIWMV.write(lineJustFetched + "\n");
					else {
						wordsArray = lineJustFetched.split("\\s+");
						NoOfLinesread = 0;
						if (Double
								.parseDouble(wordsArray[wordsArray.length - 1]) > 0.998
								&& Double
										.parseDouble(wordsArray[wordsArray.length - 1]) < 1) {
							// WIWMV.write(lineJustFetched+"\n")
							try {
								FTBCD = new BufferedReader(
										new InputStreamReader(
												new FileInputStream(
														"fileToBeClassifiedDetailed.txt"),
												"UTF8"));

							} catch (FileNotFoundException e) {
								System.err
										.println("Error opening seed words file.");
								System.exit(-1);
							} catch (UnsupportedEncodingException e) {
								System.err
										.println("UTF-8 encoding is not supported.");
								System.exit(-1);
							}
							count = Integer
									.parseInt(wordsArray[wordsArray.length - 4]);
							while (NoOfLinesread != count) {
								LJFFFTBCD = FTBCD.readLine();
								NoOfLinesread++;
							}
							WIWMV.write(LJFFFTBCD + "\t"
									+ wordsArray[wordsArray.length - 2] + "\n");
							try {
								FTBCD.close();
							} catch (IOException e) {
								System.err.println("Error in input/output.");
								e.printStackTrace();
							}

						}

					}

				}

			}

		} catch (IOException e) {
			System.err.println("Error in input/output.");
			e.printStackTrace();
		} finally {
			try {
				IWMV.close();
				WIWMV.close();
			} catch (Exception e) {

			}
		}
		// System.out.println("lineJustFetched : " + lineJustFetched);

	}

	public static void main(String args[]) throws Exception {
		FMI();
	}

}
