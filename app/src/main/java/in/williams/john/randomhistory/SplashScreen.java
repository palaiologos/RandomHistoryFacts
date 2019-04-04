package in.williams.john.randomhistory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                // Tries to sleep for 3 seconds.
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Then go to the main activity.
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMain);
                finish();

            }
        };

        // Sleep for 3 secs..
        myThread.start();

    }
}
