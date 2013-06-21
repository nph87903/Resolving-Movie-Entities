import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class FeatureCount extends Feature{
	ArrayList<TargetObject> al;
	String fileLocation;

	public void init( ArrayList<TargetObject> allTargets, String featureDomain) {
		al = allTargets;
		fileLocation = featureDomain;
	}

	public double[][] run() {
		double[][] counts = new double[al.size()][4];
		double[] a1 = FeatureActor.actorFeature(al, fileLocation);
		double[] a2 = FeatureActress.actressFeature(al, fileLocation);
		double[] a3 = FeatureGenre.genraFeature(al, fileLocation);
		double[] a4 = FeatureDirector.directorFeature(al, fileLocation);
		
		for (int i = 0; i < al.size(); i++) {
			counts[i][0] = a1[i];
			counts[i][1] = a2[i];
			counts[i][2] = a3[i];
			counts[i][3] = a4[i];
//			if(a1[i]==-10&&a2[i]==-10&&a3[i]==-10&&a4[i]==-10)
//				System.out.println(i+":  "+al.get(i).getFile());
		}
		return counts;
	}
	
	public void writeFeatures(){
		double[][] counts=run();
		FileWriter fstream;
		BufferedWriter out;
		String format="";
		double sum=0,count=0;
		try {
			fstream = new FileWriter(new File(fileLocation+"Features/countFeature.txt"),false);
			out = new BufferedWriter(fstream);
			// return f;
		/*
		 * ***********************
		 * no entrophy 
		 */
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
					sum +=counts[i][3];
					count++;
					}
			format+="countAll:"+sum+" countUnique:"+count+"\n";
			sum=0;count=0;
		}
		/*
		 * ***********************
		 * with entrophy 
		 */
//		for(int i=0;i<al.size();i++)
//		{
//			if(counts[i][0]>0){
//			sum +=2* counts[i][0];
//			count+=2;
//			}
//			if(counts[i][1]>0){
//				sum +=3* counts[i][1];
//				count+=3;
//				}
//			if(counts[i][2]>0){
//				sum += counts[i][2];
//				count+=1;
//				}
//			if(counts[i][3]>0){
//					sum += 4*counts[i][3];
//					count+=4;
//					}
//			format+="countAll:"+sum+" countUnique:"+count+"\n";
//			sum=0;count=0;
//		}
		out.write(format);
		out.close();
		} catch (Exception e) {
		}
	}

}
