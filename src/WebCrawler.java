//WebCrawler
//Created by Benjamin "JSON Survivor" Park
import com.google.gson.*;
import java.util.ArrayList;

public class WebCrawler {
	//Test case builder, We are building the JSON "Internet" in this function
	//This function is more for me so I can learn how JSON is created, and get a better feel for it
	//TODO Build a JsonObject builder that takes string input, GSON builder gets a bit crazy when trying to reformat strings
	public static JsonObject TestCaseBuilder(){
		//creates the JsonObject of the first Test Case
		JsonObject testCase1 = new JsonObject();
		//creates the pages array inside the JSON "Internet"
		JsonArray pages = new JsonArray();
		
		//create the first page
		JsonObject page1 = new JsonObject();
		//add the property address to the page
		page1.addProperty("address", "http://foo.bar.com/p1");
		//creates a array of links
		JsonArray links1 = new JsonArray();
		//adds links to array
		links1.add("http://foo.bar.com/p2");
		links1.add("http://foo.bar.com/p3");
		links1.add("http://foo.bar.com/p4");
		//add link to page 
		page1.add("links", links1);
		
		//add page1 to the page array
		pages.add(page1);
		
		//create the second page
		JsonObject page2 = new JsonObject();
		//add the property address to the page
		page2.addProperty("address", "http://foo.bar.com/p2");
		//creates a array of links
		JsonArray links2 = new JsonArray();
		//adds links to array
		links2.add("http://foo.bar.com/p2");
		links2.add("http://foo.bar.com/p4");
		//add link to page 
		page2.add("links", links2);
		
		//add page2 to the page array
		pages.add(page2);
		
		//create the fourth page
		JsonObject page4 = new JsonObject();
		//add the property address to the page
		page4.addProperty("address", "http://foo.bar.com/p4");
		//creates a array of links
		JsonArray links4 = new JsonArray();
		//adds links to array
		links4.add("http://foo.bar.com/p5");
		links4.add("http://foo.bar.com/p1");
		links4.add("http://foo.bar.com/p6");
		//add link to page 
		page4.add("links", links4);
		
		//add page4 to the page array
		pages.add(page4);
		
		//create the fifth page
		JsonObject page5 = new JsonObject();
		//add the property address to the page
		page5.addProperty("address", "http://foo.bar.com/p5");
		//creates a array of links
		JsonArray links5 = new JsonArray();
		//This page has no links
		//add link to page 
		page5.add("links", links5);
		
		//add page1 to the page array
		pages.add(page5);
		
		//create the sixth page
		JsonObject page6 = new JsonObject();
		//add the property address to the page
		page6.addProperty("address", "http://foo.bar.com/p6");
		//creates a array of links
		JsonArray links6 = new JsonArray();
		//adds links to array
		links6.add("http://foo.bar.com/p7");
		links6.add("http://foo.bar.com/p4");
		links6.add("http://foo.bar.com/p5");
		//add link to page 
		page6.add("links", links6);
		
		//add page1 to the page array
		pages.add(page6);
		
		//add pages array to the JsonObject of TestCase1
		testCase1.add("pages",pages);
	
		
	/*This section is for my personal testing purposes, this prints out the JSON "Internet" in a more readable form
		// create the gson using the GsonBuilder. Set pretty printing on. 
		// Allow serializing null and set all fields to the Upper Camel Case
		//
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        System.out.println(gson.toJson(testCase1));
        
	*/	
		//finally return the testCase so it can be passed to other functions
		return testCase1;
	}
	public static void main(String [] args){
		//This creates a new JsonObject from the TestCaseBuilder
		//This JsonObject will be used in the Web Crawler
		JsonObject testCase = TestCaseBuilder();
		
		//This starts the traversal through the JSON "Internet"
		WebCrawl(testCase);
	}
	
	//This function handles the first page of the JSON "Internet" and calls its helper function
	//to deal with the links on the first page
	public static void WebCrawl(JsonObject testCase){
		//Creating the ArrayLists that will be eventually passed to the console
		ArrayList <String> success = new ArrayList <String>();
		ArrayList <String> skipped = new ArrayList <String>();
		ArrayList <String> error = new ArrayList <String>();
		
		//Creating an JSON array called page from the JsonObject
		JsonArray pageArray = testCase.getAsJsonArray("pages");
		//Grabbing the first page from the JSON array
		JsonObject page = pageArray.get(0).getAsJsonObject();
		//Storing the address of the first page in the JSON array
		String curAddress = page.get("address").getAsString();
		
		//Add current address to success
		success.add(curAddress);
		//Store links as an Json array of links
		JsonArray linkArray = page.get("links").getAsJsonArray();
		//loop through links to first page from 0 to linkArray.length
		for(int i = 0; i < linkArray.size(); i++){
			//Grab link from link Array
			String link = linkArray.get(i).getAsString();
			
			//Run WebCrawlerDeep on each link
			WebCrawlerDeep(pageArray,link,success,skipped,error);
		}
			
		//exit loop
		
		//Print results to console
		System.out.println("Success: " + success);
		System.out.println("Skipped: " + skipped);
		System.out.println("Error: " + error);
		
	}
	
	//This helper function does a bulk of the work
	//It traverses through all the links and adds the links to the proper ArrayList
	public static void WebCrawlerDeep(JsonArray pageArray, String link, ArrayList <String> success,ArrayList <String> skipped,ArrayList <String> error){
		//initialize a boolean checker to false, this is to check to see if the link exists
		boolean errorChecker = false;
		
		//check to see if link is in success
		if(success.contains(link)){
			//if errorChecker == true then current link exists
			errorChecker = true;
			//add link to skipped if it isn't already
			if(!skipped.contains(link)){
				skipped.add(link);
			}
		}else{
			//If the current link hasn't been used
			//loop through each page from 1 to array.length (skip first page)
			for(int i = 1; i < pageArray.size(); i++){
				//get current page
				JsonObject page = pageArray.get(i).getAsJsonObject();
				//get current address
				String curAddress = page.get("address").getAsString();
				
				if(curAddress == link){
					//if link = current address add link to success and set checker to true
					success.add(link);
					errorChecker = true;
					
					//run program on current address links
					//Store links as an Json array of links
					JsonArray linkArray = page.get("links").getAsJsonArray();
					//loop through links to first page from 0 to linkArray.length
					for(int k = 0; k < linkArray.size(); k++){
						//Grab link from link Array
						String newLink = linkArray.get(k).getAsString();
						
						//Run WebCrawlerDeep on each new link
						WebCrawlerDeep(pageArray,newLink,success,skipped,error);
					}
				}
			}
		}
		//if checker == false
		//add link to error if it isn't already
		if(errorChecker==false && !error.contains(link)){
			error.add(link);
		}
		return;
		
	}
	
}
