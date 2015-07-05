package com.company;

import com.company.config.Config;
import com.company.config.ConfigProvider;
import com.company.http.HttpService;
import com.company.merge.HttpClientProvider;
import com.company.merge.MergeByDOMService;
import com.company.merge.MergeService;
import com.company.source.FileYmlProvider;
import com.company.source.HttpYmlProvider;
import com.company.source.YmlProvider;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user50 on 24.06.2015.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new ConfigProvider().get();

        MergeService mergeService = new MergeByDOMService(config.getEncoding());

        CloseableHttpClient httpClient = new HttpClientProvider(config.getUser(), config.getPsw()).get();
        HttpService httpService = new HttpService(httpClient);

        List<YmlProvider> ymlProviders = new ArrayList<>();
        for (String url : config.getUrls())
            ymlProviders.add(new HttpYmlProvider(httpService, url, config.getEncoding()));

        for (String fileName : config.getFiles())
            ymlProviders.add(new FileYmlProvider(fileName, config.getEncoding()));


        FileOutputStream outputStream = new FileOutputStream(config.getOutputFile());

        outputStream.write(mergeService.merge(ymlProviders));

        outputStream.close();
    }
}
