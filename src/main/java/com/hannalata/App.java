package com.hannalata;

import com.hannalata.model.Item;
import com.hannalata.service.AboutYouNavigationParserService;
import com.hannalata.service.AboutYouProductParserService;
import com.hannalata.util.ItemMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class App {

    public static final Logger LOGGER = Logger.getLogger(App.class.getName());


    public static void main( String[] args ) {

        List<Item> items = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();
        String searchUrl = "https://www.aboutyou.de/c/maenner/bekleidung-20290";

        LOGGER.info("Parser started!");

        AboutYouNavigationParserService aboutYouNavigationParserService = new AboutYouNavigationParserService(items, searchUrl, threads);
        threads.add(aboutYouNavigationParserService);
        aboutYouNavigationParserService.start();

        boolean threadsFinished;
        do {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadsFinished = checkThreads(threads);
        } while  (threadsFinished);

        LOGGER.info("Parser finished! " + items.size() + " items were extracted!");

        ItemMapper.convertObjectToJSON(items);

    }

    private static boolean checkThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            if (thread.isAlive() || thread.getState().equals(Thread.State.NEW)) {
                return true;
            }
        }
        return false;
    }
}
