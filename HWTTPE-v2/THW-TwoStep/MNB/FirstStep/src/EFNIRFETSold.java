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
public class EFNIRFETSold {
	
	public static void extract(){
		
	// ETSF--  Expanded training set file
	// ESWF -- Expanded seed word file
	BufferedReader ETSF = null;
	BufferedReader ESWF = null;
	
	try {
		ETSF = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						"/home/dipawesh/workspace/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/Expanded_Training_data/NaiveBayes/0gram/trainingSetforAtemporal-TemporalTwoStep_2Class.arff"), "UTF8"));
		ESWF = new BufferedReader(new InputStreamReader(
				new FileInputStream("/home/dipawesh/workspace/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/Expanded_Training_data/NaiveBayes/0gram/Temporal_atemporal_seed_words_2Class.txt"),
				"UTF8"));

	} catch (FileNotFoundException e) {
		System.err.println("Error opening seed words or training set file.");
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
						"/home/dipawesh/workspace/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/Expanded_Training_data/NaiveBayes/0gram/trainingSetforAtemporal-TemporalTwoStep_2Class.arff"), "UTF8"));
		

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
				new OutputStreamWriter(new FileOutputStream("/home/dipawesh/workspace/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/GoldStandardTestSet/RandTrainSetForTest.txt"),
						"UTF8"));
		  SWFCRTSFT = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("/home/dipawesh/workspace/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/GoldStandardTestSet/seedWordFileCorrspToRandTrainSetForTest.txt"),
						"UTF8"));

	} catch (FileNotFoundException e) {
		System.err.println("Error opening trainingset file.");
		System.exit(-1);
	} catch (UnsupportedEncodingException e) {
		System.err.println("UTF-8 encoding is not supported.");
		System.exit(-1);
	}
	try{
			data x[]=new data[ETSFsize-8];
			String linejustfetchedfromETSF,linejustfetchedfromESWF;
			String wordsArrayforESWF[],wordsArrayforETSF[];
			int ctemporal=0,catemporal=0,noofinstance=0;
			for(int i=0;i<8;i++){
				ETSF.readLine();
				
			}
			while(( linejustfetchedfromETSF=ETSF.readLine())!=null){
				linejustfetchedfromESWF=ESWF.readLine();
				wordsArrayforESWF=linejustfetchedfromESWF.split("\t");
				wordsArrayforETSF=linejustfetchedfromETSF.split("',");
				x[noofinstance]=new data();
				x[noofinstance].Gloss=wordsArrayforETSF[0];
				x[noofinstance].POS=wordsArrayforESWF[2];
				x[noofinstance].classlabel=wordsArrayforESWF[3];
				x[noofinstance].senseno=Integer.parseInt(wordsArrayforESWF[1]);
				x[noofinstance].word=wordsArrayforESWF[0];
				noofinstance++;
			}
			
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<noofinstance;i++){
				
				list.add(new Integer(i));
			}
			Collections.shuffle(list);
			int i=0,index,flagtemporal=0,flagatemporal=0;
			char pos='p';
			while((flagtemporal!=1||flagatemporal!=1)&&(ETSFsize-8)!=i){
				index=list.get(i);
			    if(x[index].classlabel.equals("temporal"))pos='t';
			    else pos='a';
			         
				switch(pos){
				
				case 't': if(flagtemporal!=1){
					                           ctemporal++;
					                          
				                               RTSFTF.write(x[index].Gloss+"',"+x[index].classlabel+"\n");
				                               SWFCRTSFT.write(x[index].word+"\t"+x[index].senseno+"\t"+x[index].POS+"\t"+x[index].classlabel+"\n");
				                               if(ctemporal==500){
				                                            	flagtemporal=1;
				                               }
				                 }
				                i++;
				                break;
				case 'a': if(flagatemporal!=1){
		                                       catemporal++;
		                                       i++;
		                                       RTSFTF.write(x[index].Gloss+"',"+x[index].classlabel+"\n");
				                               SWFCRTSFT.write(x[index].word+"\t"+x[index].senseno+"\t"+x[index].POS+"\t"+x[index].classlabel+"\n");
		                                       if(catemporal==250){
		                         	                         flagatemporal=1;
		                                        }
		                     }
				             i++;
		                     break;
				
		     
	}
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


