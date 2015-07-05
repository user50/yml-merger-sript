package com.company.main;

import com.company.Util;
import com.company.http.HttpResponseHandler;
import com.company.yml.YmlCatalog;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by user50 on 21.06.2015.
 */
public class FileDownloadHandler implements HttpResponseHandler<YmlCatalog> {

    String encoding;

    public FileDownloadHandler(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public YmlCatalog handle(CloseableHttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();

        try {
            return Util.unmarshal(entity.getContent(), YmlCatalog.class, encoding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
