import java.io.File;


public class TargetObject {
		private boolean flag;
		private File file;
		private File wikiFile;
		private int label; 
		private int fileIndex;
		
		public int getLabel(){
			return label;
		}
		public void setLabel(int label){
			this.label = label;
		}

		public int getFileIndex(){
			return fileIndex;
		}
		public void setFileIndex(int fileIndex){
			this.fileIndex = fileIndex;
		}
		public void setFlag(boolean des){
			this.flag= des;
		}
		
		
		public TargetObject( boolean flag, File file,File wikiFile,int label, int fileIndex){
		
			this.flag = flag;
			this.file = file;
			this.wikiFile = wikiFile;
			this.label = label;
			this.fileIndex = fileIndex;
			}
		
		public File getWikiFile(){
			return wikiFile;
		}
		
		
		public void setFile(File file){
		  
			this.file = file;
		}
		public void setWikiFile(File file){
		
			this.wikiFile = file;
		}
		
		public boolean getFlag(){
			return this.flag;
		}
		public File getFile(){
			return file;
		}

		public String toString(){
			return(flag+" ");
		}
	}

