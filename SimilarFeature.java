import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;


public class SimilarFeature extends Feature{

	ArrayList<TargetObject> al;
	String fileLocation;
	
	Hashtable<String, ArrayList<String>> director_dict = new Hashtable<String, ArrayList<String>>(); 
	Hashtable<String, ArrayList<String>> actor_dict = new Hashtable<String, ArrayList<String>>();
	Hashtable<String, ArrayList<String>> actress_dict = new Hashtable<String, ArrayList<String>>();
	
	public void init(ArrayList<TargetObject> al, String tmp) {
		this.al = al;
		fileLocation = tmp;
		initFile("reduced_database/director_train_list.txt", director_dict);
		initFile("reduced_database/actor_train_list.txt", actor_dict);
		initFile("reduced_database/actress_train_list.txt", actress_dict);
	}
	
	private void initFile(String fileName, Hashtable<String, ArrayList<String>> dict) {
		try {
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			String tmp = null;
			
			tmp = bin.readLine();
			int idx = tmp.indexOf('\t');
			String name = tmp.substring(0, idx).toLowerCase().trim();
			String movie = tmp.substring(idx, tmp.length()).trim().toLowerCase();
			
			ArrayList<String> al = new ArrayList<String>();
			al.add(movie);
			
			while ( (tmp = bin.readLine()) != null) {
				if ( ! (tmp.charAt(0) == '\t' || tmp.charAt(0) == ' ') ) {
					dict.put(name, al);
					
					idx = tmp.indexOf('\t');
					name = tmp.substring(0, idx).toLowerCase().trim();
					movie = tmp.substring(idx, tmp.length()).trim().toLowerCase();
					
					al = new ArrayList<String>();
					al.add(movie);
				} else {
					al.add(tmp.toLowerCase().trim());
				}
			}
			
			bin.close();
		} catch (IOException ioe) {
		}
		
	}
	
	public void display() {
		String key = null;
		int i = 0;
		for (Iterator<String> it = director_dict.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			System.out.println(i);
			System.out.println(key+": "+director_dict.get(key));
			i++;
		}
		System.out.println(director_dict.keySet().size());
		System.out.println(actor_dict.keySet().size());
		System.out.println(actress_dict.keySet().size());
	}
	
	public double[][] run() { 
		int[] actors =  FeatureActor.similarActors(al, fileLocation, actor_dict);
		int[] actresses = FeatureActress.similarActresses(al, fileLocation, actress_dict);
		int[] directors = FeatureDirector.similarDirectors(al, fileLocation, director_dict);
		
		double[][] res = new double[actors.length][3];
		for (int i=0; i< actors.length; i++) {
			res[i][0] = actors[i];
			res[i][1] = actresses[i];
			res[i][2] = directors[i];
		}
		return res;
	}
	
	/**
	 * Unit test
	 * @param args
	 */
	public static void main(String[] args) {
		SimilarFeature sf = new SimilarFeature();
		sf.init(null, null);
		sf.display();
	}

	@Override
	public void writeFeatures() {
		// TODO Auto-generated method stub
		
	}

	

}
