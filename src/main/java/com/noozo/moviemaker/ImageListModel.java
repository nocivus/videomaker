package com.noozo.moviemaker;

import com.google.common.collect.Lists;

import javax.swing.*;
import java.util.List;

/**
 * void
 * 02/07/15.
 */
public class ImageListModel extends AbstractListModel<ImageData> {

    private List<ImageData> data = Lists.newArrayList();

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public ImageData getElementAt(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }

    public void add(ImageData image) {

        data.add(image);
        fireContentsChanged(this, 0, data.size());
    }
}
