import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Grabber {

	public static HashMap classPageParser(File classFile) throws IOException {
		HashMap<String, String> ret = new HashMap<String, String>();

		Document doc = Jsoup.parse(classFile, "UTF-8", "");
		Element table = doc.select("table[summary=Method Summary table, listing methods, and an explanation]").first();

		int i = 0;
		String id = "i" + i;

		while (table != null && table.getElementById(id) != null) {
			Element code = table.getElementById(id);
			code = code.getElementsByClass("memberNameLink").first();

			i += 1;
			id = "i" + i;

			String str = code.text();

			ret.put(str, str);
		}

		return ret;
	}
}
