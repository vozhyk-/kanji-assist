package re.neutrino.kanji_assist;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.lang.Math.min;

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
            List<DictionaryParser.Example> topExamples = dictionaryParser.examples;
            List<DictionaryParser.Sense> topSenses = dictionaryParser.senses;
            String ret = "";
            for (int i = 0; i < min(topExamples.size(), topSenses.size()); i++) {
                DictionaryParser.Example example = topExamples.get(i);
                DictionaryParser.Sense sense = topSenses.get(i);
                ret += example.word + " [" + example.reading + "] " + sense.definitions.toString() + "\n";
            }
            return ret;
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
