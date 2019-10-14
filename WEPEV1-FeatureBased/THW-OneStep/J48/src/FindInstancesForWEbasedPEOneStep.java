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


public class FindInstancesForWEbasedPEOneStep {

	public static void main(String args[])throws Exception{
				
	BufferedReader testtrain = null;   //testing file in train format
	BufferedReader testseed = null;    // test file in seed format
	BufferedReader prediction = null;  // prediction file generated after testing
	BufferedReader expntrain=null;
	int nopi=0,nopri=0,nofi=0,noni=0,noati=0;
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
					if(etv[200].trim().equals("past"))nopi++;
					else{
						if(etv[200].trim().equals("present"))nopri++;
						else{
							if(etv[200].trim().equals("future"))nofi++;
							else{
								if(etv[200].trim().equals("neutral"))noni++;
								else{
								    noati++;
								}
							}
						}
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
				int noofpastinstancesadded=0,noofpresentinstancesadded=0,nooffutureinstancesadded=0,noofneutralinstancesadded=0,noofatemporalinstancesadded=0;
				expndetails.write("IterationNo\tWord\tWord_Offset\tPOS\tSenseno\tGloss\tPredictedtense\tPredictionValue\n");
				 while (iterator.hasNext()) {
					 
					if((noofpastinstancesadded<(25*nopi/100)||noofpresentinstancesadded<(25*nopri/100)||nooffutureinstancesadded<(25*nofi/100)
							||noofneutralinstancesadded<(25*noni/100)||noofatemporalinstancesadded<(25*noati/100))){
						switch((t=iterator.next()).getPredictedtense().trim()){
						
						case "past":if(noofpastinstancesadded<(25*nopi/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for( i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
							            noofpastinstancesadded++;
							            iterator.remove();  // added to train & seed so remove it from test
						             }
						             break;
						case "present":if(noofpresentinstancesadded<(25*nopri/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for( i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
							            noofpresentinstancesadded++;
							            iterator.remove();  // added to train & seed so remove it from test
						             }
			             			 break;
						case "future":if(nooffutureinstancesadded<(25*nofi/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for( i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
							            nooffutureinstancesadded++;
							            iterator.remove();  // added to train & seed so remove it from test
						             }
						             break;
						case "neutral":if(noofneutralinstancesadded<(25*noni/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for(i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t"+t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getPredictionValue()+"\n");
							            noofneutralinstancesadded++;
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
				 expndetails.write("No. of past instances that should have been added: "+(25*nopi/100)+"\n");
				 expndetails.write("No. of present instances that should have been  added: "+25*(nopri)/100+"\n");
				 expndetails.write("No. of future instances that should have been added: "+(25*nofi/100)+"\n");
				 expndetails.write("No. of neutral instances that should have been  added: "+25*(noni)/100+"\n");
				 expndetails.write("No. of atemporal instances that should have been  added: "+25*(noati)/100+"\n");
				 expndetails.write("-------------------------------NO of Instances Of each class that are actually added---------------------\n");
                 
				 expndetails.write("no of past instances added in this iteration: "+noofpastinstancesadded+"\n");
				 expndetails.write("no of present instances added in this iteration"+noofpresentinstancesadded+"\n");
				 expndetails.write("no of future instances added in this iteration"+nooffutureinstancesadded+"\n");
				 expndetails.write("no of neutral instances added in this iteration: "+noofneutralinstancesadded+"\n");
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
