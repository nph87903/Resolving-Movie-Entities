import java.util.ArrayList;


public abstract class Feature {
	int indicator=1;
	public void setInd(int i){
		indicator = i;
	}
	public abstract void init(ArrayList<TargetObject> al, String str);
	public abstract double[][] run();
	public abstract void writeFeatures();
}
