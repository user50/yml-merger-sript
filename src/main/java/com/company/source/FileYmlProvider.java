package com.company.source;

import com.company.Util;
import com.company.exeptions.YmlProvideException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by user50 on 25.06.2015.
 */
public class FileYmlProvider implements YmlProvider {

    String fileName;
    String encoding;

    public FileYmlProvider(String fileName, String encoding) {
        this.fileName = fileName;
        this.encoding = encoding;
    }

    @Override
    public Document get() throws YmlProvideException {
        try {
            return Util.parseDocument(new FileInputStream(fileName), encoding);
        } catch (Exception e) {
            throw new YmlProvideException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return fileName;
    }
}
