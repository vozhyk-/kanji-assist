/*
 * Copyright (C) 2016-2017 Witaut Bajaryn, Aleksander Mistewicz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package re.neutrino.kanji_assist.dictionary_popup;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

import re.neutrino.kanji_assist.dictionary.DictionaryParser;

import static java.lang.Math.min;

class DictionaryBackgroundTask extends AsyncTask<String, Void, String> {
    private final static  String providerURL = "jisho.org";
    private final static String baseApiUrl =
            "http://" + providerURL + "/api/v1/search/words?keyword=";
    private final static String debugName = "backgroundTask";
    private HttpURLConnection connection;
    private URL url;
    private TextView textView;

    DictionaryBackgroundTask(TextView textView) {
        this.textView = textView;
    }

    private DictionaryParser fetch(String text) throws IOException {
        String query = URLEncoder.encode(text, "utf-8");
        url = new URL(baseApiUrl + query);
        Log.d(debugName, "URL: " + url);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        DictionaryParser dictionaryParser = new DictionaryParser(inputStream);
        dictionaryParser.read();
        connection.disconnect();
        return dictionaryParser;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(debugName, "start");
        String text = strings[0];
        try {
            DictionaryParser dictionaryParser = fetch(text);
            List<DictionaryParser.Example> topExamples =
                    dictionaryParser.getExamples();
            List<DictionaryParser.Sense> topSenses =
                    dictionaryParser.getSenses();
            int list_size = min(topExamples.size(), topSenses.size());
            if (list_size == 0)
                return "No definitions found";
            String ret = "";
            for (int i = 0; i < list_size; i++) {
                DictionaryParser.Example example = topExamples.get(i);
                DictionaryParser.Sense sense = topSenses.get(i);
                if (example.getWord() == null) {
                    Log.w(debugName, "word is null, skip");
                    continue;
                }
                if (sense.getDefinitions() == null) {
                    Log.w(debugName, "definitions is null, skip");
                    continue;
                }
                if (example.getReading() == null) {
                    Log.w(debugName, "reading is null, do not skip");
                    ret += example.getWord() +
                            " [] "
                            + sense.getDefinitions().toString() + "\n";
                } else {
                    ret += example.getWord() +
                            " [" + example.getReading() + "] "
                            + sense.getDefinitions().toString() + "\n";
                }
            }
            return ret;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error: unsupported encoding";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error: malformed URL";
        } catch (UnknownHostException e) {
            return "Error: Failed to reach " + providerURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Failed to fetch definitions";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}
