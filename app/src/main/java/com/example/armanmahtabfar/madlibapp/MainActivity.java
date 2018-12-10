 package com.example.armanmahtabfar.madlibapp;

 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.content.Intent;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;

 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.ArrayList;
 import java.util.Arrays;

 public class MainActivity extends AppCompatActivity {

     /** The quote we will be altering */
     private String quote ="placeholder";

     /** Quote parsed into words */
     private String[] parsed;

     /** All the viable words for the MadLib */
     private ArrayList<String> allWords;

     /**
      * This is the jsonText as a string.
      */
     private String jsonText;

     /**
      * this is the partofspeech that we are giving the user.
      */
     private String partOfSpeech;

     /** Default loggin tag for messages from the main activity. */
     private static final String TAG = "Madlib:Main";

     /** Request queue for our network requests. */
     private static RequestQueue requestQueue;

    public void help(android.view.View v){
        startActivity(new Intent(MainActivity.this, Help.class));
    }

    public void modifyQuote(String s) {
        this.quote = s;
        parseQuote(this.quote);
    }

    public void parseQuote(final String s) {
        this.parsed = s.split("([.,!?:;'\"-]|\\s)+");
        allWords = new ArrayList<String>(Arrays.asList(parsed));
       for (int i = allWords.size() - 1; i >= 0; i--) {
            if (allWords.get(i).length() <= 3) {
                allWords.remove(i);
            }
        }
    }
     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

         final EditText editTextToRemove = (EditText) findViewById(R.id.editText);
         final EditText editTextToAdd = (EditText) findViewById(R.id.editText2);
         final TextView lyricsDisplay = (TextView) findViewById(R.id.textView4);

         //public String getPOS(String s) {
         //Call API on s to get the JSON Object
         //Go through the JSON Object to find the POS
         //Store the POS in a string
         //Return that string
         //}

        // This button creates
        Button APICall = findViewById(R.id.newSong);
        APICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Start API button clicked");
                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            "https://talaikis.com/api/quotes/random/",
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    try {
                                        lyricsDisplay.setText(response.get("quote").toString());
                                        modifyQuote(response.get("quote").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //this is the button that will madlib the lyrics and display both the lyrics
        Button madlib = (Button) findViewById(R.id.madlib);
        madlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toRemove = editTextToRemove.getText().toString();
                String toAdd = editTextToAdd.getText().toString();
                String quote = lyricsDisplay.getText().toString();
                String result = toRemove + toAdd + quote;
                lyricsDisplay.setText(result);
            }
        });
     }

     /**
      * takes the jsontext, converts it to a string, and then returns the first Part of Speech
      * within the definitions at the jsonText.
      * @param jsonText
      * @return
      */
     private String returnPOSfromJson(String jsonText) {
         //takes the Json, trims it, splits it with the quotation marks, and finds the first POS.
         // it then returns the POS.

         jsonText.trim();
         String[] jsonTextArray = jsonText.split("([.,!?:;'\"-]|\\s)+");
         for(int i = 0; i < jsonTextArray.length; i++) {
             if (jsonTextArray[i] == "noun") {
                 return "noun";
             }
             if (jsonTextArray[i] == "adjective") {
                 return "adjective";
             }
             if (jsonTextArray[i] == "pronoun") {
                 return "pronoun";
             }
             if (jsonTextArray[i] == "verb") {
                 return "verb";
             }
             if (jsonTextArray[i] == "adverb") {
                 return "adverb";
             }
             if (jsonTextArray[i] == "preposition") {
                 return "preposition";
             }
             if (jsonTextArray[i] == "conjunction") {
                 return "conjunction";
             }
             if (jsonTextArray[i] == "interjection") {
                 return "interjection";
             }
         }
         return "input anything";
     }

}
