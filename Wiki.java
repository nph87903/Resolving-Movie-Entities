import java.io.*;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Wiki {

	private int match;
	private static HashSet<String> stop_list = new HashSet<String>();

	public Wiki() {
		match = 0;
		getList();
	}

	public int getMatch() {
		return this.match;
	}

	// get the stop list
	public void getList() {

		try {
			File f = new File("stop_list_2.txt");
			Scanner scan = new Scanner(f);
			while (scan.hasNext()) {
				String s = scan.next();
				stop_list.add(s);
			}
		} catch (Exception e) {
		}
	}

	// the wiki source and target pages
	public double setMatch(File source, File target) {

		String s_token = "";
		String t_token = "";
		int i =0;
		HashSet<String> s_list = new HashSet<String>();
		HashSet<String> t_list = new HashSet<String>();

		try {
			Scanner scan1 = new Scanner(source);
			Scanner scan2 = new Scanner(target);

			// remove stop words from list1
			while (scan1.hasNext()) {
				s_token = scan1.next();
				s_list.add(s_token);
			}
			s_list.removeAll(stop_list);
			i=s_list.size();
			// remove stop words from list2
			while (scan2.hasNext()) {
				t_token = scan2.next();
				t_list.add(t_token);
			}
			t_list.removeAll(stop_list);
			// compareMatches
			s_list.retainAll(t_list);

		} catch (Exception e) {
		}
		if(i!=0)
			return (double)s_list.size()/(double)i;
		else
			return 0;
	}

	public void write(String to_write) {

		BufferedWriter bw;
		FileWriter fw;
		try {
			fw = new FileWriter("C:\\java\\wiki_word.txt", true);
			bw = new BufferedWriter(fw);
			bw.write(to_write + "\n");
			bw.close();
		} catch (Exception e) {
		}
	}

	// add Wiki File and Wiki Target
	// public static void main(String[]args){
	// Wiki w = new Wiki();
	// File f1 = new File("C:\\java\\TropicofCancer\\file4.txt");
	// File f2 = new File("C:\\java\\TropicofCancer\\file4.txt");
	// w.setMatch(f1, f2 );
	// }
}