package com.mycompany.IO;

import com.mycompany.CustomExceptions.CustomIOException;

import java.io.*;

/**
 * Load and format template to Order
 */
public class TemplateIO {

    private String template;

    /**
     * Read the text from file
     *
     * @param fileName file name of template
     * @throws CustomIOException if read failed
     */
    public TemplateIO(String fileName) throws CustomIOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new CustomIOException("File " + fileName +"not found");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(System.lineSeparator());
            }
            template = text.toString().trim();
        } catch (FileNotFoundException ex) {
            throw new CustomIOException(ex);
        } catch (IOException ex) {
            throw new CustomIOException(ex);
        }
    }

    /**
     * Get the template
     *
     * @return a template in String format
     */
    public String getTemplate() {
        return this.template;
    }

}
