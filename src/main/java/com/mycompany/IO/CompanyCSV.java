package com.mycompany.IO;

import com.mycompany.CustomExceptions.CustomIOException;
import com.mycompany.Models.Company;

import java.io.*;

/**
 * Handle Main Company data to read and write to file
 */
public class CompanyCSV {

    private Company company;
    private String fileName;

    /**
     * Get the file name and the extension
     * @param fileName name of the file and extension
     */
    public CompanyCSV(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Read the file for Company object
     *
     * @throws CustomIOException if read failed
     */
    public void reader() throws CustomIOException {
        File file = new File("./" + fileName);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("./" + fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tag = line.split(";");
                    company = new Company(tag[0], tag[1], tag[2], tag[3], tag[4], tag[5]);
                }
            } catch (IOException ex) {
                throw new CustomIOException(ex);
            }
        } else {
            company = new Company();
        }
    }

    /**
     * Write to file the Company object
     *
     * @param company Company object
     * @throws CustomIOException if write failed
     */
    public void writer(Company company) throws CustomIOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("./" + fileName, true))) {
            pw.println(company.getName() + ";" + company.getRegisteredSeat() + ";" + company.getRegisteredNumber() + ";" + company.getPhoneNumber() + ";" + company.getEmail() + ";" + company.getVatNumber());
        } catch (IOException ex) {
            throw new CustomIOException(ex);
        }
    }

    /**
     * Get the Company object
     *
     * @return Company object
     */
    public Company getCompany() {
        return this.company;
    }

}
