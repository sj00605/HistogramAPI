import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/*
 * This class gets the folder, then extracts the files names from the folder.
 * Needs to make sure the folder is a folder. Needs to send an error if the folder is empty.
 * Needs to decompress folder if it's compressed.
 * 
 */
public class FileIngest {

	private File folderName;
	private List<File> fileNames;
	private String outputFolder;
	private String password;
	
	private static final Logger logger = LogManager.getLogger("FileIngest");
	
	public FileIngest(String file){
		
		this.password = "";
		//TODO: check if compressed and if utf-8 and if txt.
		
		//We have two paths, it's a directory, it's a file
		this.folderName = new File(file);
		if(this.folderName.exists() && this.folderName.isDirectory() && this.folderName.canRead()){
			
			//TODO: explore folder and put files into list;
			fileNames = extractFiles();
			
			for(File f : fileNames){
				
				WordCount wc = new WordCount();
				
			}
			
			
			
		}else if(!this.folderName.isDirectory()){
			logger.debug("File is not directory. Treating as single file");
			
		}else if(!this.folderName.exists()){
			logger.debug("File does not exist");
			
		}else if(!this.folderName.canRead()){
			logger.debug("Application does not have read permission");
		}
	}
	
	public File getFolderName() {
		return this.folderName;
	}

	public void setFolderName(File folderName) {
		this.folderName = folderName;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public List<File> getFileNames(){
		return fileNames;
	}
	
	public void setFileNames(List<File> fileNames){
		this.fileNames = fileNames;
	}
	
	public void addFile(File fileName){
		
		//TODO: check if file exists and is not folder
		this.fileNames.add(fileName);
	}
	
	/*
	 * Assumes output folder is already created
	 */
	public void unZip(File file, String outputFolder){
		
		try {
	         ZipFile zipFile = new ZipFile(file);
	         if (zipFile.isEncrypted()) {
	            zipFile.setPassword(password);
	         }
	         zipFile.extractAll(outputFolder);
	    } catch (ZipException e) {
	        e.printStackTrace();
	    }
	
	}
	
	//TODO: set up filter
	public static List<File> extractFiles(File directory){
		List<File> res = new ArrayList<File>();
		Queue<File> q = new PriorityQueue<File>();
		
		//search top folder for directories
		//add to queue
		//keep going until queue is empty;
		
		//Step 1 : populate queue
		File[] files = listFilesFilter(directory, "txt");
		for(int i=0; i<files.length; i++)
		{
			q.add(files[i]);
		}
		//Step 2 : iterate through queue
		while(!q.isEmpty()){
			
			File temp = q.poll();
			if(temp.isDirectory()){
				
			}
		}
		
	}
	
	public static File[] listFilesFilter(File f, final String ext){
		
		File[] files = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(ext);
		    }
		});
		
		return files;
	}
	
	//TODO ask the user if they want to overwrite the folder;
	public void createOutputFolder(String file) throws FileAlreadyExistsException{
		
		this.outputFolder = new File(file);
		if(!outputFolder.exists()){
			outputFolder.mkdir();
		}else{
			throw new FileAlreadyExistsException("Output Directory already exists");
		}
	}
}
