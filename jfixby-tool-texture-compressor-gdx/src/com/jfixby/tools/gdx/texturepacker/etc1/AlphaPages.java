package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Map;

public class AlphaPages {

    @Override
    public String toString() {
	return "Pages [pages=" + pages + "]";
    }

    final Map<String, AlphaPage> pages = Collections.newMap();

    public void add(AlphaPage newPage) {
	pages.put(newPage.getName(), newPage);
    }

    public int size() {
	return pages.size();
    }

    public AlphaPage get(int i) {
	return pages.getValueAt(i);
    }

    public AlphaPage findAlphaPage(String name) {
	return pages.get(name);
    }

}
