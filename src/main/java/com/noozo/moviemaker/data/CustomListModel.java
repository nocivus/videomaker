package com.noozo.moviemaker.data;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

import com.google.common.collect.Lists;

/**
 * void
 * 02/07/15.
 */
public class CustomListModel<T> extends AbstractListModel<T> {

    private List<T> data = Lists.newArrayList();

    @Override
    public int getSize() {
    	
        return data.size();
    }

    @Override
    public T getElementAt(int index) {
    	
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }

    public void add(T image) {

        data.add(image);
        fireContentsChanged(this, 0, data.size());
    }

	public List<T> getData() {
		
		return data;
	}
	
	public void moveUp(T item) {
		
		// If anything is null or the item is already at top, bail
    	if (item == null || data == null || item.equals(data.get(0))) {
    		return;
    	}
    	
    	// Index
    	int index = data.indexOf(item);
    	
    	// Swap them
    	Collections.swap(data, index, index-1);
    }
    
	public void moveDown(T item) {
		
		// If anything is null or the item is already at bottom, bail
    	if (item == null || data == null || item.equals(data.get(data.size()-1))) {
    		return;
    	}
    	
    	// Index
    	int index = data.indexOf(item);
    	
    	// Swap them
    	Collections.swap(data, index, index+1);
    }

	public void remove(T item) {
		
		if (item != null) {
			data.remove(item);
			fireContentsChanged(this, 0, data.size());
		}		
	}
}
