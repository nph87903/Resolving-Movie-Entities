public class Targets{

	private String imdb;
	private String other;
	
	public Targets(){}
	
	public Targets(String imdb, String other){
		this.imdb = imdb;
		this.other = other;
	}
	
	public String getI(){
		return this.imdb;
	}
	
	public String getO(){
		return this.other;
	}
	
	public String toString(){
		return("IMDB " + imdb + "  OTHER " + other);
	}
}
	