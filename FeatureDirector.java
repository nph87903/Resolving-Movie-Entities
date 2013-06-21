import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;

public class FeatureDirector {

	public static double[] directorLabelFeature(ArrayList<TargetObject> al,
			String fileLocation) {
		String label = "director";
		double[] counts = new double[al.size()];
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getLabel() == 1)
				counts[i] = myUtils.countWord(label, al.get(i).getFile());
			else
				counts[i] = 0;
		}
		return counts;
	}

	public static double[] directorFeature(ArrayList<TargetObject> al,
			String fileLocation) {
		double[] counts = new double[al.size()];
		boolean inIMDB = false;
		for (int index = 0; index < al.size(); index++) {
			String line = "";
			int indexOfFile = 0;
			Scanner scanMovieDetail = null;
			File movieDetail = new File(fileLocation
					+ al.get(index).getFileIndex() + "_train.txt");
			try {
				scanMovieDetail = new Scanner(movieDetail);
				if (scanMovieDetail.hasNextLine())
					line = scanMovieDetail.nextLine();
				if (scanMovieDetail.hasNextInt()) {
					indexOfFile = scanMovieDetail.nextInt();
					movieDetail = new File(fileLocation + indexOfFile
							+ "_train.txt");
				}
				scanMovieDetail.close();

				scanMovieDetail = new Scanner(movieDetail);
				inIMDB = false;
				while (scanMovieDetail.hasNext()) {
					line = scanMovieDetail.nextLine();
					if (line.contains("directors:")) {
						if (al.get(index).getLabel() == 1)
							counts[index] = myUtils.count(line, al.get(index)
									.getFile());
						else
							counts[index] = 0;
						inIMDB = true;
					}
				}
				if (inIMDB == false)
					counts[index] = 0;
			} catch (FileNotFoundException e) {
				counts[index] = -10;
			}
		}
		return counts;
	}

	public static int[] similarDirectors(ArrayList<TargetObject> al,
			String fileLocation,
			Hashtable<String, ArrayList<String>> director_dict) {

		HashSet<String> directors = new HashSet<String>();

		int[] counts = new int[al.size()];

		for (int index = 0; index < al.size(); index++) {
			directors.clear();

			String line = "";
			int indexOfFile = 0;
			Scanner scanMovieDetail = null;
			File movieDetail = new File(fileLocation
					+ al.get(index).getFileIndex() + "_train.txt");

			try {
				scanMovieDetail = new Scanner(movieDetail);
				if (scanMovieDetail.hasNextLine())
					line = scanMovieDetail.nextLine();
				if (scanMovieDetail.hasNextInt()) {
					indexOfFile = scanMovieDetail.nextInt();
					movieDetail = new File(fileLocation + indexOfFile
							+ "_train.txt");
				}
				scanMovieDetail.close();
				// System.out.println(movieDetail);
				scanMovieDetail = new Scanner(movieDetail);

				while (scanMovieDetail.hasNextLine()) {
					line = scanMovieDetail.nextLine();
					if (line.contains("directors:")) {
						int index1 = line.indexOf("[");
						int indexOfEnd = line.indexOf("]");
						if (indexOfEnd < 0)
							indexOfEnd = line.length();

						line = line.substring(index1 + 1, indexOfEnd);

						String[] lines = line.split(", ");
						for (String l : lines) {
							l = l.replace('_', ' ');
							if (directors.contains(l))
								continue;
							if (myUtils
									.isWordAppear(l, al.get(index).getFile()))
								directors.add(l);
						}
					}
				}

				counts[index] = 0;
				for (String str : directors) {
					if (director_dict.get(str) == null)
						continue;
					counts[index] += myUtils.movieNameMatch(
							director_dict.get(str), al.get(index).getFile());
				}

			} catch (FileNotFoundException e) {
				counts[index] = -10;
			}
			System.out.println("Director Feature:" + counts[index]);

		}
		return counts;
	}

}
