import java.io.File;

public class DeleteExtraFile {

  private static File[] getFiles(String fileFolder) {
    File folder = new File(fileFolder);
    return folder.listFiles();
  }
  
  public static void main(String[] args) {
    File[] allFiles = getFiles("C:\\Users\\NoahOlson\\eclipse-workspace\\Home_Projects\\Organize_Senate_And_House_Voting_Information\\src");
    for(File file: allFiles) {
      String fileName = file.getName();
      if(fileName.contains("Copy")) {
        file.delete();
      }
    }
  }

}
