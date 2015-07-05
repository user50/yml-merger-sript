package com.company.stax;

import com.company.http.HttpResponseHandleException;
import com.company.http.HttpResponseHandler;
import com.company.stax.exception.IOResponseHandleException;
import com.company.stax.exception.XmlProcessingException;
import org.apache.http.client.methods.CloseableHttpResponse;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by user50 on 04.07.2015.
 */
public class StaxResponseHandler implements HttpResponseHandler<XMLEventReader> {

    String encoding;

    public StaxResponseHandler(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public XMLEventReader handle(CloseableHttpResponse httpResponse){
        XMLInputFactory ifactory = XMLInputFactory.newFactory();
        ifactory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        try {
            return ifactory.createXMLEventReader(httpResponse.getEntity().getContent(), encoding);
        } catch (XMLStreamException e) {
            throw new XmlProcessingException("Unable to process http response!", e);
        } catch (IOException e) {
            throw new IOResponseHandleException("I/O exception!", e);
        }
    }
}
