import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class ObtainPerformanceOnGoldStandardFirstStep {

	public static void ObtainPerformance(String f1,String f2)throws Exception{
		
		BufferedReader PredictionsOnGoldStandard = null;
		Writer PerformanceOnGoldStandard = null;
		String ljf;
		
				int c=0;
				int tt=0,ta=0,at=0,aa=0;
				float precisiont,precisionat,recallt,recallat,avgprecision,avgrecall,fmeasuret,fmeasureat,avgfmeasure,accuracy;
				try{	
				PredictionsOnGoldStandard = new BufferedReader(new InputStreamReader(
						new FileInputStream(f1), "UTF8"));
				PerformanceOnGoldStandard = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(f2), "UTF8"));
				
				while((ljf=PredictionsOnGoldStandard.readLine())!=null ){
					
					c++;
					if(c<6);
					else{
							if(!ljf.trim().isEmpty()){
							String prediction[]=ljf.split("\\s+");
							
							String actualclass=prediction[2].substring(2, prediction[2].length()).trim();
							String predictedclass=prediction[3].substring(2, prediction[2].length()).trim();
							
							if(actualclass.equals("temporal")&&predictedclass.equals("atempora") )ta++;
							if(actualclass.equals("temporal")&&predictedclass.equals("temporal") )tt++;
							if(actualclass.equals("atempora")&&predictedclass.equals("temporal") )at++;
							if(actualclass.equals("atempora")&&predictedclass.equals("atempora") )aa++;
						}
						
					}
					
					
					
				}
				System.out.println("ta"+ta+"at"+at+"tt"+tt+"aa"+aa+"c-1"+(c-1));
				PerformanceOnGoldStandard.write("temporal\tatemporal\t<-----classified as\n");
				PerformanceOnGoldStandard.write(tt+"        \t"+ta+"          \t      |temporal\n");
				PerformanceOnGoldStandard.write(at+"        \t"+aa+"           \t      |aemporal\n\n");
				PerformanceOnGoldStandard.write("-------------------Accuracy------------------------------------------------------------\n");
				if(c==0){
					System.out.println("no of instances in line are zero & hence accuracy cann't be calculated");
				}
				else{
					PerformanceOnGoldStandard.write("Accuracy="+((float)(tt+aa)/(c-6))+"\n\n" );
				}
				System.out.println(c);
				precisiont=(float)tt/(tt+at);
				precisionat=(float)aa/(aa+ta);
				avgprecision=(float)(precisionat+precisiont)/2;
				recallt=(float)tt/(tt+ta);
			    recallat=(float)aa/(aa+at);
			    avgrecall=(float)(recallat+recallt)/2;
			    fmeasuret=2*recallt*precisiont/(float)(recallt+precisiont);
			    fmeasureat=2*recallat*precisionat/(float)(recallat+precisionat);
			    avgfmeasure=2*avgprecision*avgrecall/(avgprecision+avgrecall);
			    
			    PerformanceOnGoldStandard.write("--------------------Precision--------------------------------------------------------\n");
				
				
				PerformanceOnGoldStandard.write("Precision(temporal)="+precisiont+"\n");
				PerformanceOnGoldStandard.write("Precision(atemporal)="+precisionat+"\n");
				PerformanceOnGoldStandard.write("Precision(avg)="+avgprecision+"\n");
				PerformanceOnGoldStandard.write("---------------------Recall-----------------------------------------------------------\n");
				PerformanceOnGoldStandard.write("recall(temporal)="+recallt+"\n");
				PerformanceOnGoldStandard.write("recall(atemporal)="+recallat+"\n");
				PerformanceOnGoldStandard.write("recall(avg)="+avgrecall+"\n");
				PerformanceOnGoldStandard.write("---------------------F-measure-----------------------------------------------------------\n");
				PerformanceOnGoldStandard.write("F-measure(temporal)="+fmeasuret+"\n");
				PerformanceOnGoldStandard.write("F-measure(atemporal)="+fmeasureat+"\n");
				PerformanceOnGoldStandard.write("F-measure(avg)="+avgfmeasure);
		}catch(IOException e){
			
			System.out.println("File read/write Exception"+e);
		}finally{
			try{
				PredictionsOnGoldStandard.close();
				PerformanceOnGoldStandard.close();
			}catch(IOException e){
				System.out.println("File close exception"+e);
			}
			
		}	
	}
	
	public static void main(String args[])throws Exception{
		
		
		ObtainPerformance(args[0],args[1]);
		
	}
	
	
}
