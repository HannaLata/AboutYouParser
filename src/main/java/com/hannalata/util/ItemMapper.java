package com.hannalata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hannalata.model.Item;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ItemMapper {

    private static final Logger LOGGER = Logger.getLogger(ItemMapper.class.getName());

    private static final String MAIN_DIR = System.getProperty("user.dir");
    private static final String SEP = System.getProperty("file.separator");
    private static final String FILES_DIR = MAIN_DIR + SEP + "files";

    private final static String fileName = FILES_DIR + SEP + "items.json";

    public static void convertObjectToJSON(List<Item> items) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            writer.writeValue(new File(fileName), items);
        } catch (IOException e) {
            LOGGER.severe("JSON file was not created!");
        }
    }

}


