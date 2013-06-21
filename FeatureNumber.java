import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FeatureNumber extends Feature{
	ArrayList<TargetObject> al;
	ArrayList<String> al2;
	String fileLocation;

	public void init(ArrayList<TargetObject> li, String str) {
		this.al = li;
		this.fileLocation = str;
		al2 = new ArrayList<String>();
		String fileName ="";
		if(str.contains("training"))
			fileName="inputOfTraining2.txt";
		else
			fileName ="inputOfTesting2.txt";
		String tmp = "" ;
		String label = "";
		String des = "";
		String uri = "";
		int index = -1 ;
		BufferedReader bin;
		try {
			bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			try {
				while ((tmp = bin.readLine()) != null){
					label = tmp.substring(0,1);
					index = tmp.indexOf("movies\\");
					uri = tmp.substring(index+1, tmp.length());
					uri = uri.replace('\\', '/');
					index = tmp.indexOf("movies\\");
					des = tmp.substring(5, index);
					des = des.replace('(', ' ');
					des = des.replace(')', ' ');
					des = des.replace('-', ' ');
					label = label.trim();
					des = des.trim().toLowerCase();
					uri = uri.trim();
					al2.add(des);
				}
			} catch (IOException e) {

			}
		} catch (FileNotFoundException e) {
			}

	}

	public double[][] run() {
		double[][] res = new double[al.size()][1];
		for (int i = 0; i < al.size(); i++) {
			TargetObject a = al.get(i);
			
			Scanner s = new Scanner(al2.get(a.getFileIndex()));
			String lastWord = "";
			while(s.hasNext()) {
				lastWord = s.next();
			}
			
			try {
				Integer.parseInt(lastWord);
			} catch (NumberFormatException e) {
			res[i][0]=0;	
			}
			
			int timeFeature = 0;
			if( myUtils.countWord( "year",a.getFile())>0&&a.getLabel()==1)
				timeFeature = myUtils.countWord( lastWord,a.getFile());
			res[i][0] = timeFeature;
			// System.out.println(res[i][0]);
		}
		return res;
	}

	@Override
	public void writeFeatures() {
		// TODO Auto-generated method stub
		double[][] counts=run();
		FileWriter fstream;
		BufferedWriter out;
		double sum=0;
		String format="";
		try {
			fstream = new FileWriter(new File(fileLocation+"Features/numberFeature.txt"), false);
			out = new BufferedWriter(fstream);
			// return f;
		
		for(int i=0;i<al.size();i++)
		{
			if(counts[i][0]>0)
			sum += counts[i][0];
			format+="numberFeature:"+sum+"\n";
			
			sum=0;
		}
		out.write(format);
		out.close();
		} catch (Exception e) {
		}
}

}
