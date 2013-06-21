import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class ErrorOutput {
	
	public static void prelimOutput(String movieFolder) throws IOException{
		int count1 =0,count2=0,count3 =0;
		BuildTrainingObjForAllTargets bt = new BuildTrainingObjForAllTargets();
		bt.readDir(new File(movieFolder));
		bt.objectToTargets();
		ArrayList<TargetObject> allTargets = bt.getTargets();
		FileWriter fstream;
		BufferedWriter out;
		fstream = new FileWriter(new File("Train_preForTraning.txt"),true);
		out = new BufferedWriter(fstream);
		for(TargetObject to:allTargets){
			String name="";
			if(to.getLabel()==1)
				name="MOVIE";
			if(to.getLabel()==2)
				name ="WIKI";
			if(to.getLabel()==3)
				name ="OOD";
			out.write(to.getFile()+"\t"+to.getFlag()+"\t"+name+"\n");
		if(to.getFlag()==true){
			if(to.getLabel()==1)
				count1++;
			if(to.getLabel()==2)
				count2++;
			if(to.getLabel()==3)
				count3++;
		}	
		}
		out.close();
		System.out.println(count1+" movies; "+count2+" wiki targets; "+count3+ " OOD targets!" );
	}
	
	public static String findError(int index) throws FileNotFoundException{
		Scanner scan1 = new Scanner(new File("Train_preForTraining.txt"));
		Scanner scan2 = new Scanner(new File("output.txt"));
		int count=0;
		int errorIndex = 0;
		String temp="";
		double score = 0;
		double score2 =0;
		while(scan2.hasNext()){
			temp = scan2.nextLine();
			if(count==index)
				{
				score = Double.parseDouble(temp.split("\t")[2]);
				temp = scan2.nextLine();
				count++;
				while(!temp.split("\t")[0].equals("yes"))
					{
					score2 = Double.parseDouble(temp.split("\t")[2]);
					if(score<score2){
						errorIndex = count;
						score = score2;
						}
					if(scan2.hasNext())
						{temp= scan2.nextLine();
					count++;}
					else break;
					}
				break;
				}
			count++;
		}
		count=0;
		while(scan1.hasNext()){
			temp = scan1.nextLine();
			if(count == errorIndex){
//				System.out.println(errorIndex+") "+temp);
				break;
			}
			count++;
		}
		return temp.split("\t")[2];
	}
	public static void errorReport(){
		int count1 =0,count2=0,count3 =0;
		int count11 =0,count21=0,count31 =0;
		int count12 =0,count22=0,count32 =0;
		int count13 =0,count23=0,count33 =0;
		
		int[] errors = myUtils.calAvirup(new File("output.txt"));
		try {
			Scanner scan1 = new Scanner(new File("Train_preForTraining.txt"));
			Scanner scan2 = new Scanner(new File("output.txt"));
			for(int i =0;i<errors.length;i++)
				{
				String temp1 = scan1.nextLine();
				String temp2 = scan2.nextLine();
//				System.out.println(errors[i]);
				if(errors[i]==0)
					if(temp1.contains("true"))
					{
						String errorLine = findError(i);
						if(temp1.split("\t")[2].equals("MOVIE"))
							{count1++;
							if(errorLine.equals("MOVIE"))
								count11++;
							if(errorLine.equals("WIKI"))
								count12++;
							if(errorLine.equals("OOD"))
								count13++;
							}
						if(temp1.split("\t")[2].equals("WIKI"))
							{
							count2++;
							if(errorLine.equals("MOVIE"))
								count21++;
							if(errorLine.equals("WIKI"))
								count22++;
							if(errorLine.equals("OOD"))
								count23++;
							
							}
						if(temp1.split("\t")[2].equals("OOD"))
							{count3++;
							if(errorLine.equals("MOVIE"))
								{count31++;
								System.out.println(i);
								}
							if(errorLine.equals("WIKI"))
								count32++;
							if(errorLine.equals("OOD"))
								count33++;
							}
						
					}
//					System.out.println(i+1 +")   "+temp);
				}
			System.out.println(count1+" movies are predicted wrong: "+count11+" "+count12+" "+count13+" \n"
				+count2+" wiki targets are predicted wrong "+count21+" "+count22+" "+count23+" \n"
					+count3+" OOD targets are predicted wrong "+count31+" "+count32+" "+count33+" ");
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		errorReport();
	}
}
