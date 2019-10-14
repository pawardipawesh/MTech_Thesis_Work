
public class TestInstancesInfo implements Comparable {

	private String gloss;
	private float[] vector;
	
	private String word;
	private long offset;
	private int senseno;
	private String pos;
	private float predictionValue;
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



	public float getPredictionValue() {
		return predictionValue;
	}



	public void setPredictionValue(float predictionValue) {
		this.predictionValue = predictionValue;
	}



	public String getPredictedtense() {
		return predictedtense;
	}



	public void setPredictedtense(String predictedtense) {
		this.predictedtense = predictedtense;
	}

	@Override
	public int compareTo(Object tii) {
		   float tipredictionvalue=((TestInstancesInfo)tii).getPredictionValue();
		   int i=0;
	       if(this.getPredictionValue()>tipredictionvalue)i=-1;
	       if(this.getPredictionValue()<tipredictionvalue)i=1;
	       if(this.getPredictionValue()==tipredictionvalue)i=0;
	       return i;

	        /* For Descending order do like this */
	        //return compareage-this.studentage;
	}

	
	
	
	
	
}
