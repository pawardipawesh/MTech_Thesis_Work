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


public class FindInstancesForWEBEFirstStep {
	
	
	public static void main(String args[])throws Exception{
		
		BufferedReader testtrain = null;   //testing file in train format
		BufferedReader testseed = null;    // test file in seed format
		BufferedReader prediction = null;  // prediction file generated after testing
		BufferedReader expntrain=null;
		BufferedReader expnseed=null;
		int noti=0,noati=0;
		ArrayList<TestInstancesInfo> trainseedl=null;
		FindInstancesForWEBEFirstStep fi=new FindInstancesForWEBEFirstStep();
			try{
				testtrain = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[0]), "UTF8"));
				testseed = new BufferedReader(new InputStreamReader(
						new FileInputStream(args[1]), "UTF8"));
				prediction = new BufferedReader(new InputStreamReader(
						new FileInputStream(args[2]), "UTF8"));
				expntrain = new BufferedReader(new InputStreamReader(
						new FileInputStream(args[3]), "UTF8"));
				expnseed = new BufferedReader(new InputStreamReader(
						new FileInputStream(args[4]), "UTF8"));
				
					String ljffet,et[],esf[],ljffes,etv[];
					trainseedl=new ArrayList<TestInstancesInfo>();
					while(!expntrain.readLine().equals("@data"));
					expntrain.readLine();
					while((ljffet=expntrain.readLine())!=null){
						et=ljffet.split("',");
						ljffes=expnseed.readLine();
						esf=ljffes.split("\t");
						etv=et[1].split(",");
						TestInstancesInfo tii=new TestInstancesInfo();
						tii.setGloss(et[0].substring(1, et[0].length()).trim());
						tii.setPos(esf[2].trim());
						tii.setSenseno(Integer.parseInt(esf[1].trim()));
						float f[]=new float[200];
						if(!ljffes.contains("?")){
							for(int i=0;i<200;i++){
							  f[i]=Float.parseFloat(esf[i+3]);
						    }
							tii.setVector(f);
						}else{
							tii.setVector(null);
						}
						
						tii.setWord(esf[0]);
						tii.setPredictedtense(etv[200].trim());
						trainseedl.add(tii);
						
						if(etv[200].trim().equals("temporal"))noti++;
					    else{
						      noati++;
						}
						}
					
					expntrain.close();
				    expnseed.close();
				}catch(IOException e){
				      System.out.println("I/o Error :\n"+e);
			     }
			String ljffts,ts[],p[],ljffpf,ljfftt;
			ArrayList<TestInstancesInfo> testseedl=new ArrayList<TestInstancesInfo>();
			
			int i=0;String s[]=new String[207];
			while(!(ljfftt=testtrain.readLine()).trim().equals("@data")){
				s[i]=ljfftt;
				i++;
			} // ignore part of the test file used for obtaining prediction till @data 
			s[i]=ljfftt;
			i++;
			s[i]=testtrain.readLine();
			
			prediction.readLine();prediction.readLine();prediction.readLine();prediction.readLine();prediction.readLine();//ignore first 5 lines in prediction file
			while((ljffts=testseed.readLine())!=null){
				ts=ljffts.split("\t");
				ljffpf=prediction.readLine();
			    p=ljffpf.split("\\s+");
				TestInstancesInfo t=new TestInstancesInfo();
				t.setGloss(ts[4].trim());
				t.setPos(ts[2].trim());
				t.setSenseno(Integer.parseInt(ts[3].trim()));
				float f[]=new float[200];
				if(!ljffts.contains("?")){
					for( i=0;i<200;i++){
					  f[i]=Float.parseFloat(ts[i+5]);
				    }
					t.setVector(f);
				}else{
					t.setVector(null);
				}
				t.setWord(ts[0].trim());
				String predictedtense = p[p.length - 2].replace(p[p.length - 2], p[p.length - 2].substring(p[p.length - 2].indexOf(":") + 1, p[p.length - 2].length()));
				if(predictedtense.equals("atempora"))predictedtense=predictedtense+"l";
				t.setPredictedtense(predictedtense.trim());
				t.setOffset(Long.parseLong(ts[1].trim()));
				double si=fi.calculateSimilarity(trainseedl,t);
				t.setSimilarity(si);
				testseedl.add(t);
				
			}
			testseed.close();
			prediction.close();
			
			Collections.sort(testseedl);
			Writer expntrainA = null;           // train file which need to be expanded
			Writer expnseedA = null;            //seed file which need to be expanded	
			Writer expndetails=null;
			Writer testtrainW = null;
			Writer testseedW = null;
			//String s[];  // to store first 206 lines of test train file
			try{
				
				expntrainA=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[3],true), "UTF8"));      // Append Mode
				expnseedA=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[4],true), "UTF8"));
				expndetails=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[5],true), "UTF8"));
				testtrainW=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[0]), "UTF8")); 
				testseedW=new BufferedWriter(new OutputStreamWriter(      
						new FileOutputStream(args[1]), "UTF8"));
				int iterationNo=Integer.parseInt(args[6]);
				java.util.Iterator<TestInstancesInfo> iterator = testseedl.iterator();
				ArrayList<TestInstancesInfo> instancesadded=new ArrayList<TestInstancesInfo>();
				TestInstancesInfo t/*ia*/;
				int nooftemporalinstancesadded=0,noofatemporalinstancesadded=0;
				expndetails.write("IterationNo\tWord\tWord_Offset\tPOS\tSenseno\tGloss\tPredictedtense\tSimilarity\n");
				 while (iterator.hasNext()) {
					 
					if((nooftemporalinstancesadded<(25*noti/100)||noofatemporalinstancesadded<(25*noati/100))){
						//if(!fi.checkPresence((t=iterator.next()).getGloss().trim(),trainseedl,instancesadded)){
							 //ia=new TestInstancesInfo();
							switch((t=iterator.next()).getPredictedtense().trim()){
							
						case "temporal":if(nooftemporalinstancesadded<(25*noti/100)){
							            expntrainA.write("'"+t.getGloss()+"',");
							            float f[]=t.getVector();
							            for(i=0;i<200;i++){
							            	expntrainA.write(f[i]+",");
							            }
							            expntrainA.write(t.getPredictedtense()+"\n");
							            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t");
							            for(i=0;i<200;i++){
							            	expnseedA.write(f[i]+"\t");
							            }
							            expnseedA.write(t.getPredictedtense()+"\n");
							            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getSimilarity()+"\n");
							           /* ia.setGloss(t.getGloss());
							            ia.setOffset(t.getOffset());
							            ia.setPos(t.getPos());
							            ia.setPredictedtense(t.getPredictedtense());
							            ia.setSenseno(t.getSenseno());
							            ia.setSimilarity(t.getSimilarity());
							            ia.setVector(t.getVector());
							            ia.setWord(t.getWord());
							            instancesadded.add(ia);*/
							            nooftemporalinstancesadded++;
							            iterator.remove();  // added to train & seed, so remove it from test
						             }
						             break;
						case "atemporal":if(noofatemporalinstancesadded<(25*noati/100)){
										    expntrainA.write("'"+t.getGloss()+"',");
								            float f[]=t.getVector();
								            for(i=0;i<200;i++){
								            	expntrainA.write(f[i]+",");
								            }
								            expntrainA.write(t.getPredictedtense()+"\n");
								            expnseedA.write(t.getWord()+"\t"+t.getSenseno()+"\t"+t.getPos()+"\t");
								            for(i=0;i<200;i++){
								            	expnseedA.write(f[i]+"\t");
								            }
								            expnseedA.write(t.getPredictedtense()+"\n");
								            expndetails.write(iterationNo+"\t"+t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+t.getPredictedtense()+"\t"+t.getSimilarity()+"\n");
								           /* ia.setGloss(t.getGloss());
								            ia.setOffset(t.getOffset());
								            ia.setPos(t.getPos());
								            ia.setPredictedtense(t.getPredictedtense());
								            ia.setSenseno(t.getSenseno());
								            ia.setSimilarity(t.getSimilarity());
								            ia.setVector(t.getVector());
								            ia.setWord(t.getWord());
								            instancesadded.add(ia);*/
								            noofatemporalinstancesadded++;
								            iterator.remove();  // added to train & seed so remove it from test
							             }
							             break;
						}
			       	
				}else break;
		 }
				 java.util.Iterator<TestInstancesInfo> iterator1 = testseedl.iterator();
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
					 testseedW.write(t.getWord()+"\t"+t.getOffset()+"\t"+t.getPos()+"\t"+t.getSenseno()+"\t"+t.getGloss()+"\t");
					
					 for(i=0;i<200;i++){
						 testseedW.write(f[i]+"\t");
					 }
					 testseedW.write("\n");
				 }
				 
				 expndetails.write("------------------------------NO of Instances Of each class that should have been added------------------------------------------------\n");
				 expndetails.write("No. of temporal instances that should have been  added: "+25*(noti)/100+"\n");
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
	
	public  double calculateSimilarity(ArrayList<TestInstancesInfo> trainseedl,TestInstancesInfo t){
		java.util.Iterator<TestInstancesInfo> iterator1 = trainseedl.iterator();  
		double maxsimilarity=-1,similarity;
		float v[]=t.getVector();
	    double sumOfSquaretv=0;
		for(int i=0;i<200;i++){
			   sumOfSquaretv=sumOfSquaretv+v[i]*v[i];
		}
		TestInstancesInfo next;
		while(iterator1.hasNext()){
		  if((next=iterator1.next()).getPredictedtense().equals(t.getPredictedtense())){  // Calculate similarity only with the training instances with same time sense
			   double sumOfProduct=0;
			   double sumOfSquaretrv=0;
			   //next=iterator1.next();
			   float tv[]=next.getVector(); // training vector
			   if(v!=null&&tv!=null){
				   
				   for( int i=0;i<200;i++){
					   sumOfProduct=sumOfProduct+v[i]*tv[i];
					   sumOfSquaretrv=sumOfSquaretrv+tv[i]*tv[i];
				   }
				   
				  similarity= sumOfProduct/(Math.sqrt((double)sumOfSquaretrv)*Math.sqrt((double)sumOfSquaretv));
			   }else{
				   
				  similarity= -2;
			   }
			   if(maxsimilarity<similarity){
				   maxsimilarity=similarity;
			   }
		   }
		}
		return maxsimilarity;
	}
	
	/*public boolean checkPresence(String G, ArrayList<TestInstancesInfo> trainseedl,ArrayList<TestInstancesInfo> instancesadded){ // Check Presence of Gloss G in training file
		java.util.Iterator<TestInstancesInfo> iterator1 = trainseedl.iterator();
		TestInstancesInfo next;
		while(iterator1.hasNext()){              // Check whether G is already there in training set
			next=iterator1.next();
			if(next.getGloss().trim().equals(G)){
				return true;
			}	
		}
         iterator1 = instancesadded.iterator();
		while(iterator1.hasNext()){             // Check whether G is there in instances just added in this iteration
			next=iterator1.next();
			if(next.getGloss().trim().equals(G)){
				return true;
			}	
		}
		return false;
	}*/
}
