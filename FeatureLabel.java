import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class FeatureLabel extends Feature {
	ArrayList<TargetObject> al;
	String fileLocation;
	@Override
	public void init(ArrayList<TargetObject> al, String str) {
		this.al = al;
		this.fileLocation = str;
	}

	@Override
	public double[][] run() {
		double[][] counts = new double[al.size()][4];
		double[] a1 = FeatureActor.actorLabelFeature(al, fileLocation);
		double[] a2 = FeatureActress.actressLabelFeature(al, fileLocation);
		double[] a3 = FeatureGenre.genreLabelFeature(al, fileLocation);
		double[] a4 = FeatureDirector.directorLabelFeature(al, fileLocation);
		for (int i = 0; i < al.size(); i++) {
			counts[i][0] = a1[i];
			counts[i][1] = a2[i];
			counts[i][2] = a3[i];
			counts[i][3] = a4[i];
		}
		return counts;

	}
	public void writeFeatures(){
		double[][] counts=run();
		FileWriter fstream;
		BufferedWriter out;
		double sum=0,count=0;
		String format="";
		try {
			fstream = new FileWriter(new File(fileLocation+"Features/labelFeature.txt"), false);
			out = new BufferedWriter(fstream);
			// return f;
		
		for(int i=0;i<al.size();i++)
		{
			if(counts[i][0]>0){
			sum += counts[i][0];
			count++;
			}
			if(counts[i][1]>0){
				sum += counts[i][1];
				count++;
				}
			if(counts[i][2]>0){
				sum += counts[i][2];
				count++;
				}
			if(counts[i][3]>0){
					sum += counts[i][3];
					count++;
					}
			format+="countAllLabel:"+sum+" countUniqueLabel:"+count+"\n";
			
			sum=0;count=0;
		}
		out.write(format);
		out.close();
		} catch (Exception e) {
		}
	}
}
