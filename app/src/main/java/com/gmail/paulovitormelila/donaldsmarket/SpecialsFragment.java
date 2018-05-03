package com.gmail.paulovitormelila.donaldsmarket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SpecialsFragment extends Fragment {
    private View mView;
    private static Website mWebsite = new Website();
    private TextView mSpecialsTextView;
    private PhotoView mFlyerPhotoView;
    private static final String WEBSITE_TITLE = "Website Title";
    private static final String SPECIALS_DATE = "Specials Date";
    private static final String IS_FLYER_AVAILABLE = "is Flyer Available?";
    private static final String FLYER_URL = "Flyer URL";

    /** TODO: Create more things for the UI when flyer is not available
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.specials_fragment, container, false);

        instantiateWidgets();

        new WebsiteLoader().load();

        buildText();
        loadFlyer();

        logs();

        return mView;
    }


    private void instantiateWidgets() {
        mSpecialsTextView = mView.findViewById(R.id.specialsTextView);
        mFlyerPhotoView = mView.findViewById(R.id.flyerPhotoView);
    }

    /**
     * Text regarding specials flyer to be displayed depending on the current situation.
     *
     * Is the date ("strong" tag) visible?
     *
     * NO  -> display schedule text.
     * YES ->
     *      Is the flyer also visible?
     *              YES -> display current month message.
     *              NO  -> display next month message.
     */
    private void buildText() {
        mSpecialsTextView.setText(R.string.specials_schedule);

        if (!mWebsite.getSpecialsDate().isEmpty()) {

            if (mWebsite.isFlyerAvailable()) {
                mSpecialsTextView.append(getResources().getString(R.string.specials_current));
                mSpecialsTextView.append(mWebsite.getSpecialsDate());
            } else {
                mSpecialsTextView.append(getResources().getString(R.string.specials_next));
                mSpecialsTextView.append(mWebsite.getSpecialsDate());
            }
        }
    }

    /**
     * Load flyer picture into ImageView.
     */
    private void loadFlyer() {
        if (mWebsite.isFlyerAvailable()) {

            Picasso.get()
                    .load(mWebsite.getFlyerURL())
                    .into(mFlyerPhotoView);
        }

        new PhotoViewAttacher(mFlyerPhotoView).update();
    }

    private void logs() {
        Log.i(WEBSITE_TITLE, mWebsite.getDocument().title());
        Log.i(SPECIALS_DATE, mWebsite.getSpecialsDate());
        Log.i(IS_FLYER_AVAILABLE, mWebsite.isFlyerAvailable() ? "Yes" : "No");
        Log.i(FLYER_URL, mWebsite.getFlyerURL());
    }

    private static class WebsiteLoader extends AsyncTask<Void, Integer, Document> {
        protected Document doInBackground(Void... voids) {
            Document doc = null;

            try {
                doc = Jsoup.connect("https://www.donaldsmarket.com/specials-flyer/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return doc;
        }

        private void load() {
            try {
                mWebsite.setDocument(new WebsiteLoader().execute().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
