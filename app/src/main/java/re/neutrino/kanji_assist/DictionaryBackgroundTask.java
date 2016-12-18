package re.neutrino.kanji_assist;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class DictionaryBackgroundTask extends AsyncTask<String, Void, String> {
    final static String baseApiUrl = "http://jisho.org/api/v1/search/words?keyword=";
    HttpURLConnection connection;
    BufferedReader reader;
    URL url;
    TextView textView;

    DictionaryBackgroundTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("backgroundTask", "start");
        String text = strings[0];
        try {
            url = new URL(baseApiUrl + text);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.close();
            connection.disconnect();
            return "fetch: success";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("backgroundTask", "end");
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}
