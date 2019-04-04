package in.williams.john.randomhistory;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Layout features.
    TextView responseText;
    TextView readMoreLinkText;
    EditText inputYear;
    Button submitButton;

    // Strigns to hold years and URLs.
    String yearString;
    String baseURL;
    String reformedYearString;
    String fullURL;

    boolean event;
    boolean bc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the text view and button.
        responseText = (TextView) findViewById(R.id.history_fact);
        readMoreLinkText = (TextView) findViewById(R.id.read_more);
        submitButton =(Button) findViewById(R.id.button_submit);

        // Whatever the user writes.
        inputYear = (EditText) findViewById(R.id.edit_text_year);

        // Default setup for searching.
        bc = false;
        // Default year is 2019.
        yearString = "";
        reformedYearString = "";
        String fullURL = "";

        // Base URL for the wiki page.
        baseURL = "https://en.wikipedia.org/wiki/";

        // Upon clicking the submit button.
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's input as a string.
                yearString = inputYear.getText().toString();

                // Find if BC or AD year.
                bc = checkBCorAD(yearString);

                // Remove all non-digit parts of the string with regex.
                reformedYearString = yearString.replaceAll("\\D+", "");

                // If string is empty after removing digits, toast.
                if (reformedYearString.length() < 1) {
                    Toast.makeText(MainActivity.this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Execute the 'getRandomFact' class.
                    new getRandomFact().execute();
                }

            }
        });

    }


    // Class gets the web page info.
    public class getRandomFact extends AsyncTask<Void, Void, Void> {
        String words = "";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // Make array of strings to hold our facts.
                List<String> factsList = new ArrayList<>();

                // URL determination.
                if (bc) {
                    fullURL = baseURL + reformedYearString + "_BC";
                } else {
                    fullURL = baseURL + "AD_" + reformedYearString;
                }

                // Get the entire web page.
                Document doc = Jsoup.connect(fullURL).get();

                // Get the unordered lists under the div.
                Element div = doc.select("div.mw-parser-output").first();
                Element notext = doc.select("div#noarticletext").first();

                if (notext != null) {
                    Toast.makeText(MainActivity.this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Get all the children of the div.
                    Element element = div.children().first();
                    // Counter to check how many <h2> tags we've seen.
                    int flag = 0;

                    // While we have only seen one <h2> tag, keep looping.
                    while (flag <= 1) {
                        // If an <h2> appears, increment flag counter.
                        if (element.tagName() == "h2" ) {
                            flag++;
                            // Break out if we hit another section.
                            if (flag > 1) {
                                break;
                            }
                        }
                        // Else, it's normal and we continue to find <ul> elements.
                        else {
                            // If we find an <ul>, get all its <li> elements.
                            if (element.tagName().equals("ul") ) {
                                // Get all <li> items.
                                Elements li = element.children();
                                int size = li.size();
                                // Put each <li> text into our factsList array list.
                                for (int i = 0; i < size; i++) {

                                    // Otherwise, proceed as normal.
                                    String fact = li.get(i).text();
                                    // Do not add empty facts.
                                    if (fact.length() > 0) {
                                        factsList.add(fact);
                                    }
                                }
                            }
                        }
                        // Progress to next sibling.
                        element = element.nextElementSibling();
                    } // End looping through all div children.

                    // Print out a random fact based on random function.
                    words = factsList.get(makeRandomNum(factsList.size()));

                    if (words.isEmpty()) {
                        words = "Could not find any information on that year.";
                    }

                } // End valid web page - else.

            }
            // Error reporting.
            catch(Exception e) {e.printStackTrace();
            words = "Could not find any information on that year."; }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Deliver fact to text view.
            responseText.setText(words);

            // Set textview link to wiki page.
            readMoreLinkText.setClickable(true);
            readMoreLinkText.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href=" + fullURL + ">Read More..</a>";
            readMoreLinkText.setText((Html.fromHtml(text)));
        }
    }

    // Generate a random number for fact printing.
    public int makeRandomNum(int maxSize) {
        Random rand = new Random();

        // Make random num.
        int randomNum = rand.nextInt((maxSize - 0));

        return randomNum;
    }

    // Check if the input string has "BC" or "bc" in it.
    public boolean checkBCorAD(String s) {
        if (s.indexOf("bc") != -1 || s.indexOf("BC") != -1 ) {
            return true;
        }

        return false;
    }

}
