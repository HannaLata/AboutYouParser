package com.hannalata.service;

import com.hannalata.model.Item;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
public class AboutYouProductParserService extends Thread {

    private static final Logger LOGGER = Logger.getLogger(AboutYouProductParserService.class.getName());

    private final List<Item> items;
    private final String url;
    private static final String attribute = "data-test-id";
    public static int counterRequest = 0;

    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
            Thread.sleep(1000);
            ++counterRequest;
            Element productInfo = document.getElementsByAttributeValue(attribute, "BuyBox").first();

            String itemId = extractItemId(document);
            String name = extractName(productInfo);
            String brand = extractBrand(productInfo);
            List<String> colors = extractColor(productInfo);
            BigDecimal price = extractPrice(productInfo);
            if(price == null) {
                price = extractSalePrice(productInfo);
            }

            Item item = new Item(itemId, name, brand, colors, price);

            items.add(item);

        } catch (Exception e) {
            LOGGER.severe(String.format("Item by url %s was not extracted", url));
        }

    }

    private BigDecimal extractPrice(Element productInfo) {
        BigDecimal out = null;
        try {
            String outAsText = productInfo.getElementsByAttributeValue(attribute, "ProductPriceFormattedBasePrice").first().text();
            String outAsTextWithoutEUR = outAsText.replace(" EUR", "");
            String outAsTextReplaced = outAsTextWithoutEUR.replace(",", ".");
            out = new BigDecimal(outAsTextReplaced).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            if(extractSalePrice(productInfo) == null) {
                LOGGER.severe(String.format("Item price by url %s was not extracted", url));
            }
        }
        return out;
    }

    private BigDecimal extractSalePrice(Element productInfo) {
        BigDecimal out = null;
        try {
            String outAsTextSale = productInfo.getElementsByAttributeValue(attribute, "FormattedSalePrice").first().text();
            String outAsTextWithoutEURSale = outAsTextSale.replace(" EUR", "");
            String outAsTextReplacedSale = outAsTextWithoutEURSale.replace(",", ".");
            out = new BigDecimal(outAsTextReplacedSale).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            if(extractPrice(productInfo) == null) {
                LOGGER.severe(String.format("Item sale price by url %s was not extracted", url));
            }
        }
        return out;
    }

    private List<String> extractColor(Element productInfo) {
        List<String> outs = null;
        try {
            outs = productInfo.getElementsByAttributeValue(attribute, "ColorVariantColorInfo").eachText();
        } catch (Exception e) {
            LOGGER.severe(String.format("Item colors by url %s  was not extracted", url));
        }

        return outs;
    }

    private String extractBrand(Element productInfo) {
        String out = "";
        try{
            out = productInfo.getElementsByTag("img").attr("alt");
        } catch (Exception e) {
            LOGGER.severe(String.format("Item brand by url %s was not extracted", url));
        }
        return out;
    }

    private String extractName(Element productInfo) {
        String out = "";
        try{
            out = productInfo.getElementsByAttributeValue(attribute, "ProductName").first().text();
        } catch (Exception e) {
            LOGGER.severe(String.format("Item name by url %s was not extracted", url));
        }
        return out;
    }

    private String extractItemId(Element document) {
        String out = "";
        try {
            out = document.getElementsByAttributeValue(attribute, "ArticleNumber").first().text();
        } catch (Exception e) {
            LOGGER.severe(String.format("Item id by url %s was not extracted", url));
        }
        return out;
    }
}
