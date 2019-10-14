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
import java.util.ArrayList;
import java.util.Collections;


public class RandomlyDistributeTS {
	
	public static void RDTS(String noofinstancestobeignored){
		
		BufferedReader TempoAtempotrainingfile = null;
		BufferedReader seedwordsfile=null;
		listTS lts[]=new listTS[200];
		int count=0;
		try{
			TempoAtempotrainingfile = new BufferedReader(new InputStreamReader(
					new FileInputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/Hindi_wordnet_time_tagging_OneStep/TrainingSet/TrainingSet.arff"), "UTF8"));
			seedwordsfile = new BufferedReader(new InputStreamReader(
					new FileInputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/Hindi_wordnet_time_tagging_OneStep/TrainingSet/TrainingSeedWords.txt"), "UTF8"));
		 
	            String LJF,wordsArrayForTF[],wordsArrayForSeedWordsFile[],LJFFSWF,s;
	            int id;
		        
			while ((LJF= TempoAtempotrainingfile.readLine()) != null) {          // LJFFTATF-----line
																				// just
																				// fetched
																				// from
																				// tempo-atempo
				if(count<Integer.parseInt(noofinstancestobeignored));
				else{// training
																				// file
				wordsArrayForTF=LJF.split("',");
				id=wordsArrayForTF[0].indexOf("'", 0);
				 s=wordsArrayForTF[0].substring(id+1,(wordsArrayForTF[0].length()));
				LJFFSWF=seedwordsfile.readLine();
				wordsArrayForSeedWordsFile=LJFFSWF.split("\t");
				lts[count-Integer.parseInt(noofinstancestobeignored)]=new listTS();
				
				lts[count-Integer.parseInt(noofinstancestobeignored)].tense=wordsArrayForTF[1];
				lts[count-Integer.parseInt(noofinstancestobeignored)].senseno=Integer.parseInt(wordsArrayForSeedWordsFile[1].trim());
				lts[count-Integer.parseInt(noofinstancestobeignored)].word=wordsArrayForSeedWordsFile[0];
				lts[count-Integer.parseInt(noofinstancestobeignored)].pos=wordsArrayForSeedWordsFile[2];
				lts[count-Integer.parseInt(noofinstancestobeignored)].G=s;
				
		
	         }
				count++;

          }
		} catch (FileNotFoundException e) {
			System.err.println("Error opening seed words or predictions or training  file." + e);
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}catch(IOException e){
			System.out.println(e);
		}
		Writer trainingfile = null;
		Writer seedfile = null;

		try {
			trainingfile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/Hindi_wordnet_time_tagging_OneStep/TrainingSet/TrainingSetAfterRandomization.arff"),
							"UTF8"));
			seedfile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/Hindi_wordnet_time_tagging_OneStep/TrainingSet/TrainingSeedWordsAfterRandomization.arff"),
							"UTF8"));

		
		ArrayList<Integer> AL=new ArrayList<Integer>();
		for(int i=0;i<(count-Integer.parseInt(noofinstancestobeignored));i++){
			AL.add(new Integer(i));
		}
		Collections.shuffle(AL);
		for(int i=0;i<(count-Integer.parseInt(noofinstancestobeignored));i++){
			
			trainingfile.write("'"+lts[AL.get(i)].G+"',"+lts[AL.get(i)].tense+"\n");
			seedfile.write(lts[AL.get(i)].word+"\t"+lts[AL.get(i)].senseno+"\t"+lts[AL.get(i)].pos+"\t"+lts[AL.get(i)].tense+"\n");
		}
		} catch (FileNotFoundException e) {
			System.err.println("Error opening trainingset file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}catch(Exception e){
			System.out.println(e);
		}finally{
			try{
				trainingfile.close();
				seedfile.close();
				TempoAtempotrainingfile.close();
				seedwordsfile.close();
			}catch(Exception e){
				System.out.println(e);
			}
			
		}
		
	}
	public static void main(String args[]){
		
		RDTS(args[0]);
	}
}

class listTS{
	
	String G;
	String tense;
	String word;
	String pos;
	int senseno;
	listTS next;
}
