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

public class PrepareDataToBeClassified {

	static void PDTBC() {
		Writer fileToBeClassified = null;
		Writer fileToBeClassifiedDetailed = null;

		try {
			fileToBeClassified = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("fileToBeClassified.txt"), "UTF8"));
			fileToBeClassifiedDetailed = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							"fileToBeClassifiedDetailed.txt"), "UTF8"));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening fileToBeClassified");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		JHWNL.initialize();
		IndexWord IW;
		String G;
		int index;
		Synset S[];

		try {
			String adj[] = Dictionary.getInstance().getAdjWordList();
			String adv[] = Dictionary.getInstance().getAdvWordList();
			String verb[] = Dictionary.getInstance().getVerbWordList();
			String noun[] = Dictionary.getInstance().getNounWordList();

			for (int i = 0; i < adj.length; i++) {
				IW = Dictionary.getInstance().getIndexWord(POS.ADJECTIVE,
						adj[i]);
				S = IW.getSenses();
				for (int j = 0; j < S.length; j++) {
					G = S[j].getGloss();
					index = G.indexOf(":");
					if(index != -1) {
						G = G.replace(G, G.substring(0, index));
						fileToBeClassifiedDetailed.write(adj[i] + "\t"
								+ S[0].getOffset() + "\t ADJECTIVE \t" + j + "\t"
								+ G + "\n");
						fileToBeClassified.write("'"+G+"',? \n");
					} else {
						fileToBeClassifiedDetailed.write(adj[i] + "\t"
								+ S[0].getOffset() + "\t ADJECTIVE \t" + j + "\t"
								+ G + "\n");
						fileToBeClassified.write("'"+G+"',? \n");
					}
				}

			}

			for (int i = 0; i < adv.length; i++) {
				IW = Dictionary.getInstance().getIndexWord(POS.ADVERB, adv[i]);
				S = IW.getSenses();
				for (int j = 0; j < S.length; j++) {
					G = S[j].getGloss();
					index = G.indexOf(":");
					G = G.replace(G, G.substring(0, index));
					fileToBeClassifiedDetailed.write(adv[i] + "\t"
							+ S[0].getOffset() + "\t ADVERB \t" + j + "\t" + G
							+ "\n");
					fileToBeClassified.write("'"+G+"',? \n");
				}

			}

			for (int i = 0; i < verb.length; i++) {
				IW = Dictionary.getInstance().getIndexWord(POS.VERB, verb[i]);
				S = IW.getSenses();
				for (int j = 0; j < S.length; j++) {
					G = S[j].getGloss();
					index = G.indexOf(":");
					G = G.replace(G, G.substring(0, index));
					fileToBeClassifiedDetailed.write(verb[i] + "\t"
							+ S[0].getOffset() + "\t VERB \t" + j + "\t" + G
							+ "\n");
					fileToBeClassified.write("'"+G+"',? \n");
				}

			}

			for (int i = 0; i < noun.length; i++) {
				IW = Dictionary.getInstance().getIndexWord(POS.NOUN, noun[i]);
				S = IW.getSenses();
				for (int j = 0; j < S.length; j++) {
					G = S[j].getGloss();
					index = G.indexOf(":");
					if(index != -1) {
					G = G.replace(G, G.substring(0, index));
					fileToBeClassifiedDetailed.write(noun[i] + "\t"
							+ S[0].getOffset() + "\t NOUN \t" + j + "\t" + G
							+ "\n");
					fileToBeClassified.write("'"+G+"',? \n");
					} else {
						fileToBeClassifiedDetailed.write(noun[i] + "\t"
								+ S[0].getOffset() + "\t NOUN \t" + j + "\t" + G
								+ "\n");
						fileToBeClassified.write("'"+G+"',? \n");
					}
				}

			}
		}

		catch (IOException e) {
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
				fileToBeClassifiedDetailed.close();
				fileToBeClassified.close();
			} catch (Exception r) {

			}

		}
	}

	public static void main(String args[]) throws Exception {
		PDTBC();
	}
}
