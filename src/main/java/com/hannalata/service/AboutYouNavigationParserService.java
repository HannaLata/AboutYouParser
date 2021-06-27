package com.hannalata.service;

import com.hannalata.model.Item;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class AboutYouNavigationParserService extends Thread {

    private static final String BASE_URL = "https://www.aboutyou.de";
    private static final Logger LOGGER = Logger.getLogger(AboutYouNavigationParserService.class.getName());
    private final List<Item> items;
    private final String url;
    private final List<Thread> threads;
    public static int counterRequest = 0;

    public AboutYouNavigationParserService(List<Item> items, String url, List<Thread> threads) {
        this.items = items;
        this.url = url;
        this.threads = threads;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
            ++counterRequest;
            Thread.sleep(1000);
            Elements productElements = document.getElementsByAttributeValue("data-test-id", "ProductTile");
            Set<String> itemLinks = new HashSet<>();
            productElements.forEach(it -> itemLinks.add(it.attr("href")));

            for (String link: itemLinks) {
                if(link != null) {
                    AboutYouProductParserService aboutYouProductParserService = new AboutYouProductParserService(items, BASE_URL + link);
                    threads.add(aboutYouProductParserService);
                    aboutYouProductParserService.start();
                }
            }

        } catch (IOException e) {
            LOGGER.severe(String.format("Products by url %s was not extracted", url));
        }




    }
}
