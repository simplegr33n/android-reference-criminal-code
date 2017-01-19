package ca.ggolda.reference_criminal_code;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 01/09/2017.
 */

public class SectionXmlParser {

    private Context mContext;
    private DbHelper dbHelper = DbHelper.getInstance(this.mContext);

    // We don't use namespaces
    private static final String ns = null;

    private List sections;


    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readStatute(parser);
        } finally {
            in.close();
        }
    }

    // For skipping.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // Start reading
    private List readStatute(XmlPullParser parser) throws XmlPullParserException, IOException {

        sections = new ArrayList();

        Log.e("XML", "readStatute");

        parser.require(XmlPullParser.START_TAG, ns, "Statute");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e("XML", "getName: " + name);

            // Starts by looking for the Body tag
            if (name.equals("Body")) {
                Log.e("XML", "Body");
                readBody(parser);
            } else {
                skip(parser);
            }
        }

        Log.e("XML ENDSIZE", "" + sections.size());

        return sections;
    }

    // Parses the contents of the body for Heading and Section
    private void readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Body");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Section")) {

                // Get the section number from the Section Code
                String section = "";
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    section = code[1];

                    Log.e("XML", "sectionTrue : " + section);
                }

                readSection(parser, section);

            } else if (parser.getName().equals("Heading")) {

                Log.e("HEADING", "HEADING!");


                // Get the section number from the Heading Code
                String section = "";
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    section = code[1];

                    Log.e("XML", "heading sectionTrue : " + section);
                }

                readHeading(parser, section);

            } else {
                skip(parser);
            }

        }

    }

    // Parses the contents of a Heading
    private void readHeading(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Heading");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TitleText")) {
                Log.e("XML", "TitleText");

                readTitleText(parser, section);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Section.
    private void readSection(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Section");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("MarginalNote")) {
                Log.e("XML", "MarginalNote");

                readMarginalText(parser, section);

            } else if (parser.getName().equals("Text")) {
                Log.e("XML", "Text");

                readSectionText(parser, section);

            } else if (parser.getName().equals("HistoricalNote")) {
                Log.e("XML", "HistoricalNote");

                readHistoricalNote(parser);

            } else if (parser.getName().equals("Definition")) {
                Log.e("XML", "Definition");

                String subsection = null;

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = code[1];

                    Log.e("XML", "definitionTrue : " + subsection);
                }

                readDefinition(parser, subsection);

            } else if (parser.getName().equals("Subsection")) {
                Log.e("XML", "Subsection");

                String subsection = null;

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = "(" + code[3] + ")";

                    Log.e("XML", "subsectionTrue : " + subsection);
                }

                readSubsection(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Paragraph");

                String subsection = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = "(" + code[3] + ")";
                }

                readParagraph(parser, subsection);


            } else {

                skip(parser);
            }

        }

    }

    // Parses the contents of a Subsection.
    private void readSubsection(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("XML", "readSubsection aftwhile" + parser.getName());

            if (parser.getName().equals("Text")) {

                readSubsectionText(parser, subsection);

            } else if (parser.getName().equals("MarginalNote")) {

                readSubMarginalText(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Paragraph");

                String subparasection = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subparasection = "(" + code[5] + ")";

                }

                readSubsectionParagraph(parser, subparasection);

            } else if (parser.getName().equals("ContinuedSectionSubsection")) {
                Log.e("XML", "ContinuedSectionSubsection");

                readContinuedSubsection(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection.
    private void readContinuedSubsection(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {
                Log.e("XML", "Text");

                readContinuedSubsectionText(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection.
    private void readDefinition(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readDefinition, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("XML", "readDefinition aftwhile" + parser.getName());

            if (parser.getName().equals("MarginalNote")) {

                readDefinitionMarginalNoteText(parser, subsection);

            } else if (parser.getName().equals("Text")) {

                readDefinitionText(parser, subsection);

                //Recursively call to use the readDefinitionText since the formatting is the same
            } else if (parser.getName().equals("ContinuedDefinition")) {

                readDefinition(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Definition Paragraph");

                String definition_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    definition_section = "(" + code[5] + ")";

                }

                readDefinitionParagraph(parser, definition_section);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Paragraph.
    private void readParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readParagraphText(parser, subsection);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subparagraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    subpara_section = "(" + code[5] + ")";

                }

                readSubparagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Paragraph");

                readContinuedParagraph(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Definition Paragraph.
    private void readDefinitionParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readParagraphText(parser, subsection);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subparagraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    subpara_section = "(" + code[7] + ")";


                }

                readSubparagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Paragraph" + parser.getName());

                readContinuedParagraph(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Paragraph.
    private void readSubsectionParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsectionParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Subsection Paragraph Text" + parser.getName());

                readSubsectionParagraphText(parser, subsection);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subsection Subaragraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subpara_section = "(" + code[7] + ")";

                }

                readSubsectionSubParagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Subsection Paragraph");

                readContinuedSubsectionParagraph(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subparagraph.
    private void readSubparagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readSubParagraphText(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Subparagraph.
    private void readSubsectionSubParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readSubsectionSubParagraphText(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // For Subsection Continued Subsection Paragraph values
    private void readContinuedSubsectionParagraph(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Continued Subsection Paragraph Text" + parser.getName());

                readContinuedSubsectionParagraphText(parser, subsection);

            }
        }
    }

    // For the section HistoricalNote value.
    private void readHistoricalNote(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        Log.e("XML", "readHistoricalNote");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("ul")) {
                Log.e("XML", "ul");

                readHistoryListItem(parser);

            } else {
                skip(parser);
            }
        }
    }

    // For History List Item
    private void readHistoryListItem(XmlPullParser parser) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("li")) {

                readHistoryListItemText(parser);

            } else {

                skip(parser);

            }
        }
    }

    // For Continued Paragraph
    private void readContinuedParagraph(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readContinuedParagraphText(parser, subsection);

            }
        }
    }



    /////////////////////////////////////
    //
    //   READ TEXT ENDPOINTS BELOW
    //
    /////////////////////////////////////

    // For the section MarginalNote value.
    private void readMarginalText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(1, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readMarginalText (  1  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection SubParagraph text values.
    private void readContinuedSubsectionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(14, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readContinuedSubsectionText (  14  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For the subsection MarginalNote value.
    private void readSubMarginalText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(5, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubMarginalText (  5  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection text values.
    private void readSubsectionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(3, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubsectionText (  3  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For definition text values.
    private void readDefinitionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(11, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readDefinitionText (  11  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Paragraph text values.
    private void readParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(4, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readParagraphText (  4  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Continued Paragraph text values.
    private void readContinuedParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {


        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(12, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readContinuedParagraphText (  12  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Continued Subsection Paragraph text values.
    private void readContinuedSubsectionParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(13, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add Continued Subsection Paragraph Text (  13  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For SubParagraph text values.
    private void readSubParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(7, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add SubParagraph Text (  7  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection SubParagraph text values.
    private void readSubsectionSubParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(8, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add Subsection SubParagraph Text (  8  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For Subsection Paragraph text values.
    private void readSubsectionParagraphText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(6, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add Subsection Paragraph Text (  6  , " + group  + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For History List Item Text
    private List readHistoryListItemText(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String section = "";
        String group = "";

        if (text != null) {
            Section resultObject = new Section(9, "", "historicalnote", text);
            dbHelper.insertSectionDetail(resultObject);

            Log.e("XML", "db add Historical Note (  9  , " + group  + section + " " + text + " )");
        }

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For Section text values.
    private void readSectionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(2, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add Section Text (  2  , " + group  + section + " " + text + " )");


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For the Definition MarginalNote value.
    private void readDefinitionMarginalNoteText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(10, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add Definition Marginal Note (  10  , " + group  + section + " " + text + " )");


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For the tags TitleText and level values.
    private void readTitleText(XmlPullParser parser, String section) throws IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        String group = "placeholderGroup";

        Section resultObject = new Section(0, group, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add TitleText (  0  , " + group  + section + " " + text + " )");


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }


    }

}