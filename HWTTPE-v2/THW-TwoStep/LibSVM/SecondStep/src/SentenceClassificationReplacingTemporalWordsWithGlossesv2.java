import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

public class SentenceClassificationReplacingTemporalWordsWithGlossesv2 {

	public static void main(String args[]) throws Exception {

		BufferedReader sensetaggedPPFVOILTIMEX = null;
		BufferedReader tempohindiwordnet = null;
		Writer TWEWGFCFSFSCUMCWHACC = null;

		sensetaggedPPFVOILTIMEX = new BufferedReader(new InputStreamReader(
				new FileInputStream(args[0]), "UTF8"));
		tempohindiwordnet = new BufferedReader(new InputStreamReader(
				new FileInputStream(args[1]), "UTF8"));

		TWEWGFCFSFSCUMCWHACC = new BufferedWriter(new OutputStreamWriter( /*
																		 * Temporal
																		 * words
																		 * expanded
																		 * with
																		 * glosses
																		 * for
																		 * creating
																		 * file
																		 * suitable
																		 * for
																		 * sentence
																		 * classification
																		 * using
																		 * model
																		 * with
																		 * highest
																		 * accuracy
																		 */
		new FileOutputStream(args[2]), "UTF8"));

		String ljf, wa[];
        TWEWGFCFSFSCUMCWHACC.write("@relation timetagging\n\n@attribute gloss String\n@attribute tense {past, present, future, neutral}\n\n\n\n@data\n");
		SentenceClassificationReplacingTemporalWordsWithGlossesv2 scg = new SentenceClassificationReplacingTemporalWordsWithGlossesv2();
		HashMap<THWF, String> thw = new HashMap<THWF, String>(); // stores THWF
																	// to class
																	// value(past,present,..)
																	// mapping
		HashMap<THWF, String> thwg = new HashMap<THWF, String>(); // stores THWF
																	// to gloss
																	// mapping

		tempohindiwordnet.readLine(); // To ignore first line
		while ((ljf = tempohindiwordnet.readLine()) != null) { // put entire
																// tempohindiWordNet
																// in hashmap
			THWF thwf = new THWF();
			wa = ljf.split("\t"); // ERROR
			// thwf.setWordOffset(Long.parseLong(wa[0].trim()));
			thwf.setPos(wa[2].trim());
			thwf.setSenseNo(Integer.parseInt(wa[3].trim()));
			thwf.setWord(wa[1].trim());
			thw.put(thwf, wa[5]);
			thwg.put(thwf, wa[4].trim());

		}

		int nooftemporalwordsinsentence;
		while ((ljf = sensetaggedPPFVOILTIMEX.readLine()) != null) {
			nooftemporalwordsinsentence = 0;
			String spl[] = ljf.split("',");
			wa = spl[0].substring(1, spl[0].length()).split("\t");
			// TWEWGFCFSFSCUMCWHACC.write("'");
			for (int i = 0; i < wa.length; i++) {

				String gloss = scg.search(wa[i], thw, thwg);
				// ef.write(wa[i]+"#"+annotationfromtempohindiwordnet+"\t");

				if (gloss.equals("null")&&gloss==null)
					;
				else {

					nooftemporalwordsinsentence++;
					if (nooftemporalwordsinsentence == 1)
						TWEWGFCFSFSCUMCWHACC.write("'" + gloss + " ");
					else
						TWEWGFCFSFSCUMCWHACC.write(gloss + " ");
				}

			}
			if (nooftemporalwordsinsentence != 0)
				TWEWGFCFSFSCUMCWHACC.write("',");
			else {
				TWEWGFCFSFSCUMCWHACC.write("?,");
			}
			TWEWGFCFSFSCUMCWHACC.write(spl[1].trim() + "\n");
			// if(past==present&&present==future&&future==0)noofrandomallocations++;
			// System.out.println(past+"\t"+present+"\t"+future+"\t"+m);
			// TWEWGFCFSFSCUMCWHACC.write(m+"\n");
			/*
			 * if(spl[1].trim().equals(m)){ noofinstancescorrectlyclassified++;
			 * if(past==present&&present==future&&future==0)
			 * noofcorrectrandomallocations++; else{
			 * noofcorrectallocationswithoutrandomness++; } }
			 */

		}
		// System.out.println(c);
		// sctaskaccuracy.write("No of instances randomly allocated time sense:"+noofrandomallocations+"\n");
		// sctaskaccuracy.write("No of instances correctly annotated with time sense with random allocation:"+noofcorrectrandomallocations+"\n");
		// sctaskaccuracy.write("No of instances correctly annotated without random allocation"+noofcorrectallocationswithoutrandomness+"\n");

		// accuracy=(float)noofinstancescorrectlyclassified/noofinstancesinfile;
		// sctaskaccuracy.write("Total Accuracy of sentence classification :"+accuracy);
		// sctaskaccuracy.write("Actual accuracy of sentence classification:"+((float)noofcorrectallocationswithoutrandomness/noofinstancesinfile));

		tempohindiwordnet.close();
		TWEWGFCFSFSCUMCWHACC.close();
		sensetaggedPPFVOILTIMEX.close();
	}

	public String search(String w, HashMap<THWF, String> thw,
			HashMap<THWF, String> thwg) throws Exception { // कमिटी#6738#NOUN#2#कमिटी
		String wa[] = w.trim().split("#");
		if (wa[wa.length - 1].trim().equals("null")) {
			return "null";
		} else {
			// System.out.println(
			THWF thwf = new THWF();
			thwf.setPos(wa[2].trim());
			thwf.setSenseNo(Integer.parseInt(wa[3].trim()));
			// thwf.setWordOffset(Long.parseLong(wa[1].trim()));
			thwf.setWord(wa[4].trim());

			if (!thw.containsKey(thwf)) {
				System.out.println(thwf.getWord() + "  " + thwf.getSenseNo()
						+ " " + thwf.getPos());
			}
			if(thw.get(thwf)!=null){
				if (thw.get(thwf).trim().equals("past")
					|| thw.get(thwf).trim().equals("present")
					|| thw.get(thwf).trim().equals("future"))
				return thwg.get(thwf);
			}
		}
		return "null";

	}

}
