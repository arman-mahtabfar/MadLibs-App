 package com.example.armanmahtabfar.madlibapp;

 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ProgressBar;
 import android.widget.TextView;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;

 import org.json.JSONObject;


 public class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Madlib:Main";

    /** Request queue for our network requests. */
    //private static RequestQueue requestQueue;

    public String wordToRemove;
    public String wordToAdd;
    public String[] lyricsScript;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button madlib = (Button) findViewById(R.id.madlib);
        madlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextToRemove = (EditText) findViewById(R.id.editText);
                EditText editTextToAdd = (EditText) findViewById(R.id.editText2);
                TextView lyricsDisplay = (TextView) findViewById(R.id.textView4);

                String toRemove = editTextToRemove.getText().toString();
                String toAdd = editTextToAdd.getText().toString();
                String result = toRemove + toAdd;
                lyricsDisplay.setText(result);
            }
        });

        Button getLyrics = (Button) findViewById(R.id.newSong);
        getLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
