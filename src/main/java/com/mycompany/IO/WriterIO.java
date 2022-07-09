package com.mycompany.IO;

import com.mycompany.CustomExceptions.CustomIOException;
import com.mycompany.Models.Company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fill template with constructor parameters and write to file
 */
public class WriterIO {

    private String orderString;
    private String fileName;
    private Company company;

    /**
     * Fill the template with parameters
     *
     * @param template template text
     * @param itemList list of order items
     * @param order String array, 1. element order no., 2. element name, 3. element date, 4. element total price
     * @throws CustomIOException if write failed
     */
    public WriterIO(TemplateIO template, List<String[]> itemList, String[] order) throws CustomIOException {
        String itemListString = formatItemsToString(itemList);
        fileName = order[1].trim() + "#" + order[0];
        getCompanyFromFile();
        StringBuffer stringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile("\\$\\{([A-Z]+)\\}");
        Matcher m = pattern.matcher(template.getTemplate());
        while (m.find()) {
            switch (m.group(1)) {
                case "NAME":
                    m.appendReplacement(stringBuffer, order[1]);
                    break;
                case "DATE":
                    m.appendReplacement(stringBuffer, order[2]);
                    break;
                case "ORDERID":
                    m.appendReplacement(stringBuffer, order[0]);
                    break;
                case "ITEMS":
                    m.appendReplacement(stringBuffer, itemListString);
                    break;
                case "TOTALPRICE":
                    m.appendReplacement(stringBuffer, order[3]);
                    break;
                case "MYCOMPANY":
                    m.appendReplacement(stringBuffer, company.toString());
                    break;
                default:
                    break;
            }
        }
        m.appendTail(stringBuffer);
        orderString = stringBuffer.toString();
    }

    /**
     * Format item list for template
     *
     * @param itemList list of items
     */
    private String formatItemsToString(List<String[]> itemList) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(String.format("%-30s%10s%20s", "Name", "Square meter", "Price(HUF)")).append(System.lineSeparator());
        stringBuffer.append(String.format("%-30s", "").replace("", "*")).append(System.lineSeparator());
        for (String[] item : itemList) {
            stringBuffer.append(String.format("%-30s%10s%20s", item[0], item[1], item[2])).append(System.lineSeparator());
        }
        return stringBuffer.toString().trim();
    }

    /**
     * Write order ot file
     *
     * @throws CustomIOException if write failed
     */
    public void writeToFile(String fileNameAndPath) throws CustomIOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileNameAndPath))) {
            System.out.println(fileNameAndPath);
            pw.println(orderString);
        } catch (IOException ex) {
            throw new CustomIOException(ex);
        }
    }

    private void getCompanyFromFile() throws CustomIOException {
        CompanyCSV csv = new CompanyCSV("company.txt");
        try {
            csv.reader();
            company = csv.getCompany();
        } catch (CustomIOException ex) {
            throw new CustomIOException(ex);
        }
    }

    public String getFileName() {
        return fileName.toLowerCase().replaceAll("\\s+", "") + ".txt";
    }
}
