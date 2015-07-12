import com.company.stax.XMLEventReaderProvider;
import com.company.stax.providers.IncludedCategoriesProvider;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Yevhen on 2015-07-12.
 */
public class IncludedCategoriesProviderTest {


    private static final List<String> TEST_FILES = Arrays.asList("testCategories.xml", "testCategories2.xml");

    @Test
    public void testGet() throws Exception {
        List<XMLEventReaderProvider> readerProviders = new ArrayList<>();
        for (String testFile : TEST_FILES) {
            readerProviders.add(new ResourceFileXmlEventReaderProvider(testFile, "utf-8"));
        }

        Set<String> configIds = Sets.newHashSet("35");
        Set<String> actual = new IncludedCategoriesProvider(readerProviders, configIds).get();
        Set<String> ecpected = Sets.newHashSet("48", "46", "36", "37", "38");

        Assert.assertEquals(ecpected, actual);
    }
}
