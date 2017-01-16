package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterSection extends ArrayAdapter<Section> {


    private Context mContext;


    public AdapterSection(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_section, parent, false);
        }

        // Heading text/number
        TextView headingText = (TextView) convertView.findViewById(R.id.heading_text);
        TextView headingNumber = (TextView) convertView.findViewById(R.id.heading_number);

        // Subsection text/number
        TextView subtext = (TextView) convertView.findViewById(R.id.sub_text);
        TextView subnumber = (TextView) convertView.findViewById(R.id.sub_number);

        // Paragraph text/number
        TextView paratext = (TextView) convertView.findViewById(R.id.para_text);
        TextView paranumber = (TextView) convertView.findViewById(R.id.para_number);

        // Paragraph text/number
        TextView subsectionParatext = (TextView) convertView.findViewById(R.id.subsection_para_text);
        TextView subsectionParanumber = (TextView) convertView.findViewById(R.id.subsection_para_number);


        // Section text/number
        TextView section = (TextView) convertView.findViewById(R.id.section);
        TextView text = (TextView) convertView.findViewById(R.id.text_section);

        // MarginalNote text/number
        TextView marginalNote = (TextView) convertView.findViewById(R.id.marginal_note);
        TextView marginalNumber = (TextView) convertView.findViewById(R.id.marginal_number);

        // SubMarginalNote text/number
        TextView subMarginalNote = (TextView) convertView.findViewById(R.id.submarginal_note);
        TextView subMarginalNumber = (TextView) convertView.findViewById(R.id.submarginal_number);

        // Subparagraph text/number
        TextView subParaText = (TextView) convertView.findViewById(R.id.subpara_text);
        TextView subParaNumber = (TextView) convertView.findViewById(R.id.subpara_number);

        // Subsection Subparagraph text/number
        TextView subsectionSubParaText = (TextView) convertView.findViewById(R.id.subsection_subpara_text);
        TextView subsectionSubParaNumber = (TextView) convertView.findViewById(R.id.subsection_subpara_number);


        LinearLayout subsectionParagraphLayout = (LinearLayout) convertView.findViewById(R.id.subsection_paragraph_layout);
        LinearLayout subMarginalLayout = (LinearLayout) convertView.findViewById(R.id.submarginal_layout);
        LinearLayout marginalLayout = (LinearLayout) convertView.findViewById(R.id.marginal_layout);
        LinearLayout sectionLayout = (LinearLayout) convertView.findViewById(R.id.section_layout);
        LinearLayout subSectionLayout = (LinearLayout) convertView.findViewById(R.id.subsection_layout);
        LinearLayout paragraphLayout = (LinearLayout) convertView.findViewById(R.id.paragraph_layout);
        LinearLayout headingLayout = (LinearLayout) convertView.findViewById(R.id.heading_layout);
        LinearLayout subparagraphLayout = (LinearLayout) convertView.findViewById(R.id.subparagraph_layout);
        LinearLayout subsectionSubparagraphLayout = (LinearLayout) convertView.findViewById(R.id.subsection_subparagraph_layout);

        TextView historicalNote = (TextView) convertView.findViewById(R.id.historical_note);


        final Section current = getItem(position);


        // Section Heading
        if (current.getType() == 0) {
            marginalLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.VISIBLE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            headingText.setText("" + current.getSectionText());
            headingNumber.setText("" + current.getSection());

            // Section MarginalNote
        } else if (current.getType() == 1) {
            marginalNote.setText("" + current.getSectionText());
            marginalNumber.setText("" + current.getSection());

            marginalLayout.setVisibility(View.VISIBLE);
            subMarginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            // Section Text
        } else if (current.getType() == 2) {
            text.setText("" + current.getSectionText());

            headingLayout.setVisibility(View.GONE);
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.VISIBLE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            // Section Subsection Text
        } else if (current.getType() == 3) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.VISIBLE);
            historicalNote.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            subtext.setText("" + current.getSectionText());
            subnumber.setText("" + current.getSection());

            // Section Paragraph
        } else if (current.getType() == 4) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.VISIBLE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            paratext.setText("" + current.getSectionText());
            paranumber.setText("" + current.getSection());

            // Subsection MarginalNote
        } else if (current.getType() == 5) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.VISIBLE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            subMarginalNote.setText("" + current.getSectionText());
            subMarginalNumber.setText("" + current.getSection());

            // Subsection paragraph
        } else if (current.getType() == 6) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.VISIBLE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            subsectionParatext.setText("" + current.getSectionText());
            subsectionParanumber.setText("" + current.getSection());

            // Subsection Paragraph
        } else if (current.getType() == 7) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.VISIBLE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);

            subParaText.setText("" + current.getSectionText());
            subParaNumber.setText("" + current.getSection());

            // Subsection subParagraph
        } else if (current.getType() == 8) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.VISIBLE);

            subsectionSubParaText.setText("" + current.getSectionText());
            subsectionSubParaNumber.setText("" + current.getSection());

            // HistoricalNote
        } else if (current.getType() == 9) {
            historicalNote.setText("" + current.getSectionText());

            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.VISIBLE);
            paragraphLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);
            subparagraphLayout.setVisibility(View.GONE);
            subsectionSubparagraphLayout.setVisibility(View.GONE);
        } else {

        }

        section.setText("" + current.getSection());

        //TODO: open corresponding part
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}