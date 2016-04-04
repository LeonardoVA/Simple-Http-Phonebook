import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Database {
	static String defaultFilenameWindows = "phonebookDatabase.txt";
	static String defaultFilenameNonWindows = ".phonebookDatabase";
	static boolean windowsOs;
	
    //	- List all entries in the phone book.
	public static void listAll() throws IOException{
		ArrayList<Entries> entries = new ArrayList<Entries>();
		//read all entries into arraylist
		entries = readEntriesFromFile();
		//print them out.	
		printEntries(true,entries);
	}
	
    //	- Create a new entry to the phone book.
	public static void addEntry(Entries entry) throws IOException{
		ArrayList<Entries> entriesArrayList = new ArrayList<Entries>();
		//read all entries into arraylist
		entriesArrayList = readEntriesFromFile();
		//Add new entry to the list @ index 0
		entriesArrayList.add(0,entry);
		//Sort the arraylist
		sortEntriesBySurname(entriesArrayList);
		//write modified & sorted arraylist to file 
		writeToFile(entriesArrayList);
		
	}
	//	- Remove an existing entry in the phone book.
	public static void removeEntry(int entryNumber) throws Exception{
		ArrayList<Entries> entriesArrayList = new ArrayList<Entries>();
		//read all entries into arraylist
		entriesArrayList = readEntriesFromFile();
		//check valid entry
		if (entryNumber > (entriesArrayList.size()-1)){
			throw new Exception(); 
		}
		//remove one entry
		entriesArrayList.remove(entryNumber);
		//write entries to file.
		writeToFile(entriesArrayList);
	}

	//	- Update an existing entry in the phone book.
	public static void editEntry(int entryNumber, String firstname, String surname, String number,
			String address) throws Exception{
		ArrayList<Entries> entriesArrayList = new ArrayList<Entries>();
		//read all entries into arraylist
		entriesArrayList = readEntriesFromFile();
		//check valid entry number given
		if (entryNumber > (entriesArrayList.size()-1)){
			throw new Exception(); 
		}
		//edit something
		Entries entry = entriesArrayList.get(entryNumber);
		
		entry.setFirstname(firstname);
		entry.setSurname(surname);
		entry.setNumber(number);
		entry.setAddress(address);
		entriesArrayList.remove(entryNumber);
		entriesArrayList.add(entryNumber, entry);
		//write entries to file.
		writeToFile(entriesArrayList);
	}
	

	//	- Search for entries in the phone book by surname.
	public static void searchEntries(String searchterms) throws IOException{
		ArrayList<Entries> entriesArrayList = new ArrayList<Entries>();
		//read all entries into arraylist
		entriesArrayList = readEntriesFromFile();
		//for each entry if surname does not contain search term remove, starting from the bottom 
		for (int i = entriesArrayList.size()-1; i >= 0; i--){
			Entries entry = entriesArrayList.get(i);
			if (!entry.getSurname().contains(searchterms)){
				entriesArrayList.remove(i);
			}
		}

		printEntries(false, entriesArrayList);
		
	}
	
	//printing out entries to console with addition of entry numbers if Listing
	public static void printEntries(boolean listAllValues,ArrayList<Entries> entriesArraylist){
		if (!entriesArraylist.isEmpty()){
			if (listAllValues){
				System.out.println("Listing all results:<br><br>");
				int entryNumber = 0;
				for (Entries Entry : entriesArraylist){
					System.out.println("Entry Number: " + entryNumber + "<br>");
					System.out.println("Firstname: " + Entry.getFirstname() + "<br>");
					System.out.println("Surname: " + Entry.getSurname() + "<br>");
					System.out.println("Phone Number: " + Entry.getNumber() + "<br>");
					System.out.println("Address: " + Entry.getAdress() + "<br><br>");
					entryNumber++;
				}
			} else{
				System.out.println("Listing search results:<br><br>");
				for (Entries Entry : entriesArraylist){
					System.out.println("Firstname: " + Entry.getFirstname() + "<br>");
					System.out.println("Surname: " + Entry.getSurname() + "<br>");
					System.out.println("Phone Number: " + Entry.getNumber() + "<br>");
					System.out.println("Address: " + Entry.getAdress() + "<br><br>");
				}
				
			}
		} else{
			System.out.println("No values found.");
		}
	}
	
	// Sort aplabetically by Surname.
	public static ArrayList<Entries> sortEntriesBySurname(ArrayList<Entries> entriesArraylist) throws IOException{
		//as the arraylist will always be sorted (comes sorted to start with) save at most 1 entry (when added or edited) 
		//we can use a fairly simple method for sorting, placing the new/edited entry at the top of the arraylist and 
		//performing a linear comparison with each following entry to see which string comes first.
		
		//loops through all entries, checks if surname is alphabetically > than next entry if so swaps them.
		for (int i = 0; i < entriesArraylist.size()-1; i++ ){
			if (entriesArraylist.get(i).getSurname().compareToIgnoreCase(entriesArraylist.get(i + 1).getSurname())>0){
				//System.out.println("Entry " + i + " Greater then entry " + (i+1) + " Swapping them!");
				Entries placeholderEntry = entriesArraylist.get(i);
				entriesArraylist.remove(i);
				entriesArraylist.add(i+1, placeholderEntry);
			}
		}
		return entriesArraylist;
		}
	
	// - method to read from file.
	public static ArrayList<Entries> readEntriesFromFile() throws IOException{
		
        Charset cs = Charset.forName("UTF-8");
        //TODO either specify files must be encoded with utf-8 or check OS and use deafult.
        List<String> linesList = Files.readAllLines(getFilePath(), cs);
        ArrayList<Entries> listOfEntries = new ArrayList<Entries>();
        for (String line : linesList){
        	String[] values = line.split("\\!");
        	if (values.length > 3){
        		listOfEntries.add(new Entries(values[0],values[1],values[2],values[3]));
        	} else {
        		listOfEntries.add(new Entries(values[0],values[1],values[2]));
        	}
        }
        return listOfEntries;	
	}
	//write arraylist to file in format.
	public static void writeToFile(ArrayList<Entries> entriesArraylist) throws IOException{
		PrintWriter pw = new PrintWriter(getFilePath().toString());
		for (int i = 0; i < entriesArraylist.size(); i++){
			Entries entry = entriesArraylist.get(i);
			pw.println(entry.getFirstname() + "!" + entry.getSurname() 
					+ "!" + entry.getNumber() + "!" + entry.getAdress());
		}	
		pw.close();
	}
	
	//get filepath dependant on os. //
    public static Path getFilePath()
    {
        String osName = System.getProperty("os.name");
        String homeDir   = System.getProperty("user.home");
        windowsOs = osName.startsWith("Windows");

        String fileName = windowsOs
                ? defaultFilenameWindows
                : defaultFilenameNonWindows;

        return Paths.get(homeDir, fileName);
    }
    
    //check file exists
    public static boolean checkFile(){
    	// Check the file exists as a normal file
        File file = getFilePath().toFile();
        if ( ! file.exists() || file.isDirectory() ) {
            System.err.println("File does not exist or is directory");
            return false;
        }
        return true;
    	
    }
}