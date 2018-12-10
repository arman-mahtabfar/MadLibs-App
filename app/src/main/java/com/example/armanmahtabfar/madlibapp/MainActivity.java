 package com.example.armanmahtabfar.madlibapp;

 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.content.Intent;
 import android.widget.Toast;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.VolleyLog;
 import com.android.volley.toolbox.JsonArrayRequest;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;

 import org.json.JSONArray;
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

     private int allWordsSize;

    //only if we want to include multiple words. Lets get one first.
     /** List of the parts of speech that we will print */
     private ArrayList<String> partsOfSpeechOfReplacedWords;

     /**
      * this is the partofspeech that we are giving the user.
      */
     private String partOfSpeechofReplacedWord;

     /**
      * this is the replaced word that we are letting the user change in the quote.
      */
     private String replacedWord;

     /** Default loggin tag for messages from the main activity. */
     private static final String TAG = "Madlib:Main";

     /** Request queue for our network requests. */
     private static RequestQueue requestQueue;

    public void help(android.view.View v){
        startActivity(new Intent(MainActivity.this, Help.class));
    }

    public void modifyQuote(String s) {
        //this.quote = s;
        parseQuote(this.quote);
    }

    // this gets us an ArrayList of all the words we would possibly madlib.
     //WE NEED TO get this to return an int so we know how many there are. Maybe use a word of random index.
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

         final TextView partOfSpeech = findViewById(R.id.textView3);
         final EditText editTextToAdd = (EditText) findViewById(R.id.editText2);
         final TextView lyricsDisplay = (TextView) findViewById(R.id.textView4);


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
                                        quote = response.get("quote").toString();
                                        //in reality we dont want to show this yet.
                                        //the button to the right should do this.
                                        //lyricsDisplay.setText(quote);
                                        modifyQuote(response.get("quote").toString());
                                        lyricsDisplay.setText("Random Quote Found");
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
                Log.d(TAG, "Start API button clicked");
                // i call the dictionaryrequestURL method, whose parameter can change.
                JsonArrayRequest req = new JsonArrayRequest(dictionaryRequestURL(allWords.get(0)),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                try {
                                    // Parsing json array response
                                    // loop through each json object
                                    JSONObject wordDef = (JSONObject) response.get(0);
                                    replacedWord = allWords.get(0);
                                    String POS = wordDef.getString("fl");// + " " + replacedWord;
                                    partOfSpeech.setText(POS);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(req);
            }
        });
        Button ShowQuote = findViewById(R.id.ShowQuote);
        ShowQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the text to the madlibquotereturn string below with proper perameters.
                lyricsDisplay.setText("MadLib: \n");
                lyricsDisplay.append(madlibQuoteReturn(quote, editTextToAdd.getText().toString()));
                lyricsDisplay.append("\n");
                lyricsDisplay.append("   \n");
                lyricsDisplay.append("Original Quote:  \n");
                lyricsDisplay.append(quote);
            }
        });
     }

     /**
      * this will be what changes the dictionary url request based on the quote.
      * @param word this is the word that we are finding POS for.
      * @return
      */
     private String dictionaryRequestURL(String word) {
         String website = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/";
         String key = "?key=58ca55d6-2ee4-4f29-8e9f-7f7d3d471b8a";
         return website + word + key;
     }

     //this will modify the origial quote to the string that we want.
     private String madlibQuoteReturn(String quote, String replacer) {
         return quote.replace(replacedWord, replacer);
     }

}
