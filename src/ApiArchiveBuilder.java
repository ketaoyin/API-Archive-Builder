import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class ApiArchiveBuilder extends PackageParser {

	public static void main(String[] args) throws IOException {

		// Tracker information
		int totalClasses = 0;
		int totalMethods = 0;

		// Necessary Hashmaps
		HashMap<String, HashMap<String, String>> classesMap = classesMapBuilder();
		HashMap packageMap = packagesBuilder();
		
		
//		System.out.println("java.lang.Object: " + classesMap.get("java.lang.Object"));
//		Set testz = classesMap.get("java.lang.Object").keySet();
//		System.out.println(testz);
//		
//		Iterator itrz = testz.iterator();
//		while (itrz.hasNext())
//			System.out.println(itrz.next());
//	
//		System.exit(0);
		
		
		// Writes to dtabase file
		try {
			File file = new File("API Database.txt");

			// If file doesn't exist, create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Find all key/value pairs in package map
			Set keys = packageMap.keySet();
			Iterator itr = keys.iterator();

			while (itr.hasNext()) {
				String key = (String) itr.next();
				// System.out.println(key + ": " + packageMap.gets(key));

				// Iterates through all packages to get their respective classes

				// String parsing
				File test = new File(key.replace(".", "/") + "/package-tree.html");
				Document document = Jsoup.parse(test, "UTF-8", "");
				document.outputSettings(new Document.OutputSettings().prettyPrint(false));
				document.select("br").append("\\n");
				document.select("p").prepend("\\n\\n");
				String s = document.html().replaceAll("\\\\n", "\n");
				String a = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

				int begin = 0;
				int end = a.length();
				if (a.contains("Class Hierarchy\n"))
					begin = a.indexOf("Class Hierarchy\n");
				else
					continue;
				if (a.contains("Interface Hierarchy"))
					end = a.indexOf("Interface Hierarchy");
				else
					end = a.indexOf("Skip navigation links", begin);

				String b = a.substring(begin, end);
				String[] parsed = b.split("\n");

				// Loop through parsed string
				// c = class name
				for (int i = 0; i < parsed.length; i++) {
					String c = parsed[i];
					if (c.contains("("))
						c = c.substring(0, c.indexOf("(")).trim();
					if (c.contains("<"))
						c = c.substring(0, c.indexOf("<")).trim();
					if (!c.isEmpty() && !c.equals("Class Hierarchy")) {
						String output = key + "," + c;

						totalClasses += 1;
						
						if (classesMap.containsKey(c)) {
							
							Set<String> methodsKeys = classesMap.get(c).keySet();
							Iterator<String> methodsItr = methodsKeys.iterator();

							while (methodsItr.hasNext()) {
								String method = methodsItr.next();
								String output2 = output + "," + method + "\n";
								totalMethods += 1;

								System.out.println(output2);
								
								bw.write(output2);
							}
						}
					}
				}
			}

			bw.close();

			System.out.println("Done");
			System.out.println("Total packages: " + keys.size());
			System.out.println("Total classes in all packages: " + totalClasses);
			System.out.println("Total methods in all classes: " + totalMethods);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Parses the package page's classes hierarchy tree for classes
		// File test = new File("java/applet/package-tree.html");
		// Document document = Jsoup.parse(test, "UTF-8", "");
		// document.outputSettings(new
		// Document.OutputSettings().prettyPrint(false));
		// document.select("br").append("\\n");
		// document.select("p").prepend("\\n\\n");
		// String s = document.html().replaceAll("\\\\n", "\n");
		// String a = Jsoup.clean(s, "", Whitelist.none(), new
		// Document.OutputSettings().prettyPrint(false));
		//
		// int begin = a.indexOf("Class Hierarchy\n");
		// int end = a.indexOf("Interface Hierarchy");
		//
		// String b = a.substring(begin, end);
		//
		// String[] parsed = b.split("\n");
		//
		// for (int i = 0; i < parsed.length; i++) {
		// String c = parsed[i];
		//
		// if (c.contains("("))
		// c = c.substring(0, c.indexOf("(")).trim();
		//
		// if (c.contains("<"))
		// c = c.substring(0, c.indexOf("<")).trim();
		//
		// if (!c.isEmpty() && !c.equals("Class Hierarchy"))
		// System.out.println(c);
		// }

		// // Serialize class map
		// try
		// {
		// FileOutputStream fos = new FileOutputStream("classesMap.ser");
		// ObjectOutputStream oos = new ObjectOutputStream(fos);
		// oos.writeObject(classesMap);
		// oos.close();
		// fos.close();
		// System.out.println("Serialized HashMap of all classes and their
		// methods is saved in classesMap.ser");
		// }catch(IOException ioe)
		// {
		// ioe.printStackTrace();
		// }

	}

	public static HashMap packagesBuilder() throws FileNotFoundException {

		HashMap packageMap = new HashMap<String, HashMap>();

		Scanner file = new Scanner(new FileReader("packagesList.txt"));
		String line = file.nextLine();
		line = line + ",";

		line.trim();
		String[] packageNames = line.split(",");

		for (int i = 0; i < packageNames.length; i++) {
			String packageName = packageNames[i].trim();
			HashMap classes = new HashMap<String, HashMap>();
			packageMap.put(packageName, classes);

			// System.out.println(packageName + " inserted? " +
			// packageMap.containsKey(packageName));
		}

		return packageMap;
	}
}
