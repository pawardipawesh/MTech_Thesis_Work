
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;




public class AccuracyFinder {

	
	public static void main(String args[])throws Exception{
		Double Accuracy=AF(args[0]);
		System.out.println(Accuracy);
	}
	
	public static Double AF(String f1)throws Exception{
		
		BufferedReader performance = null;
		String wordsArray[];

			performance = new BufferedReader(new InputStreamReader(
					new FileInputStream(f1),"UTF8"));
		
		
		
		while(!performance.readLine().trim().equals("=== Stratified cross-validation ===")){
			
			
			
		}
		performance.readLine();
		wordsArray=performance.readLine().split("\\s+");
	
        performance.close();
				
		
		return Double.valueOf(wordsArray[4]);
		
		
		
				
		
		
	}
}
