package lv.neotech.homework.ws.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
class WikiPhoneCountryCodeDataSource {

    private static final String WIKI_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";
    private static final String TEXT_OF_NEEDLE_TH_TAG = "Country, Territory or Service";
    private static final int COUNTRY_COLUMN_INDEX = 0;
    private static final int CODES_COLUMN_INDEX = 1;

    private static final Logger LOG = LoggerFactory.getLogger(WikiPhoneCountryCodeDataSource.class);

    Map<String, List<String>> getCodeMap() throws IOException {
        LOG.info("Loading data map from WIKI");
        Document wikiPage = loadWikiPage();

        Element phoneCountryCodeTable = getPhoneCountryCodeTable(wikiPage);
        Elements rowsInPhoneCountryCodeTable = getRowsInPhoneCountryCodeTable(phoneCountryCodeTable);
        Map<String, List<String>> codeMap = extractDataFromRows(rowsInPhoneCountryCodeTable);
        LOG.info("Data map successfully loaded from WIKI");
        return codeMap;
    }

    private Document loadWikiPage() throws IOException {
        return Jsoup.connect(WIKI_URL).get();
    }

    private Element getPhoneCountryCodeTable(Document wikiPage) {
        Element needleThTag = wikiPage
                .select(String.format("th:contains(%s)", TEXT_OF_NEEDLE_TH_TAG))
                .first();

        return needleThTag.parent().parent().parent();
    }

    private Elements getRowsInPhoneCountryCodeTable(Element table) {
        Element tbody = table.children().first();

        return tbody.children();
    }

    private Map<String, List<String>> extractDataFromRows(Elements rows) {
        Map<String, List<String>> countryByCodeMap = new HashMap<>();

        Iterator<Element> rowIter = rows.iterator();
        rowIter.next();//  skip header row

        while (rowIter.hasNext()) {
            Element row = rowIter.next();

            extractData(row, countryByCodeMap);
        }

        return countryByCodeMap;
    }

    private void extractData(Element row, Map<String, List<String>> destination) {
        String country = extractCountryFromRow(row);
        List<String> codes = extractCodesFromRow(row);

        putCodesAndCountry(codes, country, destination);
    }

    private String extractCountryFromRow(Element row) {
        Elements colsInRow = row.children();

        return colsInRow.get(COUNTRY_COLUMN_INDEX).text();
    }

    private List<String> extractCodesFromRow(Element row) {
        Elements cells = row.children();

        Element codeCell = cells.get(CODES_COLUMN_INDEX);
        Elements codesInsideHyperlinkTags = codeCell.select("a");

        return extractCodesFromHyperlinkTags(codesInsideHyperlinkTags);
    }

    private List<String> extractCodesFromHyperlinkTags(Elements codesInsideHyperlinkTags) {
        return codesInsideHyperlinkTags
                .stream()
                .filter(e -> e.text().matches("^[0-9+, ]+$"))
                .map(e -> e.text().split(", "))
                .flatMap(Arrays::stream)
                .map(str -> str.replaceAll("[+ ]", ""))
                .collect(Collectors.toList());
    }

    private void putCodesAndCountry(List<String> codes, String country, Map<String, List<String>> destination) {
        for (String code : codes) {
            List<String> countriesForCode = destination.computeIfAbsent(code, k -> new ArrayList<>());
            countriesForCode.add(country);
        }
    }

}
