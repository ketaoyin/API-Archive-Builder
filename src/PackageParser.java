import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PackageParser extends Grabber {

	public static HashMap<String, HashMap<String, String>> classesMapBuilder() throws IOException {

		HashMap<String, HashMap<String, String>> classesMap = new HashMap<String, HashMap<String, String>>();

		Scanner file = new Scanner(new FileReader("classesList.txt"));

		while (file.hasNextLine()) {
			String path = file.nextLine();
			if (path.contains("("))
				path = path.substring(0, path.indexOf("(")).trim();
			if (path.contains("<"))
				path = path.substring(0, path.indexOf("<")).trim();
			path = path + ".html";

			int capitalLetter = capitalLetterLocator(path);
			String firstHalf = path.substring(0, capitalLetter).replace(".", "/");
			String secondHalf = path.substring(capitalLetter);
			path = firstHalf + secondHalf;

			File test = new File(path);
			while (!test.exists()) {
				int toReplace = path.indexOf(".");
				path = path.substring(0, toReplace) + "/" + path.substring(toReplace + 1);
				test = new File(path);
			}

			if (!classesMap.containsKey(path)) {
				File classFile = new File(path);
				HashMap classMethods = classPageParser(classFile);

				if (!classMethods.isEmpty()) {
					String key = path.replace("/", ".");
					key = key.substring(0, key.length()-5);
					
					classesMap.put(key, classMethods);
					
//					System.out.println("ClassesMap pairing: " + key + ", " + classMethods);
				}
			}
		}

		return classesMap;
	}

	public static int capitalLetterLocator(String input) {
		int ret = 100;
		for (int i = 65; i <= 90; i++) {
			char test = (char) i;
			if (input.indexOf(test) != -1 && input.indexOf(test) < ret)
				ret = input.indexOf(test);
		}
		if (ret == 100)
			ret = -1;
		return ret;
	}
}

// Stand alone class page parser code

// File input = new File("Object.html");
// Document doc = Jsoup.parse(input, "UTF-8", "");
// Element table = doc.select("table[summary=Method Summary table, listing
// methods, and an explanation]").first();
//
// int i = 0;
// String id = "i" + i;
//
// while (table.getElementById(id) != null) {
// Element code = table.getElementById(id);
// code = code.getElementsByClass("memberNameLink").first();
//
// i += 1;
// id = "i" + i;
//
// String str = code.text();
// System.out.println(id + ": " + str);
// }