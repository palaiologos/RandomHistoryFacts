package in.williams.john.randomhistory;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView responseText;

    EditText inputYear;

    Button submitButton;
    Button eventButton;
    Button birthButton;


    String yearString;
    String baseURL;

    boolean event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the text view and button.
        responseText =(TextView) findViewById(R.id.history_fact);
        submitButton =(Button) findViewById(R.id.button_submit);

        // Toggleable buttons.
        eventButton =(Button) findViewById(R.id.button_event);
        birthButton =(Button) findViewById(R.id.button_birth);

        // Whatever the user writes.
        inputYear = (EditText) findViewById(R.id.edit_text_year);

        // Default setup for searching.
        event = true;
        // Default year is 2019.
        yearString = "2019";
        // Base URL for the wiki page.
        baseURL = "https://en.wikipedia.org/wiki/";




        // Based on what user wants returned.
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute the 'getRandomFact' class.
                event = true;
            }
        });
        // Finds famous births.
        birthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute the 'getRandomFact' class.
                event = false;
            }
        });


        // Upon clicking the submit button.
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's input as a string.
                yearString = inputYear.getText().toString();

                // Execute the 'getRandomFact' class.
                new getRandomFact().execute();
            }
        });

    }


    // Class gets the web page info.
    public class getRandomFact extends AsyncTask<Void, Void, Void> {
        String words = "";

        @Override
        protected Void doInBackground(Void... voids) {

            // Get an event, not a birth.
            if (event) {
                try {
                    // Make array of strings to hold our facts.
                    List<String> factsList = new ArrayList<>();
                    // URL.
                    String fullURL = baseURL + yearString;
                    // Get the entire web page.
                    Document doc = Jsoup.connect(fullURL).get();
                    // Get the unordered lists under the div.
                    Element div = doc.select("div.mw-parser-output").first();
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
                                    factsList.add(li.get(i).text());
                                }
                            }
                        }
                        // Progress to next sibling.
                        element = element.nextElementSibling();
                    } // End looping through all div children.

                    // Print out a random fact based on random function.
                    words = factsList.get(makeRandomNum(factsList.size()));

                } catch(Exception e) {e.printStackTrace();}

            }

            // Otherwise, get a birth.
            else {
                try {
                    Document doc = Jsoup.connect(baseURL + yearString).get();

                    words = doc.text();

                } catch(Exception e) {e.printStackTrace();}

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            responseText.setText(words);
        }
    }

    // Generate a random number for fact printing.
    public int makeRandomNum(int maxSize) {
        Random rand = new Random();

        // Make random num.
        int randomNum = rand.nextInt((maxSize - 0) + 1);

        return randomNum;
    }






}
