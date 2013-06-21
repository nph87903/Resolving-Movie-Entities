import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.Hashtable;
	import java.util.Scanner;
public class GenerateDataFile {
	
		ArrayList<Object> al = null;;
		Hashtable<String, Integer> ht= new Hashtable<String, Integer>();
		
		class Object {
			String label;
			String des;
			String uri;

			public Object(String l, String d, String u) {
				label = l;
				des = d;
				uri = u;
			}
		}

		public void init() {

			al = new ArrayList<Object>();
			File f = new File("inputOfTraining2.txt");
			Scanner s = null;
			try {
				s = new Scanner(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String tmp = "";
			String label = "";
			String des = "";
			String uri = "";
			int index = -1;
			while (s.hasNextLine()) {
				tmp = s.nextLine();
				label = tmp.substring(0, 1);
				index = tmp.indexOf("movies\\");
				uri = tmp.substring(index, tmp.length());
				index = tmp.indexOf("movies");
				des = tmp.substring(3, index);
				des = des.replace('(', ' ');
				des = des.replace(')', ' ');
				des = des.replace('-', ' ');
				label = label.trim();
				des = des.trim().toLowerCase();
				uri = uri.trim();
				al.add(new Object(label, des, uri));
			}
		}

		public void analyze() {
			Object tmp = null;
			for (int i = 0; i <al.size() ; i++) {
				
				tmp = al.get(i);
				//System.out.println(tmp.label + "; " + tmp.des);
				//"TV","Video","TV episode","TV Series","TV mini-series"
//				System.out.println("------------" + i + "----------");
//				System.out.println(tmp.label + "; " + tmp.des);
				
					System.out.println("------------" + i + "----------");
					System.out.println(tmp.label + "; " + tmp.des);
//					analyzeFile(tmp.des, tmp.uri, 171);
				analyzeFile(tmp.des, tmp.uri, i);
			}
		}

		private void analyzeFile(String des, String fileName, int index) {
			
			if (des.length() < 2) {
				return;
			}
			System.out.println(fileName);
			File f = new File(fileName);
			Scanner s = null;
			ArrayList<String> words = new ArrayList<String>();

			try {
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						new FileInputStream(fileName)));
				String tmp = null;
				while ((tmp = bin.readLine()) != null) {
					words.add(tmp);
				}

				bin.close();
			} catch (IOException ioe) {
			}

			System.out.println(words.size());
			if (words.size() < 2)
				return;

			PrintWriter of = null;
			try {
				 of = new PrintWriter(new File(""+index+"_train.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			
			of.println(des + "|"+fileName);

			if ( ht.get(des) != null) {
				of.println(ht.get(des));
				of.close();
				return;
			} else {
				ht.put(des, index);
			}
			/**
			 * Analyze Genres for each description
			 */
			ArrayList<String> genres = analyzeGenres(des, words);

			if (genres != null && !genres.isEmpty()) {
				of.print("genres:\t");
				of.println(genres);
			}

			/**
			 * Analyze Director for each description
			 */
			ArrayList<String> director = analyzeDirector(des, words);

			if (director != null && !director.isEmpty()) {
				of.print("directors:\t");
				of.println(director);
			}

			/**
			 * Analyze Actors for each description
			 */
			ArrayList<String> actor = analyzeActor(des, words);

			if (actor != null && !actor.isEmpty()) {
				of.print("actors:\t");
				of.println(actor);
			}

			/**
			 * Analyze Actresses for each description
			 */
			ArrayList<String> actress = analyzeActress(des, words);

			if (actress != null && !actress.isEmpty()) {
				of.print("actresses:\t");
				of.println(actress);
			}

			of.close();
			return;
		}

		private ArrayList<String> analyzeDirector(String des,
				ArrayList<String> words) {
			File f = new File("directors.list");
			Scanner s = null;

			ArrayList<String> desWords = new ArrayList<String>();
			s = new Scanner(des);
			while (s.hasNext()) {
				desWords.add(s.next());
			}
			if (desWords.size() < 2)
				return null;

			ArrayList<String> tempList = new ArrayList<String>();
			String firstWord = desWords.get(0).toLowerCase();
			String secondWord = desWords.get(1).toLowerCase();
			try {
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String tmp = null;
				while ((tmp = bin.readLine()) != null) {
					if (tmp.indexOf("THE DIRECTORS LIST") > -1)
						break;
				}

				String director = "";
				while ((tmp = bin.readLine()) != null) {
					if (tmp.equals(""))
						continue;
					if (tmp.indexOf("SUBMITTING UPDATES") > -1)
						break;
					if (tmp.charAt(0) != '\t') {
						if (tmp.indexOf('\t') > -1) {
							director = tmp.substring(0, tmp.indexOf("\t"))
									.replace('\t', ' ').trim().toLowerCase();
							tmp = tmp.substring(tmp.indexOf('\t'), tmp.length());
						}
					}
					tmp = tmp.replace('\t', ' ');
					if (tmp.indexOf('{') > -1)
						tmp = tmp.substring(0, tmp.indexOf('{'));

					tmp = tmp.replace('"', ' ').replace('(', ' ').replace(')', ' ')
							.trim().toLowerCase();

					if (tmp.indexOf(firstWord) != -1
							&& tmp.indexOf(secondWord) != -1
							&& tmp.indexOf(firstWord) < tmp.indexOf(secondWord)) {
						tempList.add(tmp + " " + director.replace(' ', '_'));
					}
				}
				bin.close();
			} catch (IOException ioe) {
			}

			int count = 0;
			String str = null;
			ArrayList<String> tempList2 = new ArrayList<String>();
			ArrayList<String> doctors = new ArrayList<String>();
			for (int i = 0; i < tempList.size(); i++) {
				count = 0;
				str = tempList.get(i);
				for (int j = 0; j < desWords.size(); j++) {
					if (str.indexOf(desWords.get(j)) != -1)
						count++;
				}
				if (1.0 * count / desWords.size() > 0.95) {
					s = new Scanner(str);
					int count2 = 0;
					String lastword = "";
					while (s.hasNext()) {
						count2++;
						lastword = s.next();
					}
					if (1.0 * count / count2 > 0.51 && 1.0 * count / count2 < 1.01) {
						tempList2.add(str);
						doctors.add(lastword);
					}
				}
			}
			System.out.println(firstWord + " " + secondWord);
			System.out.println(tempList2.size());

			return doctors;
		}

		private ArrayList<String> analyzeActor(String des, ArrayList<String> words) {
			File f = new File("actors.list");
			Scanner s = null;

			ArrayList<String> desWords = new ArrayList<String>();
			s = new Scanner(des);
			while (s.hasNext()) {
				desWords.add(s.next());
			}
			if (desWords.size() < 2)
				return null;

			ArrayList<String> tempList = new ArrayList<String>();
			String firstWord = desWords.get(0).toLowerCase();
			String secondWord = desWords.get(1).toLowerCase();
			try {
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String tmp = null;
				while ((tmp = bin.readLine()) != null) {
					// System.out.println(tmp);
					if (tmp.indexOf("THE ACTORS LIST") > -1)
						break;
				}

				String actor = "";
				while ((tmp = bin.readLine()) != null) {
					if (tmp.equals(""))
						continue;
					if (tmp.indexOf("SUBMITTING UPDATES") > -1)
						break;
					if (tmp.charAt(0) != '\t') {
						if (tmp.indexOf('\t') > -1) {
							actor = tmp.substring(0, tmp.indexOf("\t"))
									.replace('\t', ' ').trim().toLowerCase();
							// System.out.println(actor);
							tmp = tmp.substring(tmp.indexOf('\t'), tmp.length());
						}
					}
					tmp = tmp.replace('\t', ' ');
					if (tmp.indexOf('{') > -1)
						tmp = tmp.substring(0, tmp.indexOf('{'));

					tmp = tmp.replace('"', ' ').replace('(', ' ').replace(')', ' ')
							.trim().toLowerCase();

					if (tmp.indexOf(firstWord) != -1
							&& tmp.indexOf(secondWord) != -1) {
						tempList.add(tmp + " " + actor.replace(' ', '_'));
						// System.out.println (tmp+" "+actor.replace(' ', '_'));
					}
				}
				bin.close();
			} catch (IOException ioe) {
			}

			int count = 0;
			String str = null;
			ArrayList<String> tempList2 = new ArrayList<String>();
			ArrayList<String> actors = new ArrayList<String>();
			for (int i = 0; i < tempList.size(); i++) {
				count = 0;
				str = tempList.get(i);
				for (int j = 0; j < desWords.size(); j++) {
					if (str.indexOf(desWords.get(j)) != -1)
						count++;
				}
				if (1.0 * count / desWords.size() > 0.95) {
					s = new Scanner(str);
					int count2 = 0;
					String lastword = "";
					while (s.hasNext()) {
						count2++;
						lastword = s.next();
					}
					if (1.0 * count / count2 > 0.51) {
						tempList2.add(str);
						actors.add(lastword);
					}
				}
			}
			System.out.println(firstWord + " " + secondWord);
			System.out.println(tempList2.size());

			return actors;
		}

		private ArrayList<String> analyzeActress(String des, ArrayList<String> words) {
			File f = new File("actresses.list");
			Scanner s = null;

			ArrayList<String> desWords = new ArrayList<String>();
			s = new Scanner(des);
			while (s.hasNext()) {
				desWords.add(s.next());
			}
			if (desWords.size() < 2)
				return null;

			ArrayList<String> tempList = new ArrayList<String>();
			String firstWord = desWords.get(0).toLowerCase();
			String secondWord = desWords.get(1).toLowerCase();
			try {
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String tmp = null;
				while ((tmp = bin.readLine()) != null) {
					// System.out.println(tmp);
					if (tmp.indexOf("THE ACTRESSES LIST") > -1)
						break;
				}

				String actress = "";
				while ((tmp = bin.readLine()) != null) {
					if (tmp.equals(""))
						continue;
					if (tmp.indexOf("SUBMITTING UPDATES") > -1)
						break;
					if (tmp.charAt(0) != '\t') {
						if (tmp.indexOf('\t') > -1) {
							actress = tmp.substring(0, tmp.indexOf("\t"))
									.replace('\t', ' ').trim().toLowerCase();
							// System.out.println(actor);
							tmp = tmp.substring(tmp.indexOf('\t'), tmp.length());
						}
					}
					tmp = tmp.replace('\t', ' ');
					if (tmp.indexOf('{') > -1)
						tmp = tmp.substring(0, tmp.indexOf('{'));

					tmp = tmp.replace('"', ' ').replace('(', ' ').replace(')', ' ')
							.trim().toLowerCase();

					if (tmp.indexOf(firstWord) != -1
							&& tmp.indexOf(secondWord) != -1) {
						tempList.add(tmp + " " + actress.replace(' ', '_'));
						// System.out.println (tmp+" "+actor.replace(' ', '_'));
					}
				}
				bin.close();
			} catch (IOException ioe) {
			}

			int count = 0;
			String str = null;
			ArrayList<String> tempList2 = new ArrayList<String>();
			ArrayList<String> actresses = new ArrayList<String>();
			for (int i = 0; i < tempList.size(); i++) {
				count = 0;
				str = tempList.get(i);
				for (int j = 0; j < desWords.size(); j++) {
					if (str.indexOf(desWords.get(j)) != -1)
						count++;
				}
				if (1.0 * count / desWords.size() > 0.95) {
					s = new Scanner(str);
					int count2 = 0;
					String lastword = "";
					while (s.hasNext()) {
						count2++;
						lastword = s.next();
					}
					if (1.0 * count / count2 > 0.51) {
						tempList2.add(str);
						actresses.add(lastword);
					}
				}
			}
			System.out.println(firstWord + " " + secondWord);
			System.out.println(tempList2.size());

			return actresses;
		}

		private ArrayList<String> analyzeGenres(String des, ArrayList<String> words) {
			File f = new File("genres.list");
			Scanner s = null;

			ArrayList<String> desWords = new ArrayList<String>();
			s = new Scanner(des);
			while (s.hasNext()) {
				desWords.add(s.next());
			}
			if (desWords.size() < 2)
				return null;

			ArrayList<String> tempList = new ArrayList<String>();
			String firstWord = desWords.get(0).toLowerCase();
			String secondWord = desWords.get(1).toLowerCase();
			try {
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String tmp = null;
				while ((tmp = bin.readLine()) != null) {
					if (tmp.indexOf("THE GENRES LIST") > -1)
						break;
				}

				while ((tmp = bin.readLine()) != null) {
					// System.out.println(tmp);
					tmp = tmp.toLowerCase();
					if (tmp.indexOf(firstWord) != -1
							&& tmp.indexOf(secondWord) != -1) {
						tempList.add(tmp);
					}
				}
				bin.close();
			} catch (IOException ioe) {
			}

			int count = 0;
			String str = null;
			ArrayList<String> tempList2 = new ArrayList<String>();
			ArrayList<String> genres = new ArrayList<String>();
			for (int i = 0; i < tempList.size(); i++) {
				count = 0;
				str = tempList.get(i);
				for (int j = 0; j < desWords.size(); j++) {
					if (str.indexOf(desWords.get(j)) != -1)
						count++;
				}
				if (1.0 * count / desWords.size() > 0.95) {
					s = new Scanner(str);
					int count2 = 0;
					String lastword = "";
					while (s.hasNext()) {
						count2++;
						lastword = s.next();
					}
					if (1.0 * count / count2 > 0.51) {
						tempList2.add(str);
						genres.add(lastword);
					}
				}
			}
			System.out.println(firstWord + " " + secondWord);
			System.out.println(tempList2.size());

			return genres;
		}

		public static void main(String[] args) {
			GenerateDataFile bf = new GenerateDataFile();
			bf.init();
			bf.analyze();
		}

	}
