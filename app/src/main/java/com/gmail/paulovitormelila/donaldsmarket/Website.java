package com.gmail.paulovitormelila.donaldsmarket;

import org.jsoup.nodes.Document;

public class Website {
    private Document mDocument;

    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document document) {
        mDocument = document;
    }

    /**
     *  Getting the URL from the flyer's src attribute so it can be loaded by Picasso.
     * @return url
     */
    public String getFlyerURL() {
        return mDocument.getElementById("post-4052") // finding the correct div
                .select("[src]")     // getting access to the src attribute
                .attr("abs:src"); // returns the value of the src;
    }

    /**
     * Getting the date of when the specials will be available.
     * Conventionally they are wrapped in 'strong' tag.
     * @return date as string
     */
    public String getSpecialsDate() {
        return mDocument.getElementsByTag("strong").first().wholeText();
    }

    /**
     *  Method to check if the month's flyer image is seen on the page.
     * @return true or false.
     */
    public boolean isFlyerAvailable() {
        return !(getFlyerURL().isEmpty());
    }
}