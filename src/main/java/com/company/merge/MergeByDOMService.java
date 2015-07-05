package com.company.merge;

import com.company.Util;
import com.company.config.Config;
import com.company.exeptions.YmlProvideException;
import com.company.http.HttpService;
import com.company.main.DownloadPriceListRequest;
import com.company.main.FileDownloadAsDOMHandler;
import com.company.source.YmlProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by lozov on 23.06.15.
 */
public class MergeByDOMService implements MergeService {

    String encoding;

    public MergeByDOMService(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public byte[] merge(Iterable<YmlProvider> ymlProviders) {

        List<Document> documents = new ArrayList<>();

        for (YmlProvider ymlProvider : ymlProviders) {
            try {
                System.out.println("Start downloading "+ymlProvider);

                documents.add(ymlProvider.get());

                System.out.println("done");
            } catch (YmlProvideException e) {
                System.out.println("Unable to download YML from "+ymlProvider.toString() + " due to "+e.getMessage());
            }
        }

        Iterator<Document> iterator = documents.iterator();


        if (!iterator.hasNext()) {
            throw new RuntimeException("Must be specified at least one valid yml price source");
        }

        try {
            Document document = iterator.next();

            int count = 0;
            while (iterator.hasNext()){
                System.out.println("Start merging  file number "+(++count));
                Document nextDocument = iterator.next();

                mergeCategories(document, nextDocument);
                mergeOffers(document, nextDocument);
            }

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            Util.writeDocument(document, byteOutputStream, encoding);

            return byteOutputStream.toByteArray();

        } catch ( TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeCategories(Document toDocument, Document fromDocument) {
        NodeList toCategories = toDocument.getElementsByTagName("category");

        Set<String> categoryIds = new HashSet<>();

        for (int i = 0; i < toCategories.getLength(); i++) {
            Element categoryEl = (Element) toCategories.item(i);
            String categoryId = categoryEl.getAttribute("id");
            categoryIds.add(categoryId);
        }

        Element toCategoriesEl = (Element) toDocument.getElementsByTagName("categories").item(0);

        NodeList fromCategories = fromDocument.getElementsByTagName("category");

        for (int i = 0; i < fromCategories.getLength(); i++) {
            Element categoryEl = (Element) fromCategories.item(i);
            if (categoryIds.contains(categoryEl.getAttribute("id")))
                continue;
            moveNode(categoryEl, toDocument, toCategoriesEl);
        }
    }

    private void mergeOffers(Document toDocument, Document fromDocument) {

        System.out.println("Start merging");
        Element toOffersEl = (Element) toDocument.getElementsByTagName("offers").item(0);
        NodeList fromOffers = fromDocument.getElementsByTagName("offer");

        for (int i = 0; i < fromOffers.getLength(); i++) {
            Node offerNode = fromOffers.item(i);
            moveNode((Element)offerNode, toDocument, toOffersEl);

            if (i % 100 == 0)
                System.out.println("completed "+((double)i/fromOffers.getLength() * 100)+"%");

        }

        System.out.println("merging done");
    }

    private void moveNode(Element element, Document toDocument, Element newParent) {
        Node copiedCategoryNode = element.cloneNode(true);
        toDocument.adoptNode(copiedCategoryNode);
        newParent.appendChild(copiedCategoryNode);
    }
}
