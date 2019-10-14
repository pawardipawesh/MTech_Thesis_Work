import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Random;


public class SentenceClassificationv1 {

	public static void main(String args[])throws Exception{
		
		BufferedReader sensetaggedPPFVOILTIMEX = null;
		BufferedReader tempohindiwordnet = null;
		Writer sctaskaccuracy=null;
		Writer ef=null;
		
		sensetaggedPPFVOILTIMEX = new BufferedReader(new InputStreamReader(
				new FileInputStream(args[0]), "UTF8"));
		tempohindiwordnet = new BufferedReader(new InputStreamReader(
				new FileInputStream(args[1]), "UTF8"));
		
		ef=new BufferedWriter(new OutputStreamWriter(       //evaluated file with word & tense & total evaluated annotation
				new FileOutputStream(args[2]), "UTF8"));
		
		sctaskaccuracy=new BufferedWriter(new OutputStreamWriter(      
				new FileOutputStream(args[3]), "UTF8"));		
		
		String ljf,wa[];
		int past=0,present=0,future=0,noofinstancesinfile=0;
		float accuracy;
		int noofinstancescorrectlyclassified=0;
		SentenceClassificationv1 sc=new SentenceClassificationv1();
		HashMap<THWF,String> thw=new HashMap<THWF,String>();
		tempohindiwordnet.readLine();  // To ignore first line 
		while((ljf=tempohindiwordnet.readLine())!=null){      // put entire tempohindiWordNet in hashmap
			THWF thwf=new THWF();
			wa=ljf.split("\t");                                        //ERROR
			//thwf.setWordOffset(Long.parseLong(wa[0].trim()));
			thwf.setPos(wa[2].trim());
			thwf.setSenseNo(Integer.parseInt(wa[3].trim()));
			thwf.setWord(wa[1].trim());
			thw.put(thwf, wa[5]);
			
		}
		
		int noofcorrectrandomallocations=0;
		int noofcorrectallocationswithoutrandomness=0; 
		int noofrandomallocations=0;
		while((ljf=sensetaggedPPFVOILTIMEX.readLine())!=null){
			past=0;present=0;future=0;
			String spl[]=ljf.split("',");
			wa=spl[0].substring(1, spl[0].length()).split("\t");
			ef.write("'");
			for(int i=0;i<wa.length;i++){
				
			   String annotationfromtempohindiwordnet=sc.search(wa[i],thw);
			   if(annotationfromtempohindiwordnet!=null){
				   ef.write(wa[i]+"#"+annotationfromtempohindiwordnet+"\t");
				   if(annotationfromtempohindiwordnet.trim().equals("past"))past++;
				   if(annotationfromtempohindiwordnet.trim().equals("present"))present++;
				   if(annotationfromtempohindiwordnet.trim().equals("future"))future++;
			   }
			}
			ef.write("',");
			String m=sc.decideTime(past, present, future);
			if(m==null){
				System.out.println(noofinstancesinfile);
			}
			if(past==present&&present==future&&future==0)noofrandomallocations++;
			System.out.println(past+"\t"+present+"\t"+future+"\t"+m);
			ef.write(m+"\n");
			if(spl[1].trim().equals(m)){
				noofinstancescorrectlyclassified++;
				if(past==present&&present==future&&future==0)noofcorrectrandomallocations++;
				else{
					noofcorrectallocationswithoutrandomness++;
				}
			}
			noofinstancesinfile++;
		}
		//System.out.println(c);
		sctaskaccuracy.write("No of instances randomly allocated time sense:"+noofrandomallocations+"\n");
		sctaskaccuracy.write("No of instances correctly annotated with time sense with random allocation:"+noofcorrectrandomallocations+"\n");
		sctaskaccuracy.write("No of instances correctly annotated without random allocation"+noofcorrectallocationswithoutrandomness+"\n");
		
		accuracy=(float)noofinstancescorrectlyclassified/noofinstancesinfile;
		sctaskaccuracy.write("Total Accuracy of sentence classification :"+accuracy);
		sctaskaccuracy.write("Actual accuracy of sentence classification:"+((float)noofcorrectallocationswithoutrandomness/noofinstancesinfile));
        
		tempohindiwordnet.close();
		ef.close();
		sctaskaccuracy.close();
		sensetaggedPPFVOILTIMEX.close();
	}
	public String decideTime(int past, int present, int future){
		
		if(past>0&&present>0&&future>0){
			return "future";
		}
		if(past==0&&present==0&&future==0){
			Random r=new Random();
			int i=r.nextInt(2);
			if(i==0)return "past";
			if(i==1)return "present";
			if(i==2)return "future";
			
		}
		//if(past>=present&&future==0&&present!=0)return "present";
		if(past>0&&present>0&&future==0)return "present";
		if(past==0&&present>0&&future>0)return "future";
		if(past>0&&present==0&&future>0)return "future";
		if(past>0&&present==0&&future==0)return "past";
		if(past==0&&present>0&&future==0)return "present";
		if(past==0&&present==0&&future>0)return "future";
		return "null";
	}
	public String search(String w,HashMap<THWF,String> thw)throws Exception{  //कमिटी#6738#NOUN#2#कमिटी
		String wa[]=w.trim().split("#");
		if(wa[wa.length-1].trim().equals("null")){
			return "atemporal";
		}else{
			//System.out.println(
			THWF thwf=new THWF();
			thwf.setPos(wa[2].trim());
			thwf.setSenseNo(Integer.parseInt(wa[3].trim()));
			//thwf.setWordOffset(Long.parseLong(wa[1].trim()));
			thwf.setWord(wa[4].trim());
			
			if(!thw.containsKey(thwf)){
				System.out.println(thwf.getWord()+"  "+thwf.getSenseNo()+" "+thwf.getPos());
			}
				//
				return thw.get(thwf);
			}
			
			
		}
		
}	
	


