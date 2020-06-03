package lv.neotech.homework.ws.phone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
class WikiPhoneCountryCodeDataSource {

    private static final String WIKI_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";
    private static final String TEXT_OF_NEEDLE_TH_TAG = "Country, Territory or Service";
    private static final int INDEX_OF_COLUMN_WITH_COUNTRY = 0;
    private static final int INDEX_OF_COLUMN_WITH_CODES = 1;


    public Map<String, List<String>> getCodeMap() throws IOException {
        Document wikiPage = loadWikiPage();

        Element phoneCountryCodeTable = getPhoneCountryCodeTable(wikiPage);
        Elements rowsInPhoneCountryCodeTable = getRowsInPhoneCountryCodeTable(phoneCountryCodeTable);
        return extractDataFromRows(rowsInPhoneCountryCodeTable);
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

            String country = extractCountryFromRow(row);
            List<String> codesForCountry = extractCodesFromRow(row);

            putCodesAndCountryIntoMap(codesForCountry, country, countryByCodeMap);
        }

        return countryByCodeMap;
    }

    private String extractCountryFromRow(Element row) {
        Elements colsInRow = row.children();

        return colsInRow.get(INDEX_OF_COLUMN_WITH_COUNTRY).text();
    }

    private List<String> extractCodesFromRow(Element row) {
        Elements colsInRow = row.children();

        Element codeCol = colsInRow.get(INDEX_OF_COLUMN_WITH_CODES);
        Elements codesInsideHyperlinkTags = codeCol.select("a");

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

    private void putCodesAndCountryIntoMap(List<String> codes, String country, Map<String, List<String>> map) {
        codes.forEach(code -> {
            List<String> countriesForCode = map.computeIfAbsent(code, k -> new ArrayList<>());
            countriesForCode.add(country);
        });
    }

}
