//build the vectors for imdb, wiki and oow.
import java.util.*;
import java.io.*;

public class BuildTrainingObjForAllTargets {

	private ArrayList<TrainingObject_2> li = new ArrayList<TrainingObject_2>(); // list
																				// of
																				// all
																				// the
																				// training
																				// objects
	private ArrayList<TargetObject> targets = new  ArrayList<TargetObject>();
	
	static String mainDir = "movies//test_movies";
	static String inputFileName = "inputOfTraining.txt";
	static String inputFileName2 = "inputOfTraining2.txt";
	static int count = 0; // count of all the object
	static int countFile = 0; // count of all the files
	static int num_of_files = 0;
	static boolean isChanged = false;
	static int count1=0;
	static int count2=0;
	static int count3=0;
	static int count4=0;

	final static String IMDB = "www.imdb.com";
	final static String WIKI = "wikipedia.org";
	// list of all the wiki files
	static ArrayList<File> wikiFiles = new ArrayList<File>();
	HashMap<String, File> otherIMDB = new HashMap<String, File>();
	// hash map for wiki file respect to corresponding extra imdb page
	HashMap<String, File> otherWiki = new HashMap<String, File>();
	
	public static int getMovieIndex(File imdbToWrite) {
		String searchDomain="";
		if (imdbToWrite.getParent().contains("train_movies"))
			searchDomain = "inputOfTraining.txt";
		else if (imdbToWrite.getParent().contains("test_movies"))
			searchDomain = "inputOfTesting.txt";
		int index = 0;
		Scanner scan = null;
		String movieName = "";
		try {
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(imdbToWrite)));
			scan = new Scanner(new File(searchDomain));
			movieName = bin.readLine();
			while (scan.hasNext()) {
				String line = scan.nextLine();
				if (line.contains(movieName))
					return ++index;
				index++;
			}
			// temScan.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

