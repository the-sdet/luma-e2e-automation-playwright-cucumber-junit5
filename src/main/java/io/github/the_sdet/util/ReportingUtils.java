package io.github.the_sdet.util;

import com.google.common.io.Files;
import io.github.the_sdet.logger.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import static io.github.the_sdet.globalconstants.Constants.OUTPUT_DIR;
import static io.github.the_sdet.globalconstants.Constants.REPORT_TEMPLATE;
import static io.github.the_sdet.util.ConfigReader.getPropertyValue;

public class ReportingUtils {
    private static String reportTemplate;

    public static String parseDataForReport(Map<String, List<Integer>> data) {
        StringBuilder results = new StringBuilder();
        String htmlCode;
        try {
            reportTemplate = Files.asCharSource(new File(REPORT_TEMPLATE), StandardCharsets.UTF_8).read();
            String trString = "<tr>\n" +
                    "                <td class=\"first-col\">$featureName</td>\n" +
                    "                <td class=\"cred\">$testCredentials</td>\n" +
                    "                <td class=\"$passClass\">$passCount</td>\n" +
                    "                <td class=\"$failClass\">$failCount</td>\n" +
                    "                <td>$totalCount</td>\n" +
                    "                <td class=\"$statusClass\">$statusOfTest</td>\n" +
                    "                <td class=\"last-col\">$passPercent</td>\n" +
                    "            </tr>";
            String finalTrString = "<tr class=\"total\">\n" +
                    "                <td class=\"first-col-heading\"><strong>Overall Result:</strong></td>\n" +
                    "                <td></td>\n" +
                    "                <td class=\"pass_total\"><strong>$overallPassCount</strong></td>\n" +
                    "                <td class=\"fail_total\"><strong>$overallFailCount</strong></td>\n" +
                    "                <td class=\"total_count\"><strong>$overallCount</strong></td>\n" +
                    "                <td></td>\n" +
                    "                <td class=\"last-col\"><strong>$overallPassPercent</strong></td>\n" +
                    "            </tr>";
            int overAllTotalCount = 0;
            int overAllPassCount = 0;
            int overAllFailCount = 0;
            for (Map.Entry<String, List<Integer>> item : data.entrySet()) {
                String featureName = item.getKey();
                int passCount = item.getValue().get(0);
                int failCount = item.getValue().get(1);
                int totalCount = passCount + failCount;
                double passPercent = ((double) passCount / (double) totalCount) * 100;
                String passPercentage = new Formatter().format("%.2f", passPercent) + "%";
                String replace = trString
                        .replace("$featureName", featureName)
                        .replace("$testCredentials", "Test User" + "<br>" + "Test Password")
                        .replace("$passCount", String.valueOf(passCount))
                        .replace("$failCount", String.valueOf(failCount))
                        .replace("$passClass", "pass");
                if (failCount == 0) {
                    results.append(replace
                            .replace("$failClass", "fail-empty")
                            .replace("$totalCount", String.valueOf(totalCount))
                            .replace("$statusOfTest", "Passed")
                            .replace("$statusClass", "pass")
                            .replace("$passPercent", passPercentage));
                } else {
                    results.append(replace
                            .replace("$failClass", "fail")
                            .replace("$totalCount", String.valueOf(totalCount))
                            .replace("$statusOfTest", "Failed")
                            .replace("$statusClass", "fail")
                            .replace("$passPercent", passPercentage));
                }
                overAllTotalCount += totalCount;
                overAllPassCount += passCount;
                overAllFailCount += failCount;
            }
            double passPercent = ((double) overAllPassCount / (double) overAllTotalCount) * 100;
            Log.info("Pass Percent: " + passPercent);
            String passPercentage = new Formatter().format("%.2f", passPercent) + "%";

            results.append(finalTrString.replace("$overallPassCount", String.valueOf(overAllPassCount))
                    .replace("$overallFailCount", String.valueOf(overAllFailCount))
                    .replace("$overallCount", String.valueOf(overAllTotalCount))
                    .replace("$overallPassPercent", passPercentage));

        } catch (IOException e) {
            Log.error("Report Template is NOT available. Please check the File or Filepath...", e);
        }
        htmlCode = reportTemplate.replace("InsertTestResultsHere", results)
                .replace("$reportName", getPropertyValue("report.name"))
                .replace("$envURL", getPropertyValue(getPropertyValue("active.env").toLowerCase().trim() + ".url"))
                .replace("$browser", toProperCase(getPropertyValue("browser")))
                .replace("$dateTime", getCurrentDateAndTime());
        File newHtmlFile = new File(OUTPUT_DIR + "AutomationReport.html");
        try {
            FileUtils.writeStringToFile(newHtmlFile, htmlCode, Charset.defaultCharset());
        } catch (IOException e) {
            Log.error("Something went wrong... Unable to write to report...", e);
        }
        return htmlCode;
    }

    /**
     * This method converts a String to Proper Case, i.e., first letter of word should be in uppercase and other letters should be in lowercase
     * Applies to both words and sentences.
     *
     * @param str - String to format
     * @return formatted String
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static String toProperCase(String str) {
        if (str.contains("\\s")) {
            String[] parts = str.split("\\s");
            StringBuilder strToReturn = new StringBuilder();
            for (String s : parts) {
                String part1 = s.substring(0, 1);
                String part2 = s.substring(1);
                strToReturn.append(part1.toUpperCase()).append(part2.toLowerCase());
            }
            return strToReturn.toString();
        } else {
            String part1 = str.substring(0, 1);
            String part2 = str.substring(1);
            return part1.toUpperCase() + part2.toLowerCase();
        }
    }

    /**
     * This method returns current timestamp
     *
     * @return String - Timestamp
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static String getCurrentDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm:ss zzz");
        Date date = new Date();
        return formatter.format(date);
    }
}