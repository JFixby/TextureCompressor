package com.jfixby.tools.gdx.texturepacker.etc1;

import java.util.ArrayList;

public class Pages {

    @Override
    public String toString() {
	return "Pages [pages=" + pages + "]";
    }

    final ArrayList<Page> pages = new ArrayList<Page>();

    public void add(Page newPage) {
	pages.add(newPage);
    }

    public int size() {
	return pages.size();
    }

    public Page get(int i) {
	return pages.get(i);
    }

}
