import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.swing.tree.ExpandVetoException;


public class FindInstancesForWEbasedPEFirstStep {

	public static void main(String args[])throws Exception{
				
	BufferedReader testtrain = null;   //testing file in train format
	BufferedReader testseed = null;    // test file in seed format
	BufferedReader prediction = null;  // prediction file generated after testing
	BufferedReader expntrain=null;
	int noti=0,noati=0;
		try{
			testtrain = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[0]), "UTF8"));
			testseed = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[1]), "UTF8"));
			prediction = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[2]), "UTF8"));
			expntrain = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[3]), "UTF8"));
			
				String ljffet,et[],etv[];
				
				
				
				while(!expntrain.readLine().equals("@data"));
				expntrain.readLine();
				while((ljffet=expntrain.readLine())!=null){
					et=ljffet.split("',");
					etv=et[1].split(",");
					if(etv[200].trim().equals("temporal"))noti++;
					else{
							 noati++;	
					}	
				}
				
				expntrain.close();
			
			}catch(IOException e){
			      System.out.println("I/o Error :\n"+e);
		     }
						
			Writer expntrainA = null;           // train file which need to be expanded
			Writer expnseedA = null;            //seed file which need to be expanded	
			Writer expndetails=null;
			Writer testtrainW = null;
			Writer testseedW = null;
			String s[];  // to store first 206 lines of test train file
			try{
				s=new String[207];
				expntrainA=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[3],true), "UTF8"));      // Append Mode
				expnseedA=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[4],true), "UTF8"));
				expndetails=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[5],true), "UTF8"));
			    int iterationNo=Integer.parseInt(args[6]);
				ArrayList<TestInstancesInfo> al=new ArrayList<TestInstancesInfo>();
				String ljffp,ljffts,ljfftt;   // line just fetched from
				String p[],ts[],tt[],ttv[];
				String predictedtense;
				int i=0;
				while(!(ljfftt=testtrain.readLine()).trim().equals("@data")){
					s[i]=ljfftt;
					i++;
				} // ignore part of the test file used for obtaining prediction till @data 
				s[i]=ljfftt;
				i++;
				s[i]=testtrain.readLine();
				prediction.readLine();prediction.readLine();prediction.readLine();prediction.readLine();prediction.readLine();//ignore first 5 lines in prediction file
				
				while(!(ljffp=prediction.readLine()).isEmpty()){
					ljffts=testseed.readLine();
					ljfftt=testtrain.readLine();
					p=ljffp.split("\\s+");
					ts=ljffts.split("\t");
					tt=ljfftt.split("',");
					ttv=tt[1].split(",");
					TestInstancesInfo tii=new TestInstancesInfo();
					tii.setGloss(ts[4].trim());
					tii.setOffset(Long.parseLong(ts[1].trim()));
					tii.setPos(ts[2].trim());
					predictedtense = p[p.length - 2].replace(p[p.length - 2], p[p.length - 2].substring(p[p.length - 2].indexOf(":") + 1, p[p.length - 2].length()));
					tii.setPredictedtense(predictedtense);
					tii.setPredictionValue(Float.parseFloat(p[p.length - 1]));
					tii.setSenseno(Integer.parseInt(ts[3].trim()));
					float f[]=new float[200];
					for(i=0;i<200;i++){
						f[i]=Float.parseFloat(ttv[i]);
					}
					tii.setVector(f);
					tii.setWord(ts[0]);
					al.add(tii);
					
				}
				testtrain.close();
				testseed.close();
				
				
				testtrainW=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[0]), "UTF8")); 
				testseedW=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[1]), "UTF8")); 
				
				
				Collections.sort(al);
				TestInstancesInfo t;
				java.util.Iterator<TestInstancesInfo> iterator = al.iterator();
				int nooftemporalinstancesadded=0,noofatemporalinstancesadded=0;
				expndetails.write("IterationNo\tWord\tWord_Offset\tPOS\tSenseno\tGloss\tPredictedtense\tPredictionValue\n");
				 while (iterator.hasNext()) {
					 
					if((nooftemporalinstancesadded<(25*noti/100)||noofatemporalinstancesadded<(25*noati/100))){
						switch((t=iterator.next()).getPredictedtense().trim()){
						
						case "temporal":if(nooftemporalinstancesadded<(25*noti/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for( i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
							            nooftemporalinstancesadded++;
							            iterator.remove();  // added to train & seed so remove it from test
						             }
						             break;
						case "atempora":if(noofatemporalinstancesadded<(25*noati/100)){
								            expntrainA.write("'"+t.getGloss()+"',");
								            float f[]=t.getVector();
								            for( i=0;i<200;i++){
								            	expntrainA.write(f[i]+",");
								            }
								            
								            expntrainA.write(t.getPredictedtense()+"l"+"\n");
								            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"l"+"\n");
								            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
								            noofatemporalinstancesadded++;
								            iterator.remove();  // added to train & seed so remove it from test
							             }
							             break;
						}
			       
					
					
				}else break;
		 }
				 java.util.Iterator<TestInstancesInfo> iterator1 = al.iterator();
				 for(i=0;i<207;i++){             
					 testtrainW.write(s[i]+"\n");             // replicate first 207 lines as it is
				 }
				 while(iterator1.hasNext()){
					 
					 t=new TestInstancesInfo();
					 t=iterator1.next(); 
					 testtrainW.write("'"+t.getGloss()+"',");
					 float f[]=t.getVector();
					 for(i=0;i<200;i++){
						 testtrainW.write(f[i]+",");
					 }
					 testtrainW.write("?"+"\n");
					 testseedW.write(t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+"\n");
				 }
				 
				 expndetails.write("------------------------------NO of Instances Of each class that should have been added------------------------------------------------\n");
				 expndetails.write("No. of temporal instances that should have been added: "+(25*noti/100)+"\n");
				 expndetails.write("No. of atemporal instances that should have been  added: "+25*(noati)/100+"\n");
				 expndetails.write("-------------------------------NO of Instances Of each class that are actually added---------------------\n");
                 
				 expndetails.write("no of temporal instances added in this iteration: "+nooftemporalinstancesadded+"\n");
				 expndetails.write("no of atemporal instances added in this iteration: "+noofatemporalinstancesadded+"\n");
				 expndetails.write("*******************************Iteration "+iterationNo+" Over********************************************\n");
	}catch(IOException e){
		System.out.println("I/o Error\n"+e);
	}finally{
		try{
			testtrain.close();
			testseed.close();
			expntrainA.close();
			expnseedA.close();
			prediction.close();
			expndetails.close();
			testtrainW.close();
			testseedW.close();
		}catch(Exception e){
			System.out.println("Closing error\n"+e);
		}
	}

  }
}
