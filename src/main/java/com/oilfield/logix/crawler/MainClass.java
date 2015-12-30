package com.oilfield.logix.crawler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
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

    public static Logger LOGGER = Logger.getLogger(MainClass.class);

    public static String lastResponse;

    private static final RateLimitedHttpClient httpClient = new RateLimitedHttpClient();
    private static Config config;
    private static List<Well> wells;
    private static String oldWellsFilePath = "wells.csv";
    private static String siteUri;

    public static String[] districts = {"01","02","03","04","05","06","6E","7B","7C","08","8A","09","10"};
    public static String MAX_RECORDS_EXCEEDED_STRING = "records found which exceeds the maximum records allowed. Please refine your search.";
    public static String NO_RECORDS_STRING = "No 'Packet' records found";

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        org.apache.log4j.BasicConfigurator.configure();

        config = new Config(args[0],args[1]);
        siteUri = args[2];

        wells = new ArrayList<>();

        try {
            if(new File(oldWellsFilePath).exists()) {
                populateOldWells();
            }
            populateNewWells();
            writeCSV();
        } catch(Exception e) {
            LOGGER.error(e);
            LOGGER.error("Last response received from server:\n" + lastResponse);
            throw e;
        }

    }


    public static void populateOldWells() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(oldWellsFilePath));
        List<String[]> csvLines = csvReader.readAll();

        for (String[] line : csvLines) {
            if(!line[0].equals("id"))
                continue;

            wells.add(new Well(Integer.valueOf(line[0]), line[1], line[2], line[3], line[4],
                    line[5], line[6], line[7], line[8], line[9], line[10], line[11], line[12],
                    line[13], line[14], line[15], line[16], line[17], line[18], line[19], line[20],
                    line[21], line[22]));
        }
    }

    /**
     * Populates new wells into the list of wells from the list of IDs.  Will update documents on old wells.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
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

            Elements summaryElements = document.body().getElementsByAttributeValue("class", "GroupBox1");
            el = summaryElements.first();
            summaryElements = el.getElementsByTag("strong");
            summaryElements.remove(summaryElements.first());
            Well newWell;

            // If the well is submitted but not completed it will have one fewer element
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
                // The submission date is actually in the approval date section if the well has not been approved yet
                newWell = new Well(Integer.valueOf(summaryElements.get(0).text()), "null", summaryElements.get(
                        2).text(), summaryElements.get(3).text(), summaryElements.get(4).text(), summaryElements.get(5)
                        .text(), summaryElements.get(6).text(), summaryElements.get(7).text(),
                        summaryElements.get(8).text(),
                        summaryElements.get(9).text(), summaryElements.get(10)
                        .text(), summaryElements.get(11).text(), summaryElements.get(12).text(), summaryElements
                        .get(13).text(), summaryElements.get(14).text(), summaryElements.get(15).text(),
                        summaryElements.get(1).text(), summaryElements.get(16).text());
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
                    if(oldWell.getApprovalDate().equals("null") && !newWell.getApprovalDate().equals("null")) {
                        oldWell.setApprovalDate(newWell.getApprovalDate());
                        oldWell.setSubmissionDate(newWell.getSubmissionDate());
                    }
                }
            } else {
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

                wells.add(newWell);
            }

        }
    }

    public static void writeCSV() throws IOException {
        CSVWriter csvWellWriter = new CSVWriter(new FileWriter(new File("newWells.csv")));
        csvWellWriter.writeNext(("id,approvalDate,operaterName,completionType,fieldName,completionDate,leaseName," +
                "filingPurpose,rrcDistrictNo,wellType,rrcGasId,county,wellNumber,drillingPermitNumber,apiNo," +
                "wellBoreProfilesubmissionDate,fieldNumber,w2Date,w15Datel1HeaderDate,directionalSurveyMWDDate,directionSurveyGyroDate").split(","));
        for(Well well : wells) {
            csvWellWriter.writeNext(well.asCsvEntry());
        }
    }

    public static Set<Integer> getIdList()
            throws IOException, ParserConfigurationException, SAXException {

        Set<Integer> ids = new HashSet<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        /**
         * Asks for all the records between two dates but does one day at a time so ass to avoid
         * coming up against the max records limit. Just in case this limit is exceeded just in one
         * day, records are then broken down by date
         */
        for(int i = 0; i < ChronoUnit.DAYS.between(LocalDate.parse(config.getBeginDate()), LocalDate.parse(config.getEndDate())) - 1; i++) {
            String beginDate = dateFormatter.format(LocalDate.parse(config.getBeginDate()).plusDays(i)).replace("-", "/");
            String endDate = dateFormatter.format(LocalDate.parse(config.getBeginDate()).plusDays(i+1)).replace("-", "/");
            String responseString = getIdsResponseString(beginDate, endDate, null);
            
            if(responseString.contains(MAX_RECORDS_EXCEEDED_STRING)) {
                for(String district : districts) {
                    ids.addAll(getIdsFromResponseString(getIdsResponseString(beginDate, endDate, district)));
                }
            } else if(responseString.contains(NO_RECORDS_STRING)) {
                continue;
            } else {
                ids.addAll(getIdsFromResponseString(responseString));
            }
        }
        return ids;
    }

    private static String getIdsResponseString(String beginDate, String endDate, String district) throws IOException {
        URI listingUri = UriBuilder.fromUri(siteUri)
                .path("publicSearchAction.do")
                .queryParam("searchArgs.paramValue", "|0=" + beginDate + "|1=" + endDate + ((district == null) ? "" : ("|2=" + district)))
                .queryParam("pager.pageSize", "1000000")
                .queryParam("formData.methodHndlr.inputValue","search")
                .build();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setURI(listingUri);
        return httpClient.execute(httpGet);
    }

    private static Set<Integer> getIdsFromResponseString(String responseString) {
        Set<Integer> ids = new HashSet<>();

        Document document = Jsoup.parse(responseString);
        Elements elements = document.body().getElementsByAttributeValue("class", "DataGrid");
        Element el = elements.first();
        elements = el.getElementsByTag("a");
        elements.remove(elements.first());
        elements.forEach(element -> {
            try {
                ids.add(Integer.valueOf(element.text().trim()));
            } catch (NumberFormatException e) {
                LOGGER.warn(e);
            }
        });
        return ids;
    }
}
