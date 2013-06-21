import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class FeatureWiki extends Feature{
	ArrayList<TargetObject> al;
	String fileLocation ;
	public void init(ArrayList<TargetObject> al, String str) {
		this.al = al;
		this.fileLocation = str;
	}

	public double[][] run() {
		double[][] res = new double[al.size()][1];
		Wiki wk = new Wiki();
		for (int i = 0; i<al.size(); i++) {
			TargetObject a = al.get(i);
		
			if (a.getWikiFile()!=null)
				res[i][0] = wk.setMatch(a.getFile(), a.getWikiFile());
			
			//System.out.println(res[i][0]);
		}
		
		return res;
	}
	
	public void writeFeatures(){
		double[][] counts=run();
		FileWriter fstream;
		BufferedWriter out;
		String format="";
		double sum=0;
		try {
			fstream = new FileWriter(new File(fileLocation+"Features/wikiFeature.txt"), false);
			out = new BufferedWriter(fstream);
			// return f;
		
		for(int i=0;i<al.size();i++)
		{
			if(counts[i][0]>0)
			sum += counts[i][0];
			format+="wikiFeature:"+sum+"\n";
			sum=0;
		}
		out.write(format);
		out.close();
		} catch (Exception e) {
		}
}

}
