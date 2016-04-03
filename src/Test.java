import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Test {
	

	public static void runTests() throws Exception{
		
		if (!Database.checkFile()){
        	System.out.println("File test failed, file not found please add it to above location!");
        	System.exit(1);
        }
		
		if (addTest()){
			System.out.println("addTest Passed.");
		} else {
			System.out.println("addTest FAILED.");
		} 
		
		if (editTest()){
			System.out.println("editTest Passed.");
		} else {
			System.out.println("editTest FAILED.");
		} 
		
		if (removeTest()){
			System.out.println("removetest Passed.");
		} else {
			System.out.println("removetest FAILED.");
		} 
		
	}
	public static void resetOut(){
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));		
	}
	
	private static boolean addTest() throws IOException{
		//set output console each test has its own so no cross contamination
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		//setup entries
		Entries entry = new Entries("Testfirst","aaaTestsur","0000000000","333 test adresss street uk");
		Database.addEntry(entry);
		//search entries 
		Database.searchEntries("Test");;
		//get output
		String output = baos.toString();
		//return output to default
		resetOut();
		//check output
		if (output.contains("Testfirst") && output.contains("aaaTestsur") && output.contains("0000000000")
				&& output.contains("333 test adresss street uk")){
			return true;			
		}
		return false;
	}
	
	private static boolean editTest() throws Exception{
		//set output console each test has its own so no cross contamination
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Database.editEntry(0, "Test1", "aaasur", "999999999", "nowhere");
		Database.listAll();
		//get output
		String output = baos.toString();
		//return output to default
		resetOut();
		//check output
		if (!output.contains("Testfirst") && !output.contains("aaaTestsur") && !output.contains("0000000000")
				&& !output.contains("333 test adresss street uk") && output.contains("Test1")
				&& output.contains("aaasur") && output.contains("999999999") && output.contains("nowhere")){
			return true;			
		}
		return false;
	}
	
	private static boolean removeTest() throws Exception{
		//set output console each test has its own so no cross contamination
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Database.removeEntry(0);
		Database.listAll();
		//get output
		String output = baos.toString();
		//return output to default
		resetOut();
		//check output
		if (!output.contains("Test1") && !output.contains("aaasur") && 
				!output.contains("999999999") && !output.contains("nowhere")){
			return true;			
		}
		return false;		
	}

}
