import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.ListIterator;

public class TrainingObject_2{

	private String source;
	private Targets target;
	private String description;
	private int label;
	private File file;
	private File imdbFile;
	private ArrayList<File> wikiFiles;
	private File wikiFile;
	public boolean hasoow = false;
	public String[] extraIMDB = new String[2];
	private int imdbFileNumber;
	
	
	public void setNumber(int num){
		this.imdbFileNumber = num;
	}
	
	public int getNumber()
	{
		return imdbFileNumber;
	}
	
	public TrainingObject_2(){}
	
	public void setDescription(String des){
		this.description= des;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setLabel(int des){
		this.label= des;
	}
	
	public TrainingObject_2(String source, Targets target, String description, int label){
	
		this.source = source;
		this.target = new Targets(target.getI(), target.getO());
		this.description = description;
		this.label = label;
		this.file = null;
		this.wikiFile=null;
		this.wikiFiles = new ArrayList<File>();
		
	}
	
	public TrainingObject_2(String source, Targets target, String description, int label, File file,File imdbFile){
	
		this.source = source;
		this.target = new Targets(target.getI(), target.getO());
		this.description = description;
		this.label = label;
		this.file = file;
		this.imdbFile = imdbFile;
		this.wikiFiles = new ArrayList<File>();
	}
	
	public void addWikiFile(File f){
	
		wikiFiles.add(f);
	}
	
	public void addList(ArrayList<File> wikiFiles){
		Iterator<File> itr = wikiFiles.iterator();
		while (itr.hasNext()) {
			File element = itr.next();
			addWikiFile(element);
		}
	}
	
	public String getSource()
	{
	return source;
	}
	public File getWikiFile(int index){
		return wikiFiles.get(index);
	}
	public File getWikiFile(){
		return wikiFile;
	}
	
	
	public ArrayList<File> getWikiFiles(){
		return wikiFiles;
	}
	
	public Targets getTargets(){
		return this.target;
	}
	
	public void setFile(File file){
	  
		this.file = file;
	}
	public void setImdbFile(File file){
	
		this.imdbFile = file;
	}
	public void setWikiFile(File file){
	
		this.wikiFile = file;
	}
	
	public int getLabel(){
		return this.label;
	}
	
	public File getImdbFile(){
		return this.imdbFile;
	}
	public File getFile(){
		return file;
	}

	public String toString(){
		int label1 = 0;
		if (this.target.getO().equals(""))
			label1 = 1;
		else label1 = 2;
		return(label+" "+label1);
	}
}