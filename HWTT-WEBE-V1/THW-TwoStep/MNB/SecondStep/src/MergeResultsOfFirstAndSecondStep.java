import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MergeResultsOfFirstAndSecondStep {
	
	public static void main(String args[]) throws Exception {

		BufferedReader rofs = null; // result of first step
		BufferedReader ross = null; // result of second step
		Writer THW = null; // Tempo-HindiWordNetFile

		rofs = new BufferedReader(new InputStreamReader(new FileInputStream(
				args[0]), "UTF8"));
		ross = new BufferedReader(new InputStreamReader(new FileInputStream(
				args[1]), "UTF8"));
		THW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				args[2],true), "UTF8"));
		String ljf, wa[];
		THW.write("Word_Offset\tWord\tPOS\tSense_no\tGloss\tClass\tPrediction_Value\tEndMarker(##)\n");
		rofs.readLine(); //Ignore first line 
		while ((ljf = rofs.readLine()) != null) {
			wa = ljf.split("\t");
			if (wa[5].trim().equals("atemporal")) {
				THW.write(wa[0] + "\t" + wa[1] + "\t" + wa[2] + "\t"
						+ (Integer.parseInt(wa[3])) + "\t" + wa[4] + "\t"
						+ wa[5] + "\t" + wa[6] + "\t##\n");
			}

		}
		ross.readLine();  //Ignore first line 
		while ((ljf = ross.readLine()) != null) {
			wa = ljf.split("\t");
				THW.write(wa[0] + "\t" + wa[1] + "\t" + wa[2] + "\t"
						+ (Integer.parseInt(wa[3])) + "\t" + wa[4] + "\t"
						+ wa[5] + "\t" + wa[6] + "\t##\n");
		}
		

		rofs.close();
		ross.close();
		THW.close();

	}
}
