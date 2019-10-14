import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class PerformanceCollector {
	
	public static void main(String args[])throws Exception{
		PC();
	}
	
	public static void PC()throws Exception{
		
		BufferedReader performance = null;
		BufferedWriter performanceDetails=null;
		String wordsArray[];
        
		for(int i=1;i<=9;i++){
			for(int j=1;j<=5;j++){
				
		
			performance = new BufferedReader(new InputStreamReader(
					new FileInputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/SecondStep/Performance/SVM_x10_iteration"+i+"time"+j+".txt"),"UTF8"));
		    
			performanceDetails = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/home/dipawesh/Dipawesh/Mtech/ProjectHWTT-backup/HWTTwith20%Atemporal&15%temporaladdition&unequalInitialDistributionOfT&AT/Hindi_wordnet_time_tagging/SecondStep/Performance/PerformanceDetails.txt", true),
							"UTF8"));
		
		while(!performance.readLine().trim().equals("=== Stratified cross-validation ===")){
			
			
			
		}
		performance.readLine();
		wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\n\nDETAILS AFTER ITERATION "+i+" TIME "+j+" \n\nAccuracy : "+wordsArray[4]);
		performance.readLine();performance.readLine();performance.readLine();performance.readLine();performance.readLine();performance.readLine();
		
		wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\nTotal no. of instances in training file :  "+wordsArray[4]);	
		
		performance.readLine();performance.readLine();performance.readLine();performance.readLine();performance.readLine();
        
		wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\nFMeasure for past class :  "+  wordsArray[5]);
         
		wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\nFMeasure for present class :  "+  wordsArray[5]);
		
        wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\nFMeasure for future class :  "+  wordsArray[5]);
		
         wordsArray=performance.readLine().split("\\s+");
		
		performanceDetails.write("\nFMeasure for neutral class :  "+  wordsArray[5]); 
		
		wordsArray=performance.readLine().split("\\s+");
        
        performanceDetails.write("\nAverage FMeasure :  "+  wordsArray[6]);
        
        performance.readLine();performance.readLine();performance.readLine();performance.readLine();performance.readLine();
        
        wordsArray=performance.readLine().split("\\s+");
        
        performanceDetails.write("\nNo. of past instances :  "+  (Integer.parseInt(wordsArray[1])+(Integer.parseInt(wordsArray[2])+(Integer.parseInt(wordsArray[3])+(Integer.parseInt(wordsArray[4]))))));
        
        wordsArray=performance.readLine().split("\\s+");
        
        performanceDetails.write("\nNo. of present instances :  "+  (Integer.parseInt(wordsArray[1])+(Integer.parseInt(wordsArray[2])+(Integer.parseInt(wordsArray[3])+(Integer.parseInt(wordsArray[4]))))));
        
        wordsArray=performance.readLine().split("\\s+");
        
        performanceDetails.write("\nNo. of future instances :  "+  (Integer.parseInt(wordsArray[1])+(Integer.parseInt(wordsArray[2])+(Integer.parseInt(wordsArray[3])+(Integer.parseInt(wordsArray[4]))))));
        
 wordsArray=performance.readLine().split("\\s+");
        
        performanceDetails.write("\nNo. of neutral instances :  "+  (Integer.parseInt(wordsArray[1])+(Integer.parseInt(wordsArray[2])+(Integer.parseInt(wordsArray[3])+(Integer.parseInt(wordsArray[4]))))));
        
         
        performanceDetails.write("\n------------------------------------------------------------------------------");
        performance.close();
        performanceDetails.close();
				
		
			}
		}	
		
		
		
				
		
		
	}
}


