import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FeatureGenre {
	public static double[] genreLabelFeature(ArrayList<TargetObject> al,
			String fileLocation) {
		String label = "genre";
		double[] counts = new double[al.size()];
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getLabel() == 1)
				counts[i] = myUtils.countWord(label, al.get(i).getFile());
			else
				counts[i] = 0;
		}
		return counts;
	}

	public static double[] genraFeature(ArrayList<TargetObject> al,
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
				// System.out.println(movieDetail);
				scanMovieDetail = new Scanner(movieDetail);
				inIMDB = false;
				while (scanMovieDetail.hasNext()) {
					line = scanMovieDetail.nextLine();
					if (line.contains("genres:")) {
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
}
