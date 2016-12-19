package re.neutrino.kanji_assist;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class DictionaryBackgroundTask extends AsyncTask<String, Void, String> {
    final static String baseApiUrl = "http://jisho.org/api/v1/search/words?keyword=";
    final static String debugName = "backgroundTask";
    HttpURLConnection connection;
    URL url;
    TextView textView;

    DictionaryBackgroundTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(debugName, "start");
        String text = strings[0];
        try {
            url = new URL(baseApiUrl + text);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            DictionaryParser dictionaryParser = new DictionaryParser(inputStream);
            dictionaryParser.read();
            connection.disconnect();
            return dictionaryParser.examples.subList(0, 2).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(debugName, "end");
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}