		return -1;
	}
	public  ArrayList<TargetObject> getTargets(){
		return targets;
	}
	public ArrayList<TrainingObject_2> getTObjs() {
		return li;
	}

	public void readDir(File f) {
		File[] subdir = f.listFiles();

		for (int i = 0; i < subdir.length; i++) {
			if (subdir[i].isDirectory())
				readDir(subdir[i]);
			else if (subdir[i].toString().indexOf("SourcePages.txt") > -1)
				{count++;build(subdir, subdir[i]);}
			else
				countFile++;
		}
	}

	public static int findByName(ArrayList<File> al, String url) {
		url = url.replaceAll("[^a-zA-Z0-9._()'!]+", "");
		url += ".txt";
		for (int i = 0; i < al.size(); i++) {
			File element = al.get(i);
			if (element.getName().equals(url)) {
				return i;
			}
		}
		return -1;
	}

	public void objectToTargets(){
		
		for(int i=0 ;i<li.size();i++){
			TargetObject trueTar = null;
			ArrayList<TargetObject> temp = new ArrayList<TargetObject>();
			TrainingObject_2 temp1 = li.get(i);
			if (temp1.getLabel() == 1 && temp1.getNumber() == 1)
				trueTar=new TargetObject( true, temp1.getFile(),temp1.getWikiFile(),1, i);
			else
				temp.add(new TargetObject( false, temp1.getFile(),temp1.getWikiFile(),1, i));
			if (temp1.getLabel() == 1 && temp1.getNumber() == 2) {
				String tempStr = temp1.extraIMDB[0];
				File tempWikiFile = otherWiki.get(tempStr);
				File tempImdbFile = otherIMDB.get(tempStr);
				trueTar = new TargetObject( true, temp1.getFile(),tempWikiFile,1, getMovieIndex(tempImdbFile));
			} else if (temp1.extraIMDB[0] != null) {
				String tempStr = temp1.extraIMDB[0];
				File tempWikiFile = otherWiki.get(tempStr);
				File tempImdbFile = otherIMDB.get(tempStr);
				// System.out.println(getMovieIndex(tempImdbFile));
				temp.add(new TargetObject( false, temp1.getFile(),tempWikiFile,1, getMovieIndex(tempImdbFile)));
				}
			if (temp1.getLabel() == 1 && temp1.getNumber() == 3) {
				String tempStr = temp1.extraIMDB[1];
				File tempWikiFile = otherWiki.get(tempStr);
				File tempImdbFile = otherIMDB.get(tempStr);
				trueTar = new TargetObject( true, temp1.getFile(),tempWikiFile,1, getMovieIndex(tempImdbFile));
				} else if (temp1.extraIMDB[1] != null) {
				String tempStr = temp1.extraIMDB[1];
				File tempWikiFile = otherWiki.get(tempStr);
				File tempImdbFile = otherIMDB.get(tempStr);
				temp.add(new TargetObject( false, temp1.getFile(),tempWikiFile,1,  getMovieIndex(tempImdbFile)));
				}
			if (temp1.getLabel() == 2) {
				 String sou = temp1.getTargets().getI();
				 sou += ".txt";
				for (int j = 0; j < temp1.getWikiFiles().size(); j++) {
					sou = sou.replaceAll("[^a-zA-Z0-9._()'!]+", "");
					File element = temp1.getWikiFiles().get(j);
					if (element.getName().equals(sou)) {
						trueTar =new TargetObject( true, temp1.getFile(),element,2, i);
						} else {
							temp.add(new TargetObject( false, temp1.getFile(),element,2, i));
						}
				}
			} else {
				for (int j = 0; j < temp1.getWikiFiles().size(); j++) {
					temp.add(new TargetObject( false, temp1.getFile(),temp1.getWikiFiles()
							.get(j),2, i));
				}
			}
			if (temp1.getLabel() == 3) {
				trueTar =new TargetObject( true, temp1.getFile(),null,3, i);
			} else 
				temp.add(new TargetObject( false, temp1.getFile(),null,3, i));
			targets.add(trueTar);
			
			targets.addAll(temp);
			}//end loop

		}
	
	// lab1 =imdb, lab2 = wiki, lab3 =oow
	public void build(File[] f1, File f) {
		// imdb string for this folder
		String imdbUrl = "";
		// has oow or not for this folder
		boolean hasOOW = false;
		// extra imdb target for this folder
		String[] extraimdb = new String[2];
		// movie wiki file for this folder
		File fileForWiki = null;

		/*
		 * get all the lines from SourcePages.txt
		 */
		ArrayList<String> line = new ArrayList<String>();
		try {
			Scanner scan = new Scanner(f);
			while (scan.hasNext()) {
				String tempLine = scan.nextLine();
				while (tempLine.equals(""))
					tempLine = scan.nextLine();
				line.add(tempLine);// System.out.println(tempLine);
			}
		} catch (Exception e) {
		}

		/*
		 * get the imdb url for a folder
		 */
		if ( !line.isEmpty() && !line.get(0).equals("")) {
			int indexOfTarget = line.get(0).indexOf("<target>");
			int indexOfDes = line.get(0).indexOf("<description>");
			if (indexOfDes == -1)
				indexOfDes = line.get(0).indexOf("<destination>"); // some of
																	// the
																	// description
																	// is
																	// destination
			imdbUrl = line.get(0).substring(indexOfTarget + 9, indexOfDes - 1);
		}

		String pageTar = "";
		String pageDes = "";
		String pageSource = "";
		int label = 0; // 1 is IMDB; 2 is wiki; 3 is oow

		// calculate extraimdb and hasoow or not
		for (int i = 0; i < line.size(); i++) {
			Scanner inLine = new Scanner(line.get(i));
			int indexOfTarget = line.get(i).indexOf("<target>");
			int indexOfDes = line.get(i).indexOf("<description>");
			if (indexOfDes == -1)
				indexOfDes = line.get(i).indexOf("<destination>"); // some of
																	// the
																	// description
																	// is
																	// destination

			pageTar = line.get(i).substring(indexOfTarget + 9, indexOfDes - 1);
			while (inLine.hasNext()) {
				String temp = inLine.next();
				if (temp.equals("<source>"))
					pageSource = inLine.next();
			}
			StringTokenizer st = new StringTokenizer(pageTar);
			String target1 = "";
			String target2 = "";

			while (st.hasMoreTokens()) {
				target1 = st.nextToken();
				if (target1.contains(",")) {
					target1 = target1.replace(",", "");
					target2 = st.nextToken();
				} // end if
				else
					target2 = "";
				if (target1.contains(IMDB) || target2.contains(IMDB)) {
					if (!imdbUrl.contains(target1)) {
						if (pageSource.contains(IMDB)) {
							otherIMDB.put(pageSource, f1[i]);
							if (extraimdb[0] == null) {
								extraimdb[0] = pageSource;
							} else if (extraimdb[1] == null) {
								extraimdb[1] = pageSource;
							}
						}
					}
				} else if (target1.contains("oow") || target2.contains("oow"))
					hasOOW = true;
			}
			target1 = "";
			target2 = "";

		}

		/*
		 * Obtain all the wiki files inside the folder
		 */
		// System.out.println("============"+line.size()+"   "+f1.length);
		if (line.size() != f1.length - 1) {
			for (int i = line.size(); i < f1.length - 1; i++)
				wikiFiles.add(f1[i]);
		}
		// System.out.println(wikiFiles);

		// build training object from the lines
		for (int i = 0; i < line.size(); i++) {
			int countIMDB = 1;
			Scanner inLine = new Scanner(line.get(i));
			int indexOfTarget = line.get(i).indexOf("<target>");
			int indexOfDes = line.get(i).indexOf("<description>");
			if (indexOfDes == -1)
				indexOfDes = line.get(i).indexOf("<destination>"); // some of
																	// the
																	// description
																	// is
																	// destination

			pageTar = line.get(i).substring(indexOfTarget + 9, indexOfDes - 1);
			while (inLine.hasNext()) {
				String temp = inLine.next();
				if (temp.equals("<source>"))
					pageSource = inLine.next();

				if (temp.equals("<description>")) {
					while (inLine.hasNext())
						pageDes += " " + inLine.next();
				}
				if (temp.equals("<destination>")) {
					while (inLine.hasNext())
						pageDes += " " + inLine.next();
				}
			}

			StringTokenizer st = new StringTokenizer(pageTar);
			String target1 = "";
			String target2 = "";
			Targets target = new Targets();
			while (st.hasMoreTokens()) {
				target1 = st.nextToken();
				if (target1.contains(",")) {
					target1 = target1.replace(",", "");
					target2 = st.nextToken();
					int index = findByName(wikiFiles, target2);
					if (index != -1) {
						fileForWiki = wikiFiles.get(index);
						wikiFiles.remove(index);
					}
				} // end if
				else
					target2 = "";
				if (target1.contains(IMDB) || target2.contains(IMDB)) {
					label = 1;
					if(target2.equals(""))
						count1++;
					else
						count2++;
					System.out.println(target2);
					if (!imdbUrl.contains(target1)) {
						// System.out.println(extraimdb[0]+"============");
						if (extraimdb[0].equals(target1)) {
							countIMDB = 2;
							// System.out.println(extraimdb[0]+"============"+label);
						} else if (extraimdb[1].equals(target1))
							countIMDB = 3;
						// System.out.println(pageSource+"  "+target1);
					}
				} else if (target1.contains(WIKI) || target2.contains(WIKI))
					{label = 2;
					count3++;
					}
				else if (target1.contains("oow") || target2.contains("oow"))
					{label = 3;
					count4++;
					}
			}// end while
			target = new Targets(target1, target2);
			TrainingObject_2 to = new TrainingObject_2(pageSource, target,
					pageDes, label);

			to.setFile(f1[i]);
			if (to.getLabel() == 1) {
				if (!imdbUrl.contains(target1)) {
					// System.out.println(to.getFile());
					if (!target2.equals("")) {
						int index = findByName(wikiFiles, target2);
						if (index != -1) {
							otherWiki.put(target1, wikiFiles.get(index));
							wikiFiles.remove(index);
						}
					}
				}
			}

			to.setImdbFile(f1[0]);
			to.setWikiFile(fileForWiki);
			to.addList(wikiFiles);
			to.setNumber(countIMDB);
			to.extraIMDB = extraimdb;
			to.hasoow = hasOOW;

			isChanged = false;

			String movieName = getMovieName(to);
			to.setDescription(movieName);
			//writeInput(to.getLabel()+" "+"1 "+to.getDescription()+" "+to.getFile()+"\n");
			li.add(to);
			num_of_files++;
			pageDes = "";
			pageTar = "";
			pageSource = "";
		}// end for
		if (wikiFiles != null)
			wikiFiles.clear();

	}// end method

	public String getMovieName(TrainingObject_2 to) {
		File imdbToWrite = null;
		if (to.getNumber() == 1) {
			imdbToWrite = to.getImdbFile();
		} else if (to.getNumber() == 2) {
			// System.out.println(otherIMDB.get(to.extraIMDB[0]));
			imdbToWrite = otherIMDB.get(to.extraIMDB[0]);
		} else
			imdbToWrite = otherIMDB.get(to.extraIMDB[1]);

		String movieName = "";
		try {
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(imdbToWrite)));
			movieName = bin.readLine();
			// temScan.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return movieName;
	}

	public static void reWriteFile(String fileContent, File f) {
		FileWriter fstream;
		BufferedWriter out;
		try {
			fstream = new FileWriter(f, false);
			out = new BufferedWriter(fstream);
			out.write(fileContent);
			out.close();
			// return f;
		} catch (Exception e) {
		}
	}

	public void writeInput() {
		String FileOfMovieNames = inputFileName;
		String FileOfMovieNames2 = inputFileName2;
		FileWriter fstream=null;
		BufferedWriter out=null;
		try {
			fstream = new FileWriter(FileOfMovieNames, true);
			out = new BufferedWriter(fstream);
			for(TrainingObject_2 to:li)		
				out.write(to.getLabel()+" "+"1 "+to.getDescription()+" "+to.getFile()+"\n");
			out.close();
			Scanner scan1 = new Scanner(new File(inputFileName));
			fstream = new FileWriter(FileOfMovieNames2, true);
			out = new BufferedWriter(fstream);
			while(scan1.hasNext()){
				String temp = scan1.nextLine();
				temp = temp.replaceAll("TV episode", "");
				temp = temp.replaceAll("TV mini-series", "");
				temp = temp.replaceAll("TV Series", "");
				temp = temp.replaceAll("TV", "");
				temp = temp.replaceAll(" - IMDb","");
				temp = temp.replaceAll("IMDb - ","");
				temp = temp.replaceAll("Video", "");
				out.write(temp+"\n");
				
			}
			out.close();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args){
		BuildTrainingObjForAllTargets bt = new BuildTrainingObjForAllTargets();
		bt.readDir(new File(mainDir));
		bt.writeInput();
//		bt.objectToTargets();
//		System.out.println(count+" "+countFile+" "+bt.getTObjs().size()+" "+bt.getTargets().size());
		System.out.println(bt.getTObjs().size()+" "+count1+" "+count2+" "+count3+" "+count4);
	}
}// end class

