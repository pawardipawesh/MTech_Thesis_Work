import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class AssimulatePrediction_FileToBeClassified {
	/* precautions : Program is for first step. To  apply it to second step 
	 * work on commented section
	 * secondly in each prediction file one extra line is always there at last which creates null pointer exception as no of entries in 
	 * prediction file becomes one more than file to be classified.
	 */
	public static void Assimulate(String f1,String f2,String f3)throws Exception{
		BufferedReader predictionFile = null;
		BufferedReader FileToBeClassified = null;
		Writer prediction_FileToBeClassified = null;
		
		predictionFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(f1), "UTF8"));
		FileToBeClassified = new BufferedReader(new InputStreamReader(
				new FileInputStream(f2), "UTF8"));
		prediction_FileToBeClassified=new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(f3),
						"UTF8"));
		int c=1;
		while(c<6){
			predictionFile.readLine();
			c++;
		}
		c=0;
		String wordsArrayForpredictionFile[];
		String wordsArrayForFileToBeClassified[];
		String senseno;
		int sn,j=2;
		String lP,lFTBC,G;
		prediction_FileToBeClassified.write("Word_Offset\tWord\tPOS\tSense_no\tGloss\tClass\tPrediction_Value\tEndMarker(##)\n");
		while((lP=predictionFile.readLine())!=null){
 
			G="";
			wordsArrayForpredictionFile=lP.split("\\s+");
			lFTBC=FileToBeClassified.readLine();
			/*if((lFTBC=FileToBeClassified.readLine())==null){
				FileToBeClassified = new BufferedReader(new InputStreamReader(
						new FileInputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/SecondStep/TestSet/TestSetInChunks/dataset"+j+".txt"), "UTF8"));
				lFTBC=FileToBeClassified.readLine();
				j++;
			}*/
			
			if(lFTBC==null){
				System.out.println(c);
			}
			wordsArrayForFileToBeClassified=lFTBC.split("\\s+");
			senseno=wordsArrayForFileToBeClassified[3]; 
			sn=Integer.parseInt(senseno);
			int index=wordsArrayForpredictionFile[wordsArrayForpredictionFile.length-2].indexOf(":");
			if(index!=1)System.out.println(wordsArrayForpredictionFile[wordsArrayForpredictionFile.length-4]);
			String s= wordsArrayForpredictionFile[wordsArrayForpredictionFile.length-2].substring(index+1,wordsArrayForpredictionFile[wordsArrayForpredictionFile.length-2].length() );
			if(s.equals("atempora"))s="atemporal";
			for(int i=4;i<wordsArrayForFileToBeClassified.length;i++){
				G=G.concat(wordsArrayForFileToBeClassified[i]+" ");
				
			}
			prediction_FileToBeClassified.write(wordsArrayForFileToBeClassified[1]+"\t"+wordsArrayForFileToBeClassified[0]+"\t"+wordsArrayForFileToBeClassified[2]+"\t"+String.valueOf(sn)+"\t"+G+"\t"+s+"\t"+wordsArrayForpredictionFile[wordsArrayForpredictionFile.length-1]+"\t##\n");
			
			c++;
		}
		predictionFile.close();
		FileToBeClassified.close();
		prediction_FileToBeClassified.close();
		
		
	}
	
	public static void main(String args[])throws Exception{
		
		Assimulate(args[0],args[1],args[2]);
	}
	
	
	

}
