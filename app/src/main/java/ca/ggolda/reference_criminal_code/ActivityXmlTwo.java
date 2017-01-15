package ca.ggolda.reference_criminal_code;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by gcgol on 01/10/2017.
 */

public class ActivityXmlTwo extends AppCompatActivity {
    public static final String ANY = "Any";
    private static final String URL = "http://laws-lois.justice.gc.ca/eng/XML/C-46.xml";

    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    private List<Heading> headings;
    private AdapterHeading mAdapterHeading;
    private ListView mListViewHeadings;

    private List<Section> sections;
    private AdapterSection mAdapterSection;
    private ListView mListViewSections;

    private LinearLayout mDummy;
    private LinearLayout mLocal;
    private LinearLayout mOnline;

    private TextView mRedHeading;

    private ImageView mBtnParts;
    private LinearLayout mParts;
    private ImageView mBtnComments;
    private LinearLayout mComments;

    private int commentsVisible = 0;
    private int partsVisible = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRedHeading = (TextView) findViewById(R.id.offence_type);
        mBtnParts = (ImageView) findViewById(R.id.btn_parts);
        mParts = (LinearLayout) findViewById(R.id.parts);
        mDummy = (LinearLayout) findViewById(R.id.dummy);
        mLocal = (LinearLayout) findViewById(R.id.local_html);
        mOnline = (LinearLayout) findViewById(R.id.online);
        mDummy.setVisibility(View.VISIBLE);
        mLocal.setVisibility(View.GONE);
        mOnline.setVisibility(View.GONE);


        // bring comments up or down
        mBtnComments = (ImageView) findViewById(R.id.btn_comments);
        mComments = (LinearLayout) findViewById(R.id.comments);
        mBtnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commentsVisible == 0) {
                    mComments.setVisibility(View.VISIBLE);
                    commentsVisible = 1;
                } else if (commentsVisible == 1) {
                    mComments.setVisibility(View.GONE);
                    commentsVisible = 0;
                }

            }
        });

        loadPage();

    }


    // Implementation of AsyncTask used to download Sections from XML feed.
    private class DownloadSectionXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadSectionXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            mAdapterSection = new AdapterSection(ActivityXmlTwo.this, R.layout.card_heading, sections);
            mListViewSections = (ListView) findViewById(R.id.listview_section);
            mListViewSections.setAdapter(mAdapterSection);

        }
    }

    // Implementation of AsyncTask used to download Headings from XML feed.
    private class DownloadHeadingsXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            mAdapterHeading = new AdapterHeading(ActivityXmlTwo.this, R.layout.card_heading, headings);
            mListViewHeadings = (ListView) findViewById(R.id.listview_heading);
            mListViewHeadings.setAdapter(mAdapterHeading);

            // bring parts up or down
            mBtnParts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (partsVisible == 0) {
                        mParts.setVisibility(View.VISIBLE);
                        mBtnComments.setVisibility(View.INVISIBLE);
                        mRedHeading.setVisibility(View.INVISIBLE);
                        partsVisible = 1;
                    } else if (partsVisible == 1) {
                        mParts.setVisibility(View.GONE);
                        mBtnComments.setVisibility(View.VISIBLE);
                        mRedHeading.setVisibility(View.VISIBLE);
                        partsVisible = 0;
                    }

                }
            });


        }
    }

    // Uses AsyncTask to download the XML feed from laws-lois.justice.gc.ca.
    public void loadPage() {

            new DownloadHeadingsXmlTask().execute(URL);
            new DownloadSectionXmlTask().execute(URL);

    }

    // Uploads XML from online source
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        InputStream stream = null;

        // Instantiate the parser
        XmlHeadingParser xmlParser = new XmlHeadingParser();
        headings = null;

        try {
            // TODO: use downloadUrl as source when updating
            //stream = downloadUrl(urlString);

            stream = getResources().openRawResource(R.raw.c46mod);

            headings = xmlParser.parse(stream);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        if (headings.size() > 0) {
            Log.e("XML sections.get(0)", "" + headings.get(0));
        }

        return ""+headings.size();

    }

    // Loads section from XML
    private String loadSectionXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        InputStream stream = null;

        // Instantiate the parser
        XmlSectionParser xmlParser = new XmlSectionParser();

        try {
            // TODO: use downloadUrl as source when updating
            //stream = downloadUrl(urlString);

            stream = getResources().openRawResource(R.raw.c46mod);

            sections = xmlParser.parse(stream);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }


        if (sections.size() > 0) {
            Log.e("XML sections.get(0)", "" + sections.get(0));
        }

        return ""+sections.size();

    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream. TODO: use for updating
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}