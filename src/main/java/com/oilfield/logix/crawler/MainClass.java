package java.com.oilfield.logix.crawler;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;

import com.oilfield.logix.crawler.Config;
import com.oilfield.logix.crawler.Well;
import com.oilfield.logix.crawler.Well.Form;

/**
 * Main class that runs the uber jar
 *
 * @author Jordan Sanderson
 */
public class MainClass {

    private static final CloseableHttpClient httpClient = HttpClientBuilder
            .create()
            .build();
    private static Config config;
    private static List<Well> wells;
    private static String oldFilePath;
    private static String siteUri;

    public static void main(String[] args) throws IOException, InterruptedException {

        config = new Config(args[0],args[1],args[2]);
        oldFilePath = args[3];
        siteUri = args[4];



    }


    public static void populateOldWells() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(oldFilePath));
        List<String[]> csvLines = csvReader.readAll();
        for(String[] line : csvLines) {
            wells.add(new Well(Integer.valueOf(line[0]), line[1], line[2], line[3], line[4],
                    line[5], line[6], line[7], LocalDate.parse(line[8]), LocalDate.parse(line[9]), LocalDate
                            .parse(line[10]), line[11], line[12], line[13], line[14], line[15], line[16]));
        }

        csvReader = new CSVReader(new FileReader(oldFilePath));
        csvLines = csvReader.readAll();

        for(String[] line : csvLines) {
            for(Well well : wells) {
                if(Integer.valueOf(line[0]).equals(well.getId())) {
                    well.getForms().add(new Form(line[1], LocalDate.parse(line[2]), LocalDate.parse(line[3])));
                }
            }
        }
    }

    public static void populateNewWells() throws ParserConfigurationException, SAXException, IOException {
        for(Integer id : getIdList()) {
            URI wellUri = UriBuilder.fromUri(siteUri)
                    .path("publicSearchAction.do")
                    .queryParam("packetSummaryId", id)
                    .queryParam("formData.methodHndlr.inputValue", "loadPacket")
                    .build();
            HttpGet httpGet = new HttpGet();

            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setURI(wellUri);
            HttpResponse response = httpClient.execute(httpGet);
            String responseString  = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            Document document = Jsoup.parse(responseString);
            Elements elements = document.body().getElementsByAttributeValue("class", "GroupBox1");
            Element el = elements.first();
            elements = el.getElementsByTag("strong");
            if (wells.contains(id)) {

            } else {
                wells.add(new Well(Integer.valueOf(elements.get(0).text()), elements.get(1).text(), elements.get(
                        2).text(), elements.get(3).text(), elements.get(4).text(), elements.get(5)
                        .text(), elements.get(6).text(), elements.get(7).text(),
                        LocalDate.parse(elements.get(8).text()),
                        LocalDate.parse(elements.get(9).text()), LocalDate.parse(elements.get(10)
                        .text()), elements.get(11).text(), elements.get(12).text(), elements
                        .get(13).text(), elements.get(14).text(), elements.get(15).text(),
                        elements.get(16).text()));
            }

        }
    }

    public static List<Integer> getIdList()
            throws IOException, ParserConfigurationException, SAXException {

        URI listingUri = UriBuilder.fromUri(siteUri)
                .path("publicSearchAction.do")
                .queryParam("searchArgs.paramValue", "|0=" + config.getBeginDate() + "|1=" + config.getEndDate() + "|2=" + config.getDistrict())
                .queryParam("pager.pageSize", "100000")
                .queryParam("formData.methodHndlr.inputValue","search")
                .build();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("Content-Type","application/x-www-form-urlencoded");
        httpGet.setURI(listingUri);
        HttpResponse response = httpClient.execute(httpGet);
        String responseString  = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        List<Integer> ids = new ArrayList<>();

        Document document = Jsoup.parse(responseString);
        Elements elements = document.body().getElementsByAttributeValue("class", "DataGrid");
        Element el = elements.first();
        elements = el.getElementsByTag("a");
        elements.forEach(element -> ids.add(Integer.valueOf(element.text())));

        return ids;

    }
}
