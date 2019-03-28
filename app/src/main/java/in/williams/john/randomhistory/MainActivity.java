package in.williams.john.randomhistory;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {
    TextView responseText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the text view and button.
        responseText =(TextView)findViewById(R.id.history_fact);
        submitButton =(Button) findViewById(R.id.button_submit);


        // Upon clicking the submit button.
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute the 'doit' class.
                new doit().execute();
            }
        });

    }


    public class doit extends AsyncTask<Void, Void, Void> {
        String words;

        @Override
        protected Void doInBackground(Void... voids) {



            try {

                Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/AD_98").get();

                words = doc.text();

            } catch(Exception e) {e.printStackTrace();}



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            responseText.setText(words);
        }
    }
}
