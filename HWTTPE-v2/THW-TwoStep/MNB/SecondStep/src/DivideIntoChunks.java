import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.*;

public class DivideIntoChunks {

	public static void DIC() {

		List<String> dataset = new ArrayList<String>();

		BufferedReader dataSetChunks = null;
		Writer datasetDividedInChunks = null;
		try {
			dataSetChunks = new BufferedReader(new InputStreamReader(
					new FileInputStream("fileToBeClassifiedDetailed.txt"),
					"UTF8"));

			int sizeOfdataSet = 0;
			String LJF;

			while ((LJF = dataSetChunks.readLine()) != null) {
				dataset.add(LJF);
				sizeOfdataSet++;
			}

			Random x = new Random();
			int z, i = 1;
			int NoOfLinesRead = 0;
			String l;
			int count = sizeOfdataSet;
			String filename;
			int NoOfFiles=(int) Math.ceil(sizeOfdataSet/15000)+1;
			filename = "dataset" + String.valueOf(i);
			datasetDividedInChunks = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF8"));
			while (!dataset.isEmpty()) {
				z = x.nextInt(count);
				l = (String) dataset.get(z);

				if (NoOfLinesRead==14999||(i==10&&NoOfLinesRead==(sizeOfdataSet%15000)-1))
					datasetDividedInChunks.write(l);
				else {
					datasetDividedInChunks.write(l+"\n");
				}
				dataset.remove(z);
				NoOfLinesRead++;
				count--;
				if (NoOfLinesRead == 15000) {
					i++;
					NoOfFiles--;
					datasetDividedInChunks.close();
					NoOfLinesRead = 0;
					filename = "dataset" + String.valueOf(i);

					datasetDividedInChunks = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									filename), "UTF8"));
				}

			}
		} catch (FileNotFoundException e) {
			System.err.println("Error opening seed words file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dataSetChunks.close();
				datasetDividedInChunks.close();

			} catch (Exception e) {
			}
		}
	}

	public static void main(String args[]) {
		DIC();
	}
}
