import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class myUtils {
	public static int countWord(String word, File doc) {
		int returnValue = 0;
		Scanner scanLine = null;
		try {
			scanLine = new Scanner(doc);
			String Aword = null;

			while (scanLine.hasNext()) {
				Aword = scanLine.next();
				if (Aword.toLowerCase().contains(word))
					returnValue++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public static  int[] calAvirup(File f) {
//		double accuracy = 0;
		
		double correct=0;
		double total=0;
		double baseline = 0;
		double tempCount =1;
		int index =0;
		try {
			double score = 0;
			boolean isCorrect = true;
			Scanner scan1 = new Scanner(f);
			while (scan1.hasNext()) {
				String Aline = scan1.nextLine();
				index++;
				String[] line = Aline.split("\t");
				if (line[0].equals( "yes")){
					//System.out.println("yes");
					score = Double.parseDouble(line[2]);
					if(isCorrect == true)
						correct++;
					//else
						//System.out.println(total);
					total++;
					//System.out.println(correct+" "+total);
					isCorrect=true;
					baseline+= 1/tempCount;
					tempCount=1;
				}
				else if (score < Double.parseDouble(line[2])) {
					isCorrect = false;
					tempCount++;
				}
				else
					tempCount++;
			}
			System.out.println((baseline-1)/total);
			System.out.println((correct-1)/total);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] errorSet = new int[index];
		index=0;
		tempCount=1;
		try {
			double score = 0;
			boolean isCorrect = true;
			Scanner scan1 = new Scanner(f);
			while (scan1.hasNext()) {
				String Aline = scan1.nextLine();
				index++;
				String[] line = Aline.split("\t");
				if (line[0].equals( "yes")){
					//System.out.println("yes");
					score = Double.parseDouble(line[2]);
					if(isCorrect == true)
						{
						correct++;
						for(int k = 0;k<tempCount;k++)
							if(index -k-2>=0)
								errorSet[index-k-2]=1;
						}
					else
						{
						for(int k = 0;k<tempCount;k++)
								errorSet[index-k-2]=0;
						}
					total++;
					//System.out.println(correct+" "+total);
					isCorrect=true;
					baseline+= 1/tempCount;
					tempCount=1;
				}
				else if (score < Double.parseDouble(line[2])) {
					isCorrect = false;
					tempCount++;
				}
				else
					tempCount++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorSet;
	}
	public static double count(String line, File doc) {

		double returnValue = 0;

		ArrayList<String> tempStr = new ArrayList<String>();
		String[] temp;
		line = line.replaceAll(",_", " ");

		int index = line.indexOf("[");
		int indexOfEnd = line.indexOf("]");
		if (indexOfEnd < 0)
			indexOfEnd = line.length();

		line = line.substring(index + 1, indexOfEnd);


//		 partial match
		line = line.replaceAll(",", "");
		temp = line.split(" ");
//		 exact match
//		temp = line.split(",");
		
		for (int i = 0; i < temp.length; i++) {
			int indexOfBrace = temp[i].indexOf("_");
			if (indexOfBrace > -1)
				temp[i] = temp[i].substring(0, indexOfBrace);
		}

		tempStr.add(temp[0]);
		boolean isUnique = true;
		for (int i = 1; i < temp.length; i++) {
			if (temp[i].equals(""))
				continue;

			for (int j = 0; j < tempStr.size(); j++) {
				if (temp[i].equals(tempStr.get(j)))
					isUnique = false;
			}
			if (isUnique == true)
				tempStr.add(temp[i]);
		}

		BufferedReader scanLine = null;

		for (int i = 0; i < tempStr.size(); i++) {
			try {
				scanLine = new BufferedReader(new InputStreamReader(
						new FileInputStream(doc)));
				String ALine = null;
				try {
					while ((ALine = scanLine.readLine()) != null) {
						if (ALine.toLowerCase().contains(tempStr.get(i)))
							{returnValue++;
//							break;
							}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

//		return returnValue/(double)tempStr.size();
		return returnValue;

	}

	public static boolean isWordAppear(String line, File doc) {
		String[] tempStr = line.split(" ");

		BufferedReader scanLine = null;

		for (int i = 0; i < tempStr.length; i++) {

			int indexOfBrace = tempStr[i].indexOf("(");
			if (indexOfBrace > -1)
				tempStr[i] = tempStr[i].substring(0, indexOfBrace);

			try {
				scanLine = new BufferedReader(new InputStreamReader(
						new FileInputStream(doc)));
				String ALine = null;
				try {
					while ((ALine = scanLine.readLine()) != null) {
						if (ALine.toLowerCase().contains(tempStr[i]))
							return true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return false;

	}

	public static int movieNameMatch(ArrayList<String> movies, File doc) {
		BufferedReader scanLine = null;
		int count = 0;

		for (int i = 0; i < movies.size(); i++) {

			String movie = movies.get(i);

			movie.replaceAll("\"", "");

			int indexOfBrace = movie.indexOf("(");
			if (indexOfBrace > -1)
				movie = movie.substring(0, indexOfBrace);

			String[] trash = movie.split(" ");
			if (trash.length < 3)
				continue;
			// System.out.println(movie);

			try {
				scanLine = new BufferedReader(new InputStreamReader(
						new FileInputStream(doc)));
				String ALine = null;
				try {
					while ((ALine = scanLine.readLine()) != null) {
						if (ALine.toLowerCase().contains(movie)) {
							count++;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return count;
	}
	public static void combine2(){
		try {
			Scanner scan1 = new Scanner(new File("trainingVector1.txt"));
			Scanner scan2 = new Scanner(new File("trainingVector2.txt"));
			PrintStream ps = new PrintStream(new File("trainingVector3.txt"));
			while(scan1.hasNext()){
				String temp1 = scan1.nextLine();
				String temp2 = scan2.nextLine();
				String[] str = temp2.split(" ");
				temp1 += " "+str[1]+" "+str[2];
				ps.println(temp1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void ErrorOutput(ArrayList<TargetObject> al, String str){
		int countPlace=0,countood=0,total=0,totalTarget=0;
		int[] errors = myUtils.calAvirup(new File("outputOfSportsDomain.txt"));
		try {
			Scanner scan1 = new Scanner(new File("train_prelim3.txt"));
			Scanner scan2 = new Scanner(new File("outputOfSportsDomain.txt"));
			Scanner scan3 = new Scanner(new File("sports-feat-vector1.txt"));
			
			for(int i =0;i<errors.length;i++)
				{
				String temp = scan1.nextLine();
				String temp2 = scan2.nextLine();
				String temp3 = scan3.nextLine();
					
//					if(temp.contains("yes"))
//					{
//					totalTarget++;
//					if(temp.contains("place"))
//						countPlace++;
//					if(temp.contains("ood"))
//						countood++;
//					}
//				if(errors[i]==0){
//					
					if(temp.contains("yes"))
					{
						total++;
						if(temp.contains("WIKI"))
							countPlace++;
						if(temp.contains("OOD"))
							countood++;
					System.out.println( temp3+"\t"+temp);
					}
					}
//				}
			System.out.println(countPlace+"\t"+countood+"\t"+total+"\t"+totalTarget);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		combine2();
	}
}
