import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main extends AbstractHandler {
	public static String databaselocation = "Database file expected -> " + Database.getFilePath() + " <- <br>";
	
	public static void main(String[] args) throws Exception{
	System.out.println(databaselocation);
	
	//Database.listAll();
	
	System.out.println("Begining Tests");
	
	Test.runTests();
	
	//server setup
	Server server = new Server(6060);
    server.setHandler(new Main());
    server.start();
    server.join();
	}

	//handler for http requests
	@Override
	public void handle(String url, Request request, HttpServletRequest servletRequest, HttpServletResponse response)
			throws IOException, ServletException {
		
		System.out.println("url : " + url);
		
		response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Phonebook</h1>");
        request.setHandled(true);
        
        //removes first /
        if (url.startsWith("/", 0)){
        	url = url.substring(1);
        }
        String[] args = url.split("/");
        System.out.println(Arrays.toString(args));
        int size = args.length;
        
        String usage = " See localhost:6060/help for usage";
        String fileError = "Problem writing to file, ensure it is there!";
        String invalidEntry = "Please use a valid number as entry number as shown when listing all entries!";
        boolean error = false;
        //case for different commands        
		switch (args[0]){
		
		case "help":
			response.getWriter().println("<h2>Help page</h2>");
			response.getWriter().println("<p>Usage:</p>");
			response.getWriter().println("<p>Adding an entry: localhost:6060/add/firstname/surname/number/address(optional)</p>");
			response.getWriter().println("<p>Searching entries for surname: localhost:6060/search/surname(case sensitive)</p>");
			response.getWriter().println("<p>Listing all entries: localhost:6060/list</p>");
			response.getWriter().println("<p>For editing an entry and deleting an entry you are required to list all entries first and "
					+ "locate the entry number associated with the entry</p>");
			response.getWriter().println("<p>Editing an entry: localhost:6060/edit/entry-number/firstname/surname/number/address(optional)"
					+ "(if you dont enter adress it will overide previous with nothing)");
			response.getWriter().println("<p>Removing an entry: localhost:6060/remove/entry-number");			
			break;
			
		case "add" : 
			if (size > 5 || size < 4){
				print(response, "Max of 4 additional fields and minimum of 3 for add. "
						+ usage);
											
			} else {
				Entries entry;
				if (size == 4){
					entry = new Entries(args[1], args[2], args[3]);	
				} else {
					entry = new Entries(args[1], args[2], args[3], args[4]);					
				}
				try{
					Database.addEntry(entry);
				}catch(IOException e){
					print(response, fileError);
					error = true;
				}
				if (!error){
					print(response, "Success, added entry to phonebook.");
				}
			}
			break;
			
		case "list" :
			if (!(size == 1)){
				print(response, "0 additional fields required." + usage);
			} else{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				System.setOut(new PrintStream(baos));
				try{
					Database.listAll();
				}catch(IOException e){
					print(response, fileError);
					error = true;
				}
				//return output to default
				Test.resetOut();
				if (!error){
					print(response, baos.toString());
				}
			}
			break;
		
		case "edit" :
			if (size > 6 || size < 5){
				print(response, "Max of 5 additional fields and minimum of 4 for edit. "
						+ usage);
											
			} else {
				int entryno = Integer.parseInt(args[1]);
				String address = "";
				if (size == 6){
					address = args[5];
				}
				try {
					Database.editEntry(entryno, args[2], args[3], args[4], address);
				} catch (IOException e){
					print(response, fileError);
					error = true;
				} catch (Exception e){
					print(response, invalidEntry);	
					error = true;
				}
				if (!error){
					print(response, "Success, edited entry.");
				}
				
				
			}
			
			break;
		
		case "remove" :
			if (!(size == 2)){
				print(response, "1 field required." + usage);
			} else{
				try{
					int entryno = Integer.parseInt(args[1]);					
					Database.removeEntry(entryno);
				} catch (IOException e){
					print(response, fileError);
					error = true;
				} catch (Exception e){
					print(response, invalidEntry);	
					error = true;
				}
				if (!error){
					print(response, "Success, removed entry.");
				}
				
			}
			break;
		
		case "search" :
			if (!(size == 2)){
				print(response, "1 additional fields required." + usage);
			} else{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				System.setOut(new PrintStream(baos));
				try{
					Database.searchEntries(args[1]);
				}catch(IOException e){
					print(response, fileError);
					error = true;
				}
				//return output to default
				Test.resetOut();
				if (!error){
					print(response, baos.toString());
				}
			}
			break;
			
		case "end" :
			System.exit(0);
			break;
			
		default: print(response, usage);
		}
		
		
		
	}
	
	public static void print(HttpServletResponse response, String printstring) throws IOException{
		System.out.println(printstring);
		response.getWriter().println(printstring);
		
	}
}
