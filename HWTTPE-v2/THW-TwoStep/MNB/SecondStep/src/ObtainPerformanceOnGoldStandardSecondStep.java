import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class ObtainPerformanceOnGoldStandardSecondStep {

public static void ObtainPerformance(String f1,String f2)throws Exception{
		
		BufferedReader PredictionsOnGoldStandard = null;
		Writer PerformanceOnGoldStandard = null;
		String ljf;
		
				int c=0;
				int papa=0,papr=0,paf=0,pan=0;             // pa=past  f=future pr=present n=neutral
				int prpa=0,prpr=0,prf=0,prn=0;
				int fpa=0,fpr=0,ff=0,fn=0;
				int npa=0,npr=0,nf=0,nn=0;
				
				float precisionpa,precisionpr,precisionf,precisionn,avgprecision;
				float recallpa,recallpr,recallf,recalln,avgrecall;
				float fmeasurepa,fmeasurepr,fmeasuref,fmeasuren,avgfmeasure,accuracy;
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
							String predictedclass=prediction[3].substring(2, prediction[3].length()).trim();
							
							if(actualclass.equals("past")&&predictedclass.equals("past") )papa++;
							if(actualclass.equals("past")&&predictedclass.equals("present") )papr++;
							if(actualclass.equals("past")&&predictedclass.equals("future") )paf++;
							if(actualclass.equals("past")&&predictedclass.equals("neutral") )pan++;
							
							if(actualclass.equals("present")&&predictedclass.equals("past") )prpa++;
							if(actualclass.equals("present")&&predictedclass.equals("present") )prpr++;
							if(actualclass.equals("present")&&predictedclass.equals("future") )prf++;
							if(actualclass.equals("present")&&predictedclass.equals("neutral") )prn++;
							
							if(actualclass.equals("future")&&predictedclass.equals("past") )fpa++;
							if(actualclass.equals("future")&&predictedclass.equals("present") )fpr++;
							if(actualclass.equals("future")&&predictedclass.equals("future") )ff++;
							if(actualclass.equals("future")&&predictedclass.equals("neutral") )fn++;
							
							if(actualclass.equals("neutral")&&predictedclass.equals("past") )npa++;
							if(actualclass.equals("neutral")&&predictedclass.equals("present") )npr++;
							if(actualclass.equals("neutral")&&predictedclass.equals("future") )nf++;
							if(actualclass.equals("neutral")&&predictedclass.equals("neutral") )nn++;
						}
						
					}
					
					
					
				}
				//System.out.println("ta"+ta+"at"+at+"tt"+tt+"aa"+aa+"c-1"+(c-1));
				PerformanceOnGoldStandard.write("past\tpresent\tfuture\tneutral<-----classified as\n");
				PerformanceOnGoldStandard.write(papa+"    \t"+papr+"    \t"+paf+"    \t"+pan+"      |past\n");
				PerformanceOnGoldStandard.write(prpa+"    \t"+prpr+"    \t"+prf+"    \t"+prn+"       |present\n");
				PerformanceOnGoldStandard.write(fpa+"     \t"+fpr+"    \t"+ff+"    \t"+fn+"       |future\n");
				PerformanceOnGoldStandard.write(npa+"     \t"+npr+"    \t"+nf+"    \t"+nn+"       |neutral\n");
				
				PerformanceOnGoldStandard.write("-------------------Accuracy------------------------------------------------------------\n");
				if(c==0){
					System.out.println("no of instances in line are zero & hence accuracy cann't be calculated");
				}
				else{
					PerformanceOnGoldStandard.write("Accuracy="+((float)(papa+prpr+nn+ff)/(c-6))+"\n\n" );
				}
				//System.out.println(c);
				precisionpa=(float)papa/(papa+prpa+fpa+npa);
				precisionpr=(float)prpr/(papr+prpr+fpr+npr);
				precisionf=(float)ff/(paf+prf+ff+nf);
				precisionn=(float)nn/(pan+prn+fn+nn);
				avgprecision=(float)(precisionpa+precisionpr+precisionf+precisionn)/4;
				
				recallpa=(float)papa/(papa+papr+paf+pan);
				recallpr=(float)prpr/(prpa+prpr+prf+prn);
				recallf=(float)ff/(fpa+fpr+ff+fn);
				recalln=(float)nn/(npa+npr+nf+nn);
				avgrecall=(float)(recallpa+recallpr+recallf+recalln)/4;
				
			    
			    fmeasurepa=2*recallpa*precisionpa/(float)(recallpa+precisionpa);
			    fmeasurepr=2*recallpr*precisionpr/(float)(recallpr+precisionpr);
			    fmeasuref=2*recallf*precisionf/(float)(recallf+precisionf);
			    fmeasuren=2*recalln*precisionn/(float)(recalln+precisionn);
			    
			    avgfmeasure=2*avgprecision*avgrecall/(avgprecision+avgrecall);
			    
			    PerformanceOnGoldStandard.write("--------------------Precision--------------------------------------------------------\n");
				
				
				PerformanceOnGoldStandard.write("Precision(past)="+precisionpa+"\n");
				PerformanceOnGoldStandard.write("Precision(present)="+precisionpr+"\n");
				PerformanceOnGoldStandard.write("Precision(future)="+precisionf+"\n");
				PerformanceOnGoldStandard.write("Precision(neutral)="+precisionn+"\n");
				PerformanceOnGoldStandard.write("Precision(avg)="+avgprecision+"\n");
				
				PerformanceOnGoldStandard.write("---------------------Recall-----------------------------------------------------------\n");
				PerformanceOnGoldStandard.write("recall(past)="+recallpa+"\n");
				PerformanceOnGoldStandard.write("recall(present)="+recallpr+"\n");
				PerformanceOnGoldStandard.write("recall(future)="+recallf+"\n");
				PerformanceOnGoldStandard.write("recall(neutral)="+recalln+"\n");
				PerformanceOnGoldStandard.write("recall(avg)="+avgrecall+"\n");
				
				PerformanceOnGoldStandard.write("---------------------F-measure-----------------------------------------------------------\n");
				PerformanceOnGoldStandard.write("F-measure(past)="+fmeasurepa+"\n");
				PerformanceOnGoldStandard.write("F-measure(present)="+fmeasurepr+"\n");
				PerformanceOnGoldStandard.write("F-measure(future)="+fmeasuref+"\n");
				PerformanceOnGoldStandard.write("F-measure(neutral)="+fmeasuren+"\n");
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
