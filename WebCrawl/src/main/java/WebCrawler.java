import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    private HashSet<String> links;

    // int numberOfUrlFetched = 1;

    public WebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        // System.out.println(" No of URLs fetched =" + numberOfUrlFetched++);
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                /*if (links.add(URL)) {
                   // System.out.println(URL);
                }*/
                links.add(URL);
                try {
                    //2. Fetch the HTML code
                    Document document = Jsoup.connect(URL).get();
                    //3. Parse the HTML to extract links to other URLs
                    Elements linksOnPage = document.select("a[href]");
                    // System.out.println(linksOnPage);
                    linksOnPage.forEach(link -> {
                        if (link != null && link.baseUri().equalsIgnoreCase("https://www.prudential.co.uk/") ||
                                link.baseUri().equalsIgnoreCase("http://www.prudential.co.uk/")) {
                            links.add(link.attr("abs:href"));
                        }
                    });

                //5. For each extracted URL... go back to Step 4.
                /*for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth);
                }*/
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

    public static void main(String[] args) {
        //1. Pick a URL from the frontier
        WebCrawler wc = new WebCrawler();
        wc.getPageLinks("http://www.prudential.co.uk/");
        System.out.println("==================================================================================");
        HashSet<String> links = wc.getLinks();
        System.out.println(links);
    }

}
