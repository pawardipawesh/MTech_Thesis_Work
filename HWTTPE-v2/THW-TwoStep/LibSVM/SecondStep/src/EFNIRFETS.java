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

// EFNIRFETS---Extract Fixed No Of Instances Randomly From Expanded Training Set
public class EFNIRFETS {
	
	public static void extract(){
		
	// ETSF--  Expanded training set file
	// ESWF -- Expanded seed word file
	BufferedReader ETSF = null;
	BufferedReader ESWF = null;
	
	try {
		ETSF = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						"/home/dipawesh/workspace/Hindi_wordnet_time_tagging/TestData/DatasetInChunks/ngram_data_set10.arff"), "UTF8"));
		ESWF = new BufferedReader(new InputStreamReader(
				new FileInputStream("/home/dipawesh/workspace/Hindi_wordnet_time_tagging/TestData/DatasetInChunks/dataset10"),
				"UTF8"));

	} catch (FileNotFoundException e) {
		System.err.println("Error opening seed words or ngram data set file.");
		System.exit(-1);
	} catch (UnsupportedEncodingException e) {
		System.err.println("UTF-8 encoding is not supported.");
		System.exit(-1);
	}
	int ETSFsize=0;
	try{
			
			while(ETSF.readLine()!=null){
			   ETSFsize++;
	        }
			
	ETSF.close();
	}catch(IOException e){
		System.out.println(e);
	}
	try {
		ETSF = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						"/home/dipawesh/workspace/Hindi_wordnet_time_tagging/TestData/DatasetInChunks/ngram_data_set10.arff"), "UTF8"));
		

	} catch (FileNotFoundException e) {
		System.err.println("Error opening seed words or training set file.");
		System.exit(-1);
	} catch (UnsupportedEncodingException e) {
		System.err.println("UTF-8 encoding is not supported.");
		System.exit(-1);
	}
	Writer RTSFTF = null;                     //RTSFTF--Random training set for testing , file
	Writer SWFCRTSFT = null;                  //SWFCRTSFT---- seed word file compatible to  RTSFT
	
	try {
		  RTSFTF = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("/home/dipawesh/workspace/Hindi_wordnet_time_tagging/GoldStandardTestSet/TestSet.txt",true),
						"UTF8"));
		  SWFCRTSFT = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("/home/dipawesh/workspace/Hindi_wordnet_time_tagging/GoldStandardTestSet/seedWordDetailsofTestSet.txt",true),
						"UTF8"));

	} catch (FileNotFoundException e) {
		System.err.println("Error opening TestSet or SeedWordsDetails file.");
		System.exit(-1);
	} catch (UnsupportedEncodingException e) {
		System.err.println("UTF-8 encoding is not supported.");
		System.exit(-1);
	}
	try{
			data x[]=new data[ETSFsize-8];
			String linejustfetchedfromETSF,linejustfetchedfromESWF;
			String wordsArrayforESWF[],wordsArrayforETSF[],s;
			int noofinstance=0,id;
			for(int i=0;i<8;i++){
				ETSF.readLine();
				
			}
			while(( linejustfetchedfromETSF=ETSF.readLine())!=null){
				linejustfetchedfromESWF=ESWF.readLine();
				wordsArrayforESWF=linejustfetchedfromESWF.split("\t");
				wordsArrayforETSF=linejustfetchedfromETSF.split("',");
				id=wordsArrayforETSF[0].indexOf("'", 0);
				 s=wordsArrayforETSF[0].substring(id+1,(wordsArrayforETSF[0].length()));
				
				x[noofinstance]=new data();
				x[noofinstance].Gloss=s;
				x[noofinstance].POS=wordsArrayforESWF[2].trim();
				//x[noofinstance].classlabel=wordsArrayforETSF[1].trim();
				
				x[noofinstance].senseno=Integer.parseInt(wordsArrayforESWF[3].trim())+1;
				x[noofinstance].word=wordsArrayforESWF[0].trim();
				noofinstance++;
			}
			
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<noofinstance;i++){
				
				list.add(new Integer(i));
			}
			Collections.shuffle(list);
			int i=0,index;
			while(i<50){
				index=list.get(i);
	            RTSFTF.write("'"+x[index].Gloss+"',\n");
			    SWFCRTSFT.write(x[index].word+"\t"+x[index].senseno+"\t"+x[index].POS+"\n");
			    i++;
		     }
	 }catch(IOException e){
		 System.out.println(e);
	 }finally{
				 try{
					 RTSFTF.close();
					 SWFCRTSFT.close();
					 ESWF.close();
					 ETSF.close();
				 }
				 catch(Exception e){
					 System.out.println(e);
				 }
	    }
 }
	public static void main(String args[]){
		extract();
	}
}
class data
{
	String Gloss;
	String word;
	int senseno;
	String POS;
	String classlabel;
}


