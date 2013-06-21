import java.io.File;
import java.util.ArrayList;


public class FeatureOOW extends Feature{
	double[][] CountFeatures;
	double[][] WikiFeatures;
	public void init(ArrayList<TargetObject> al, String str){
		BuildTrainingObjForAllTargets bt = new BuildTrainingObjForAllTargets();
		ArrayList<TargetObject> allTargets;
		String mainDir ="";
		if(str.contains("training"))
			mainDir= "movies/train_movies/";
		bt.readDir(new File(mainDir));
		bt.objectToTargets();
		allTargets = bt.getTargets();
		
		FeatureCount cf = new FeatureCount();
		cf.init(allTargets, str);
		CountFeatures =cf.run();
		
		
		
		FeatureWiki wf = new FeatureWiki();
		wf.init(allTargets, str);
		WikiFeatures = wf.run();	
	}
	public double[][] run(){
		return CountFeatures;
	}
	public void writeFeatures(){
		
	}
}
