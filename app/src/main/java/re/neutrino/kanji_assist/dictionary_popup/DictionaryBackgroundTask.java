package re.neutrino.kanji_assist.dictionary_popup;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import re.neutrino.kanji_assist.dictionary.DictionaryParser;

import static java.lang.Math.min;

class DictionaryBackgroundTask extends AsyncTask<String, Void, String> {
    private final static String baseApiUrl =
            "http://jisho.org/api/v1/search/words?keyword=";
    private final static String debugName = "backgroundTask";
    private HttpURLConnection connection;
    private URL url;
    private TextView textView;

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
            List<DictionaryParser.Example> topExamples =
                    dictionaryParser.getExamples();
            List<DictionaryParser.Sense> topSenses =
                    dictionaryParser.getSenses();
            String ret = "";
            for (int i = 0; i < min(topExamples.size(), topSenses.size()); i++) {
                DictionaryParser.Example example = topExamples.get(i);
                DictionaryParser.Sense sense = topSenses.get(i);
                ret += example.getWord() +
                        " [" + example.getReading() + "] "
                        + sense.getDefinitions().toString() + "\n";
            }
            return ret;
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
