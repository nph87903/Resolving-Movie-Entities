import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class BuildFeatureVector {

	double[][] CountFeatures;
	double[][] SimilarFeatures;
	double[][] WikiFeatures;
	double[][] NumberFeatures;
	double[][] LabelFeatures;
	String[] FeaturesOutput;

	static ArrayList<TrainingObject_2> allObjects = new ArrayList<TrainingObject_2>();
	static ArrayList<TargetObject> allTargets = new ArrayList<TargetObject>();
	ArrayList<Feature> allFeatures = new ArrayList<Feature>();
	
	static String mainDir = "movies/train_movies/";
	static String featureDomain = "training/";
	static String outputName="trainingVector1.txt";
	 
	

//	 static String mainDir ="movies/test_movies";
//	 static String outputName="formatted.txt";
//	 static String featureDomain = "testing/";
	public void init() {
		BuildTrainingObjForAllTargets bt = new BuildTrainingObjForAllTargets();
		bt.readDir(new File(mainDir));
		allObjects= bt.getTObjs();
		bt.objectToTargets();
		allTargets = bt.getTargets();
		FeatureCount cf = new FeatureCount();
		FeatureLabel fl = new FeatureLabel();
		FeatureWiki wf = new FeatureWiki();
		FeatureNumber fn = new FeatureNumber();
		
		allFeatures.add(cf);
		allFeatures.add(fl);
		allFeatures.add(wf);
		allFeatures.add(fn);
		
		
	}
	
	public void displayFeature() {
		Scanner scan1=null;
		try {
			scan1 = new Scanner(new File(featureDomain+"Features/labelFeature.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = "";
//		for (int i=0; i<allTargets.size(); i++){
			for (int i=1459; i<1461; i++){
//			temp = scan1.nextLine();
//			temp = temp.replace("countAllLabel:", "");
//			temp = temp.replace("countUniqueLabel:", "\t");
//			System.out.print(allTargets.get(i).getLabel()+"\t"+ allTargets.get(i).getFlag() +"\t"+allTargets.get(i).getFileIndex()+ "\t");
//			for (int j=0; j<4; j++) {
//				System.out.print(LabelFeatures[i][j] + "\t");
//			}
//			System.out.print(temp+"\t"+NumberFeatures[i][0]+ "\t");
//			System.out.println( WikiFeatures[i][0]);
//			for (int j=0; j<3; j++) {
//				System.out.print(SimilarFeatures[i][j] + " ");
//			}
			for (int j=0; j<4; j++) {
			System.out.print(CountFeatures[i][j]+"   ");
			
			}System.out.println(allTargets.get(i).getFile());
		}
	}
	
	public void updateFeature(){
		
		for(Feature f:allFeatures){
			f.init(allTargets, featureDomain);
			f.writeFeatures();
		}
	}
	public void WriteForAvirup(){
		for(Feature f:allFeatures)
			f.init(allTargets, featureDomain);
		CountFeatures =allFeatures.get(0).run();
		WikiFeatures=allFeatures.get(2).run();
		NumberFeatures = allFeatures.get(3).run();
		LabelFeatures=allFeatures.get(1).run();
		FileWriter fstream;
		BufferedWriter out;
		//FeaturesOutput = new String[allTargets.size()];
		try {
			String outputStr="";
			String fomat="";
			int i =0;
			String flag = "";
			fstream = new FileWriter(new File("trainingForAvirup.txt"), false);
			out = new BufferedWriter(fstream);
			Scanner scan1 = new Scanner(new File(featureDomain+"Features/countFeature.txt"));
			Scanner scan2 = new Scanner(new File(featureDomain+"Features/labelFeature.txt"));
			Scanner scan3 = new Scanner(new File(featureDomain+"Features/numberFeature.txt"));
			Scanner scan4 = new Scanner(new File(featureDomain+"Features/wikiFeature.txt"));
			while (scan1.hasNext()){
				//outputStr="";
				TargetObject to = allTargets.get(i);
				if(to.getFlag())
					flag ="yes";
				else
					flag ="no";
				outputStr+=flag;
				outputStr+=" "+scan1.nextLine();
				if(to.getLabel()==2)
					outputStr+=" WikiBinary:1";
				else
					outputStr+=" WikiBinary:0";
				outputStr+=" CountActor:"+(CountFeatures[i][0]>0?CountFeatures[i][0]:0);
				outputStr+=" CountDir:"+(CountFeatures[i][3]>0?CountFeatures[i][3]:0);
				outputStr+=" CountGenre:"+(CountFeatures[i][2]>0?CountFeatures[i][2]:0);
				outputStr+=" CountActress:"+(CountFeatures[i][1]>0?CountFeatures[i][1]:0);
				
//				System.out.println(to.getFileIndex());
				outputStr+=" "+scan4.nextLine();
				outputStr+=" CountWordActor:"+(LabelFeatures[i][0]>0?LabelFeatures[i][0]:0) ;
				outputStr+=" CountWordDir:"+(LabelFeatures[i][3]>0?LabelFeatures[i][3]:0);
				outputStr+=" CountWordGenre:"+(LabelFeatures[i][2]>0?LabelFeatures[i][2]:0);
				outputStr+=" CountWordActress:"+(LabelFeatures[i][1]>0?LabelFeatures[i][1]:0);
				outputStr+=" "+scan2.nextLine();
				outputStr+=" "+scan3.nextLine();
				if(to.getLabel()==3)
					outputStr+=" OODBinary:1"+"\n";
				else
					outputStr+=" OODBinary:0"+"\n";
				
//				FeaturesOutput[i] = fomat;
				i++;
			}
			out.write(outputStr);
			out.close();
		}catch(Exception e){
			
		}
			

	}
	public void WritetrainingVector(){
		FileWriter fstream;
		BufferedWriter out;
		//FeaturesOutput = new String[allTargets.size()];
		try {
			String outputStr="";
			String fomat="";
			int i =0;
			String flag = "";
			fstream = new FileWriter(new File(outputName), false);
			out = new BufferedWriter(fstream);
			Scanner scan1 = new Scanner(new File(featureDomain+"Features/countFeature.txt"));
			Scanner scan2 = new Scanner(new File(featureDomain+"Features/labelFeature.txt"));
			Scanner scan3 = new Scanner(new File(featureDomain+"Features/numberFeature.txt"));
			Scanner scan4 = new Scanner(new File(featureDomain+"Features/wikiFeature.txt"));
			while (scan1.hasNext()){
				//outputStr="";
				TargetObject to = allTargets.get(i++);
				if(to.getFlag())
					flag ="yes";
				else
					flag ="no";
				outputStr+=flag;
				outputStr+=" "+scan1.nextLine();
//				System.out.println(to.getFileIndex());
				if(to.getLabel()==2)
					outputStr+=" wikiBinary:1";
				else
					outputStr+=" wikiBinary:0";
				outputStr+=" "+scan4.nextLine();
//				outputStr+=" "+scan2.nextLine();
//				outputStr+=" "+scan3.nextLine();
				if(to.getLabel()==3)
					outputStr+=" OOD:1" + "\n";
				else
					outputStr+=" OOD:0" + "\n";
//				FeaturesOutput[i] = fomat;
			}
			out.write(outputStr);
			out.close();
		}catch(Exception e){
			
		}
			
	}
	

	public static void main(String[] args) {
		/*
		 * 1 only for imdb database feature
		 */
		BuildFeatureVector bf = new BuildFeatureVector();
		bf.init();
//		myUtils.ErrorOutput(allTargets,featureDomain);
		//bf.updateFeature();
		bf.WritetrainingVector();
		//bf.WriteForAvirup();
//		FeatureCount cf = new FeatureCount();
//		cf.init(bf.allTargets, featureDomain);
//		bf.CountFeatures =cf.run();
		
		
		
//		FeatureWiki wf = new FeatureWiki();
//		wf.init(bf.allTargets, featureDomain);
//		bf.WikiFeatures = wf.run();
//		
//		FeatureLabel fl = new FeatureLabel();
//		fl.init(bf.allTargets, featureDomain);
//		bf.LabelFeatures = fl.run();
//		
//		FeatureNumber fn = new FeatureNumber();
//		fn.init(bf.allTargets, featureDomain);
//		bf.NumberFeatures = fn.run();
//		
//		
//		bf.displayFeature();
	}

}