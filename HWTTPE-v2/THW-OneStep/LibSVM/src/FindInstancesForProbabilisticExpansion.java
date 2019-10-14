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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;


public class FindInstancesForProbabilisticExpansion {
    public static int noofmatchfound=0;
	public static void FMI(String f1, String f2, String f3, String f4,String noofinstancestobeignored,String f5,String iterationno) {
		// f1----prediction file
		// f2----Dataset file
		BufferedReader predictions = null;
		BufferedReader filename = null;
		BufferedReader TempoAtempotrainingfile = null;
		DecimalFormat df = new DecimalFormat("#.###");

		info x[] = new info[150000];   // to put info assembled from prediction file & dataset file
        Listoperation lo=new Listoperation(); 
		try {
			predictions = new BufferedReader(new InputStreamReader(
					new FileInputStream(f1), "UTF8"));
			filename = new BufferedReader(new InputStreamReader(
					new FileInputStream(f2), "UTF8"));
			TempoAtempotrainingfile = new BufferedReader(new InputStreamReader(
					new FileInputStream(f3), "UTF8"));

		} catch (FileNotFoundException e) {
			System.err.println("Error opening seed words or predictions or training  file." + e);
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		int TempoAtempotrainingfileSize = 0;
		String LJF;int count=0,id,nopresenti=0,nopasti=0,nofuturei=0,noneutrali=0,noatemporali=0;  // noti==NO Of temporal instances
		String wordsArrayForTF[];
		try {
			while ((LJF= TempoAtempotrainingfile.readLine()) != null) {          // LJFFTATF-----line
																				// just
																				// fetched
																				// from
																				// tempo-atempo
				if(count >= Integer.parseInt(noofinstancestobeignored)) {																// file
					wordsArrayForTF=LJF.split("',");
					id=wordsArrayForTF[0].indexOf("'", 0);
					String s=wordsArrayForTF[0].substring(id+1,(wordsArrayForTF[0].length())).trim();
					String annotation=wordsArrayForTF[1];
					if(annotation.trim().equals("present"))
						nopresenti++;
					else if(annotation.trim().equals("past"))
						nopasti++; 
					else if(annotation.trim().equals("future"))
						nofuturei++;
					     else if(annotation.trim().equals("neutral"))
								noneutrali++;
					          else if(annotation.trim().equals("atemporal"))
					        	  noatemporali++;
						
					lo.addinlist(s);
					TempoAtempotrainingfileSize++;
				}
				count++;
			}

			TempoAtempotrainingfile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		Writer AppendTempoAtempotrainingfile = null;
		Writer AppendTempoAtemposeedfile = null;
		Writer AppendExpansionfile=null;

		try {
			AppendTempoAtempotrainingfile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(f3, true),
							"UTF8"));
			AppendTempoAtemposeedfile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(f4, true),
							"UTF8"));
			AppendExpansionfile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(f5, true),
							"UTF8"));
			

		} catch (FileNotFoundException e) {
			System.err.println("Error opening trainingset file.");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported.");
			System.exit(-1);
		}
		
		String lineJustFetched = null;
		String LJFFFTBCD = null;
		String wordsArrayForPredictionsFile[];
		String wordsArrayForDatasetFile[];
		
		int l = 0;
		int c = 0; // No. of lines read in predictions file
		try {
			
			while (true) {
				lineJustFetched = predictions.readLine();
				if (lineJustFetched == null) {
					break;
				} else {
					c++;
					if (c < 6)
						;
					else {
						/*/home/dipawesh/workspace/Hindi_wordnet_time_tagging/Expanded_Training_data/NaiveBayes/0gram/trainingSetforAtemporal-TemporalOneStep_iteration1.arff
						 * if (c == 277) { System.out.println(c); }
						 */
						if (lineJustFetched == null
								|| lineJustFetched.trim().equals("")) {
							break;
						}
						wordsArrayForPredictionsFile = lineJustFetched
								.split("\\s+");
						LJFFFTBCD=filename.readLine();
						wordsArrayForDatasetFile = LJFFFTBCD.split("\t");
						int index = wordsArrayForPredictionsFile[wordsArrayForPredictionsFile.length - 2]
									.indexOf(":");
						
							String s = wordsArrayForPredictionsFile[wordsArrayForPredictionsFile.length - 2];
							s = s.replace(s, s.substring(index + 1, s.length()));
							x[l] = new info();
							x[l].pos = wordsArrayForDatasetFile[2].trim();
							x[l].senseno = wordsArrayForDatasetFile[3];
							x[l].gloss = wordsArrayForDatasetFile[wordsArrayForDatasetFile.length - 1];
							if (s.equals("past")||s.equals("present")||s.equals("future")||s.equals("neutral")) {
								x[l].tense = s;
							} else {
								x[l].tense = s + "l";
							}
							x[l].word = wordsArrayForDatasetFile[0];
							x[l].predval=Double.parseDouble(wordsArrayForPredictionsFile[wordsArrayForPredictionsFile.length - 1].trim());
							x[l].instanceno=Integer.parseInt(wordsArrayForPredictionsFile[wordsArrayForPredictionsFile.length - 4]);
							l++;
						}

					}

				}
		    //System.out.println(nopasti+" "+nopresenti+" "+nofuturei+" "+noneutrali);
		    Double predictionValue=Double.parseDouble("1");
		    int k=0,index,noofpastinstancesadded=0,noofpresentinstancesadded=0,nooffutureinstancesadded=0,noofneutralinstancesadded=0,noofatemporalinstancesadded=0;
		    info x1[]=new info[15000]; // For keeping info of data from which instances to be added to training set for expansion can be selected
		    info x2[]=new info[(15*nopasti/100)+(15*nopresenti/100)+(15*nofuturei/100)+(15*noneutrali/100)+(20*noatemporali/100)];
		    //System.out.println((15*nopasti/100)+" "+(15*nopresenti/100)+" "+(15*nofuturei/100)+" "+(15*noneutrali/100));
		    //System.out.println(x2.length);
		    int j=0,sr;
		    while((noofpastinstancesadded<(15*nopasti/100)||noofpresentinstancesadded<(15*nopresenti/100)||nooffutureinstancesadded<(15*nofuturei/100)||noofneutralinstancesadded<(15*noneutrali/100)||noofatemporalinstancesadded<(20*noatemporali/100))){
		    	k=0;
		    for(int i=0;i<l;i++){
		    	//System.out.println(x[i].gloss+"inside");
		    	//System.out.println(lo.search(x[i].gloss));
		    	//System.out.println(x[i].predval+" "+predictionValue);
		       if(x[i].predval.equals(predictionValue) && (lo.search(x[i].gloss)!=1)){
		    	   x1[k]=new info();
		    	   x1[k].gloss=x[i].gloss;
		    	   x1[k].pos=x[i].pos;
		    	   x1[k].predval=x[i].predval;
		    	   x1[k].senseno=x[i].senseno;
		    	   x1[k].tense=x[i].tense;
		    	   x1[k].word=x[i].word;
		    	   x1[k].instanceno=x[i].instanceno;
		    	   k++;
		       }
		    }
		  
		    ArrayList<Integer> AL=new ArrayList<Integer>();
			for(int i=0;i<k;i++){
				AL.add(new Integer(i));
			}
			Collections.shuffle(AL);
			int i=0,sn;
			
			while((noofpastinstancesadded<(15*nopasti/100)||noofpresentinstancesadded<(15*nopresenti/100)||nooffutureinstancesadded<(15*nofuturei/100)||noofneutralinstancesadded<(15*noneutrali/100)||noofatemporalinstancesadded<(20*noatemporali/100))&&i<k){
				
					index=AL.get(i);
				if(x1[index].tense.equals("past")){
					if(noofpastinstancesadded<(15*nopasti/100)){
						
						sn=Integer.parseInt(x1[index].senseno)+1;
						sr=search(x2,j,x1[index].gloss);
						if(sr==0){
						//	System.out.println("Inside Past");
						x2[j]=new info();	
						x2[j].gloss= x1[index].gloss;
						x2[j].tense=x1[index].tense;
						x2[j].word=x1[index].word;
						x2[j].senseno=String.valueOf(sn);
						x2[j].pos=x1[index].pos;
						x2[j].predval=x1[index].predval;
						x2[j].instanceno=x1[index].instanceno;
						j++;
						noofpastinstancesadded++;
						}
						
					}
				}else if(x1[index].tense.equals("present")){
					if(noofpresentinstancesadded<(15*nopresenti/100)){
						
						sn=Integer.parseInt(x1[index].senseno)+1;
						sr=search(x2,j,x1[index].gloss);
						if(sr==0){
							//System.out.println("Inside present");
						x2[j]=new info();
						x2[j].gloss= x1[index].gloss;
						x2[j].tense=x1[index].tense;
						x2[j].word=x1[index].word;
						x2[j].senseno=String.valueOf(sn);
						x2[j].pos=x1[index].pos;
						x2[j].predval=x1[index].predval;
						x2[j].instanceno=x1[index].instanceno;
						j++;
						noofpresentinstancesadded++;
						}
						
					}
				  }
				else if(x1[index].tense.equals("future")){
                        if(nooffutureinstancesadded<(15*nofuturei/100)){
                        	
						sn=Integer.parseInt(x1[index].senseno)+1;
						sr=search(x2,j,x1[index].gloss);
						if(sr==0){
						//	System.out.println("Inside future");
						x2[j]=new info();
						x2[j].gloss= x1[index].gloss;
						x2[j].tense=x1[index].tense;
						x2[j].word=x1[index].word;
						x2[j].senseno=String.valueOf(sn);
						x2[j].pos=x1[index].pos;
						x2[j].predval=x1[index].predval;
						x2[j].instanceno=x1[index].instanceno;
						j++;
						nooffutureinstancesadded++;
						}
						
					}
				  }
				else if(x1[index].tense.equals("neutral")){
                        if(noofneutralinstancesadded<(15*noneutrali/100)){
                        	
						sn=Integer.parseInt(x1[index].senseno)+1;
						sr=search(x2,j,x1[index].gloss);
						if(sr==0){
							//System.out.println("Inside neutral");
						x2[j]=new info();
						x2[j].gloss= x1[index].gloss;
						x2[j].tense=x1[index].tense;
						x2[j].word=x1[index].word;
						x2[j].senseno=String.valueOf(sn);
						x2[j].pos=x1[index].pos;
						x2[j].predval=x1[index].predval;
						x2[j].instanceno=x1[index].instanceno;
						j++;
						noofneutralinstancesadded++;
						}
						
					}
				  }
				else if(x1[index].tense.equals("atemporal")){
					if(noofatemporalinstancesadded<(20*noatemporali/100)){
						
						sn=Integer.parseInt(x1[index].senseno)+1;
						sr=search(x2,j,x1[index].gloss);
						if(sr==0){
							//System.out.println("Inside present");
						x2[j]=new info();
						x2[j].gloss= x1[index].gloss;
						x2[j].tense=x1[index].tense;
						x2[j].word=x1[index].word;
						x2[j].senseno=String.valueOf(sn);
						x2[j].pos=x1[index].pos;
						x2[j].predval=x1[index].predval;
						x2[j].instanceno=x1[index].instanceno;
						j++;
						noofatemporalinstancesadded++;
						}
						
					}
				  }
				i++;	
				}
			
			predictionValue=predictionValue-0.001;
			predictionValue = Double.valueOf(df.format(predictionValue));
			
			if(predictionValue==0)break;
		    }
		    AppendExpansionfile.write("INSTANCE NO"+"\t"+"PREDICTION VALUE"+"\t"+"Tense"+"\t"+"ITERATION NO"+"\n");
					    ArrayList<Integer> ral=new ArrayList<Integer>();
						for(int i=0;i<j;i++){
							ral.add(new Integer(i));
						}
						Collections.shuffle(ral);
		                int i=0,index1;
				           while(i<j){
				        	   index1=ral.get(i);
				        	   AppendTempoAtempotrainingfile.write("'" + x2[index1].gloss+ "'," + x2[index1].tense + "\n");
							   AppendTempoAtemposeedfile.write(x2[index1].word + "\t"
										+ x2[index1].senseno + "\t" + x2[index1].pos + "\t"
										+ x2[index1].tense + "\n");
							   AppendExpansionfile.write(x2[index1].instanceno+"\t"+(x2[index1].predval).toString()+"\t"+x2[index1].tense+"\t"+iterationno+"\n");
							   
								i++;
				           }
                   System.out.println("No. of past instances that should have been added: "+(15*nopasti/100));
                   System.out.println("No. of present instances that should have been  added: "+15*(nopresenti)/100);
                   System.out.println("No. of future instances that should have been added: "+(15*nofuturei/100));
                   System.out.println("No. of neutral instances that should have been  added: "+15*(noneutrali)/100);
                   System.out.println("No. of atemporal instances that should have been  added: "+20*(noatemporali)/100);
                   
                   
                   System.out.println("no of past instances added in this iteration: "+noofpastinstancesadded);
                   System.out.println("no of present instances added in this iteration"+noofpresentinstancesadded);
                   System.out.println("no of future instances added in this iteration"+nooffutureinstancesadded);
                   System.out.println("no of neutral instances added in this iteration: "+noofneutralinstancesadded);
                   System.out.println("no of atemporal instances added in this iteration: "+noofatemporalinstancesadded);
                   //System.out.println("No of match found in training set file: "+noofmatchfound);
                   
                   
		 }catch (IOException e) {
				System.err.println("Error in input/output.");
				e.printStackTrace();
			} finally {
				try {
					predictions.close();
					filename.close();
					AppendTempoAtemposeedfile.close();
					AppendTempoAtempotrainingfile.close();
					AppendExpansionfile.close();
				} catch (Exception e) {
                 
				}
			}
	}
	
	public static int search(info x[],int j,String G){
		
		int m=0;
		while(m<j){
			
			if(x[m].gloss.equals(G)){
			
				return 1;
			}else{
				
				m++;
			}
			
		}
		return 0;
	}

	public static void main(String args[]) throws Exception {
		FMI(args[0], args[1], args[2], args[3],args[4],args[5],args[6]);
		
	  // args[0]--> Prediction file
	  // args[1]--> Dataset file
	  // args[2]--> training set file
	  // args[3]--> seed word file
	  // args[4]--> noofinstancestobeignored in training set arff file while reading
	}

}

class info {
	String word = null, senseno = null, pos = null, tense = null, gloss = null;
	Double predval;
	int instanceno;
}

class list{
	// for keeping training set file info
	 String gloss;
	 list next;
	 
}
class Listoperation{
	
    list current,head;
    public Listoperation(){
    	list l=new list();
    	l.next=null;
    	l.gloss="";
    	head=l;
    	current=l;
    	
    }
	public void addinlist(String G){
		list l=new list();
		current.next=l;
		current=l;
		current.gloss=G;
		current.next=null;
	}
	
	public int search(String G){
		
		list pointer=head;
		
		while(pointer.next!=null){
			//System.out.println(pointer.gloss);
			//System.out.println(G);
			if(pointer.gloss.trim().equals(G.trim())){
			     FindInstancesForProbabilisticExpansion.noofmatchfound++;
				 return 1;
				 
			}
			else pointer=pointer.next;
			
		}
		
	   if(pointer.gloss.trim().equals(G.trim())){
		   FindInstancesForProbabilisticExpansion.noofmatchfound++;
		   return 1;
	   }
	   return 0;
	}
}
