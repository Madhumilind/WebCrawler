import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import java.util.Iterator;

public class WebCrawler {

    private HashSet<String> links;

    public WebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                links.add(URL);
                try {
                    //2. Fetch the HTML code
                    Document document = Jsoup.connect(URL).get();
                    //3. Parse the HTML to extract links to other URLs
                    Elements linksOnPage = document.select("a[href]");
                    linksOnPage.forEach(link -> {
                        if (link != null && link.baseUri().equalsIgnoreCase("https://www.prudential.co.uk/") ||
                                link.baseUri().equalsIgnoreCase("http://www.prudential.co.uk/")) {
                            links.add(link.attr("abs:href"));
                        }
                    });
                }
                catch (IllegalArgumentException iae) {
                  // System.out.println(URL);
                }
            } catch (IOException e) {
               // System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    private HashSet<String> getLinks() {
        return links;
    }

    public static void main(String[] args) throws IOException {
        //1. Read URL
        WebCrawler wc = new WebCrawler();
        wc.getPageLinks("http://www.prudential.co.uk/");
        System.out.println("==================================================================================");
        HashSet<String> links = wc.getLinks();
        System.out.println(links);
        Iterator it = links.iterator();
        PrintWriter siteMap = new PrintWriter(new FileWriter("siteMap.txt"));

        while(it.hasNext()) {
            siteMap.println(it.next());
        }
        siteMap.close();
    }

}
