package com.company.source;

import com.company.exeptions.YmlProvideException;
import org.w3c.dom.Document;


/**
 * Created by user50 on 25.06.2015.
 */
public interface YmlProvider {

    Document get() throws YmlProvideException;
}
