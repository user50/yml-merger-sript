package com.company.merge;

import com.company.source.YmlProvider;

import java.util.Iterator;

/**
 * Created by lozov on 23.06.15.
 */
public interface MergeService {
    byte[] merge(Iterable<YmlProvider> iterator);
}
