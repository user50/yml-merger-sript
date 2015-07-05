package com.company.source;

import com.company.exeptions.YmlProvideException;
import com.company.http.HttpService;
import com.company.main.DownloadPriceListRequest;
import com.company.main.FileDownloadAsDOMHandler;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Created by user50 on 25.06.2015.
 */
public class HttpYmlProvider implements YmlProvider{
    HttpService httpService;
    String url;
    String encoding;

    public HttpYmlProvider(HttpService httpService, String url, String encoding) {
        this.httpService = httpService;
        this.url = url;
        this.encoding = encoding;
    }

    @Override
    public Document get() throws YmlProvideException {
        try {
            return httpService.execute(new DownloadPriceListRequest(url), new FileDownloadAsDOMHandler(encoding));
        } catch (Exception e) {
            throw new YmlProvideException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return url;
    }
}
