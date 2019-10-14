
public class TestInstancesInfo implements Comparable {

	private String gloss;
	private float[] vector;
	
	private String word;
	private long offset;
	private int senseno;
	private String pos;
	private double similarity;
	private String predictedtense;
	
	
	
	

	public String getGloss() {
		return gloss;
	}





	public void setGloss(String gloss) {
		this.gloss = gloss;
	}





	public float[] getVector() {
		return vector;
	}





	public void setVector(float[] vector) {
		this.vector = vector;
	}





	public String getWord() {
		return word;
	}





	public void setWord(String word) {
		this.word = word;
	}





	public long getOffset() {
		return offset;
	}





	public void setOffset(long offset) {
		this.offset = offset;
	}





	public int getSenseno() {
		return senseno;
	}





	public void setSenseno(int senseno) {
		this.senseno = senseno;
	}





	public String getPos() {
		return pos;
	}





	public void setPos(String pos) {
		this.pos = pos;
	}





	public double getSimilarity() {
		return similarity;
	}





	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}





	public String getPredictedtense() {
		return predictedtense;
	}





	public void setPredictedtense(String predictedtense) {
		this.predictedtense = predictedtense;
	}





	@Override
	public int compareTo(Object tii) {
		   double tisimilarity=((TestInstancesInfo)tii).getSimilarity();
		   int i=0;
	       if(this.getSimilarity()>tisimilarity)i=-1;
	       if(this.getSimilarity()<tisimilarity)i=1;
	       if(this.getSimilarity()==tisimilarity)i=0;
	       return i;

	        /* For Descending order do like this */
	       
	        //return compareage-this.studentage;
	}

	
	
	
	
	
}
