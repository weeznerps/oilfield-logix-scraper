package com.oilfield.logix.crawler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.oilfield.logix.crawler.Well.Forms;
import com.oilfield.logix.crawler.httpclient.RateLimitedHttpClient;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Main class that runs the uber jar
 *
 * @author Jordan Sanderson
 */
public class MainClass {

    private static final RateLimitedHttpClient httpClient = new RateLimitedHttpClient();
    private static Config config;
    private static List<Well> wells;
    private static String oldWellsFilePath;
    private static String siteUri;

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        config = new Config(args[0],args[1],args[2]);
        oldWellsFilePath = args[3];
        siteUri = args[4];

        wells = new ArrayList<>();
        populateNewWells();
        writeCSV();
    }


    public static void populateOldWells() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(oldWellsFilePath));
        List<String[]> csvLines = csvReader.readAll();

        for (String[] line : csvLines) {
            wells.add(new Well(Integer.valueOf(line[0]), line[1], line[2], line[3], line[4],
                    line[5], line[6], line[7], line[8], line[9], line[10], line[11], line[12],
                    line[13], line[14], line[15], line[16], line[17], line[18], line[19], line[20],
                    line[21], line[22]));
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
            String responseString = httpClient.execute(httpGet);
            Document document = Jsoup.parse(responseString);

            Elements formElements = document.body().getElementsByAttributeValue("class", "DataGrid");
            Element el = formElements.first();

            formElements = el.getElementsByTag("tr");
            List<Forms> forms = new ArrayList<>();
            for(Element e : formElements) {
                Elements elements1 = e.getElementsByTag("div");
                if(!elements1.isEmpty() && Forms.fromString(elements1.get(0).text()) != null) {
                    forms.add(Forms.fromString(elements1.get(0).text()));
                }
            }

            Well oldWell = null;
            for(Well well : wells) {
                if(well.getId() == id) {
                    oldWell = well;
                }
            }

            if (oldWell != null) {
                for (Forms newForm : forms) {
                    switch(newForm) {
                        case W2:
                            if(oldWell.getW2Date().equals("null")) {
                                oldWell.setW2Date(LocalDate.now().toString());
                            }
                        case G2:
                            if(oldWell.getW2Date().equals("null")) {
                                oldWell.setW2Date(LocalDate.now().toString());
                            }
                        case W15:
                            if(oldWell.getW2Date().equals("null")) {
                                oldWell.setW15Date(LocalDate.now().toString());
                            }
                        case L1HEADER:
                            if(oldWell.getL1HeaderDate().equals("null")) {
                                oldWell.setL1HeaderDate(LocalDate.now().toString());
                            }
                        case DIRECTIONAL_SURVEY_MWD:
                            if(oldWell.getDirectionalSurveyMWDDate().equals("null")) {
                                oldWell.setDirectionalSurveyMWDDate(LocalDate.now().toString());
                            }
                        case DIRECTIONAL_SURVEY_GYRO:
                            if(oldWell.getDirectionSurveyGyroDate().equals("null")) {
                                oldWell.setDirectionSurveyGyroDate(LocalDate.now().toString());
                            }
                    }
                }
            } else {
                Elements summaryElements = document.body().getElementsByAttributeValue("class", "GroupBox1");
                el = summaryElements.first();
                summaryElements = el.getElementsByTag("strong");
                summaryElements.remove(summaryElements.first());
                Well newWell = null;
                if(summaryElements.size() == 18) {
                    newWell = new Well(Integer.valueOf(summaryElements.get(0).text()), summaryElements.get(1).text(), summaryElements.get(
                            2).text(), summaryElements.get(3).text(), summaryElements.get(4).text(), summaryElements.get(5)
                            .text(), summaryElements.get(6).text(), summaryElements.get(7).text(),
                            summaryElements.get(8).text(),
                            summaryElements.get(9).text(), summaryElements.get(10)
                            .text(), summaryElements.get(11).text(), summaryElements.get(12).text(), summaryElements
                            .get(13).text(), summaryElements.get(14).text(), summaryElements.get(15).text(),
                            summaryElements.get(16).text(), summaryElements.get(17).text());
                } else {
                    newWell = new Well(Integer.valueOf(summaryElements.get(0).text()), summaryElements.get(1).text(), summaryElements.get(
                            2).text(), summaryElements.get(3).text(), summaryElements.get(4).text(), summaryElements.get(5)
                            .text(), summaryElements.get(6).text(), summaryElements.get(7).text(),
                            summaryElements.get(8).text(),
                            summaryElements.get(9).text(), summaryElements.get(10)
                            .text(), summaryElements.get(11).text(), summaryElements.get(12).text(), summaryElements
                            .get(13).text(), summaryElements.get(14).text(), summaryElements.get(15).text(),
                            "null", summaryElements.get(16).text());
                }

                for (Forms newForm : forms) {
                    switch(newForm) {
                        case W2:
                            newWell.setW2Date(LocalDate.now().toString());
                        case G2:
                            newWell.setW2Date(LocalDate.now().toString());
                        case W15:
                            newWell.setW15Date(LocalDate.now().toString());
                        case L1HEADER:
                            newWell.setL1HeaderDate(LocalDate.now().toString());
                        case DIRECTIONAL_SURVEY_MWD:
                            newWell.setDirectionalSurveyMWDDate(LocalDate.now().toString());
                        case DIRECTIONAL_SURVEY_GYRO:
                            newWell.setDirectionSurveyGyroDate(LocalDate.now().toString());
                    }
                }

                for(String s : newWell.asCsvEntry()) {
                    System.out.print(s + ",");
                }
                System.out.println();

                wells.add(newWell);
            }

        }
    }

    public static void writeCSV() throws IOException {
        CSVWriter csvWellWriter = new CSVWriter(new FileWriter(new File("newWells.csv")));
        csvWellWriter.writeNext("id,approvalDate,operaterName,completionType,fieldName,completionDate,leaseName,filingPurpose,rrcDistrictNo,wellType,rrcGasId,county,wellNumber,drillingPermitNumber,apiNo,wellBoreProfilesubmissionDate,fieldNumber,w2Date,w15Datel1HeaderDate,directionalSurveyMWDDate,directionSurveyGyroDate".split(","));
        for(Well well : wells) {
            csvWellWriter.writeNext(well.asCsvEntry());
        }
    }

    public static List<Integer> getIdList()
            throws IOException, ParserConfigurationException, SAXException {

        URI listingUri = UriBuilder.fromUri(siteUri)
                .path("publicSearchAction.do")
                .queryParam("searchArgs.paramValue", "|0=" + config.getBeginDate() + "|1=" + config.getEndDate() + "|2=" + config.getDistrict())
                .queryParam("pager.pageSize", "1000000")
                .queryParam("formData.methodHndlr.inputValue","search")
                .build();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("Content-Type","application/x-www-form-urlencoded");
        httpGet.setURI(listingUri);
        String responseString = httpClient.execute(httpGet);

        List<Integer> ids = new ArrayList<>();

        Document document = Jsoup.parse(responseString);
        Elements elements = document.body().getElementsByAttributeValue("class", "DataGrid");
        Element el = elements.first();
        elements = el.getElementsByTag("a");
        elements.forEach(element -> {
            try {
                ids.add(Integer.valueOf(element.text().trim()));
            } catch (NumberFormatException e) {

            }
        });

        return ids;

    }
}
