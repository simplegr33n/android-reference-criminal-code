package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AdapterHeading extends ArrayAdapter<Section> {


    private Context mContext;
    private ListView listviewSection;
    private WebView webView;


    public AdapterHeading(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_heading, parent, false);
        }

        final Section current = getItem(position);

        LinearLayout headingLayout = (LinearLayout) convertView.findViewById(R.id.headingLayout);
        TextView headingOne = (TextView) convertView.findViewById(R.id.heading);
        TextView sectionOne = (TextView) convertView.findViewById(R.id.section);

        // Get listView and webView from main activity
        listviewSection = (ListView) ((ActivityMain) mContext).findViewById(R.id.listview_section);
        webView = (WebView) ((ActivityMain) mContext).findViewById(R.id.webview);

        // Set section and heading text
        headingOne.setText("" + current.getFulltext());
        sectionOne.setText("" + current.getSection());


        //Change background color based on heading type
        //TODO: make switch
        if (current.getPinpoint().equals("level1")) {

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.setVisibility(View.GONE);
                    listviewSection.setVisibility(View.VISIBLE);
                    // Set section listview on basis of TOC selection
                    listviewSection.setSelection(current.getID() - 1);
                    ActivityMain.partsHideShow();
                }
            });
            headingOne.setBackgroundColor(Color.parseColor("#8C292e34"));
            sectionOne.setBackgroundColor(Color.parseColor("#8C292e34"));
        }

        if (current.getPinpoint().equals("level2")) {


            headingOne.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            sectionOne.setBackgroundColor(Color.parseColor("#00FFFFFF"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    webView.setVisibility(View.GONE);
                    // Set section listview on basis of TOC selection
                    listviewSection.setSelection(current.getID() - 1);
                    listviewSection.setVisibility(View.VISIBLE);
                    ActivityMain.partsHideShow();
                }
            });


        }

        if (current.getPinpoint().equals("level3")) {
            // Set section listview on basis of TOC selection
            listviewSection.setVisibility(View.VISIBLE);

            webView.setVisibility(View.GONE);

            headingOne.setBackgroundColor(Color.parseColor("#12FFFFFF"));
            sectionOne.setBackgroundColor(Color.parseColor("#12FFFFFF"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set section listview on basis of TOC selection
                    listviewSection.setSelection(current.getID() - 1);
                    ActivityMain.partsHideShow();
                }
            });

        }

        if (current.getPinpoint().equals("schedules")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set section listview on basis of TOC selection
                    listviewSection.setVisibility(View.GONE);
                    webView.loadUrl("file:///android_res/raw/schedules.html");
                    webView.setVisibility(View.VISIBLE);
                    ActivityMain.partsHideShow();
                }
            });
        }

        if (current.getPinpoint().equals("forms")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set section listview on basis of TOC selection
                    listviewSection.setVisibility(View.GONE);
                    webView.loadUrl("file:///android_res/raw/forms.html");
                    webView.setVisibility(View.VISIBLE);
                    ActivityMain.partsHideShow();
                }
            });
        }

        if (current.getPinpoint().equals("related_provs")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // Set section listview on basis of TOC selection
                    listviewSection.setVisibility(View.GONE);
                    webView.loadUrl("file:///android_res/raw/related_provs.html");
                    webView.setVisibility(View.VISIBLE);
                    ActivityMain.partsHideShow();
                }
            });
        }

        if (current.getPinpoint().equals("amendments_nif")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set section listview on basis of TOC selection
                    listviewSection.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl("file:///android_res/raw/amendments_nif.html");
                    ActivityMain.partsHideShow();
                }
            });

        }


        return convertView;


    }

}