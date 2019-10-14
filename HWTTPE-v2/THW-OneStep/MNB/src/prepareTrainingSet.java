//package in.ac.iitb.cfilt.jhwnl.examples;

import in.ac.iitb.cfilt.jhwnl.JHWNL;
import in.ac.iitb.cfilt.jhwnl.JHWNLException;
import in.ac.iitb.cfilt.jhwnl.data.IndexWord;
import in.ac.iitb.cfilt.jhwnl.data.IndexWordSet;
import in.ac.iitb.cfilt.jhwnl.data.Pointer;
import in.ac.iitb.cfilt.jhwnl.data.PointerType;
import in.ac.iitb.cfilt.jhwnl.data.Synset;
import in.ac.iitb.cfilt.jhwnl.data.POS;
import in.ac.iitb.cfilt.jhwnl.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileOutputStream;

public class prepareTrainingSet {

	static void PTS() {

		BufferedReader seedWordsFile = null;
		try {
			seedWordsFile = new BufferedReader(new InputStreamReader(
					new FileInputStream("Temporal_atemporal_seed_words.txt"), "UTF8"));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening seed words file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		Writer seedwordsdetailsFile = null;
		Writer trainingFile = null;

		try {
			seedwordsdetailsFile = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Temporal_atemporal_seed_words_detailed.txt"), "UTF8"));
			trainingFile = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("trainingSetforAtemporalTemporal.txt"), "UTF8"));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening trainingset file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		JHWNL.initialize();
		String lineJustFetched = null;
		String wordsArray[];
		Synset s;
		String g, soffset;
		IndexWord IW = null;
		long offset;
		POS pos;
		int index;
		try {
			while (true) {
				lineJustFetched = seedWordsFile.readLine();
				if (lineJustFetched == null) {
					break;
				} else {
					//System.out.println("lineJustFetched : " + lineJustFetched);
					wordsArray = lineJustFetched.split("\t");
					//System.out.println(wordsArray[0] + ":" + wordsArray[1]+ ":" + wordsArray[2] + ":" + wordsArray[3]);
					if (wordsArray[2].equals("NOUN")) {
						IW = Dictionary.getInstance().getIndexWord(POS.NOUN,
								wordsArray[0]);
					} else if (wordsArray[2].equals("VERB")) {
						IW = Dictionary.getInstance().getIndexWord(POS.VERB,
								wordsArray[0]);
					} else if (wordsArray[2].equals("ADVERB")) {
						IW = Dictionary.getInstance().getIndexWord(POS.ADVERB,
								wordsArray[0]);
					} else if (wordsArray[2].equals("ADJECTIVE")) {
						IW = Dictionary.getInstance().getIndexWord(
								POS.ADJECTIVE, wordsArray[0]);
					}
					s = IW.getSense(Integer.parseInt(wordsArray[1]) - 2);

					g = s.getGloss();
					index = g.indexOf(":");
					g = g.replace(g, g.substring(0, index));
					offset = s.getOffset();
					pos = s.getPOS();
					soffset = String.valueOf(offset);
					seedwordsdetailsFile.write(wordsArray[0]);
					seedwordsdetailsFile.write("\t");
					seedwordsdetailsFile.write(wordsArray[2]);
					seedwordsdetailsFile.write("\t");
					seedwordsdetailsFile.write(soffset);
					seedwordsdetailsFile.write("\t");
					seedwordsdetailsFile.write(g + "\t");
					seedwordsdetailsFile.write(wordsArray[3] + "\n");
					trainingFile.write("'"+g+"',"+wordsArray[3]+"\n");

				}
			}
		} catch (IOException e) {
			System.err.println("Error in input/output.");
			e.printStackTrace();
		} catch (JHWNLException e) {
			System.err.println("Internal Error raised from API.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Internal Error raised from API.");
			e.printStackTrace();
		} finally {
			try {
				seedwordsdetailsFile.close();
				trainingFile.close();
			} catch (Exception r) {

			}
		}

	}

	public static void main(String args[]) throws Exception {
		PTS();
	}
}
