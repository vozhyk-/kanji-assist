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

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.dictionary.DictionaryParser;

class DictionaryBackgroundTask extends AsyncTask<String, Void, String> {
    private final static  String providerURL = "jisho.org";
    private final static String baseApiUrl =
            "http://" + providerURL + "/api/v1/search/words?keyword=";
    private final static String debugName = "backgroundTask";
    private HttpURLConnection connection;
    private URL url;
    private ScrollView scrollView;
    private Context context;
    private ArrayList<String> showEntries = new ArrayList<>();
    final RelativeLayout.LayoutParams matchParentLayout =
            new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
    final RelativeLayout.LayoutParams elementLayout =
            new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
    private ColorRotator colorRotator;

    DictionaryBackgroundTask(DictionaryPopup dictionaryPopup) {
        this.context = dictionaryPopup.getContext();
        this.scrollView = (ScrollView) dictionaryPopup.findViewById(R.id.scrollView);
        this.scrollView.removeAllViews();
        ProgressBar progress = new ProgressBar(this.context);
        progress.setLayoutParams(matchParentLayout);
        this.scrollView.addView(progress);
        this.colorRotator = new ColorRotator();
    }

    private class ColorRotator {
        private int colors[] = {
                ContextCompat.getColor(context, R.color.colorAlternate1),
                ContextCompat.getColor(context, R.color.colorAlternate2)};
        private int index = 0;
        private int size = 2;

        private int getNextColor() {
            index = (index+1) % size;
            return colors[index];
        }
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
            List<DictionaryParser.Entry> entries =
                    dictionaryParser.getEntries();
            if (entries.size() == 0)
                return "No definitions found";
            for (int i = 0; i < entries.size(); i++) {
                DictionaryParser.Entry entry = entries.get(i);
                ArrayList<DictionaryParser.Example> examples = entry.examples;
                ArrayList<DictionaryParser.Sense> senses = entry.senses;
                if (examples.size() == 0) {
                    Log.w(debugName, "no words found, skip");
                    continue;
                }
                if (senses.size() == 0) {
                    Log.w(debugName, "no definitions found, skip");
                    continue;
                }
                showEntries.add(examples.toString() + ": " + senses.toString());
            }
            return null;
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

    private void addTextView(LinearLayout linearLayout, String i) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(elementLayout);
        textView.setText(i);
        textView.setBackgroundColor(colorRotator.getNextColor());
        linearLayout.addView(textView);
    }

    private void addTextView(String i) {
        TextView textView = new TextView(context);
        textView.setText(i);
        scrollView.addView(textView);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Log.d(debugName, showEntries.toString());
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            for (String i : showEntries) {
                addTextView(linearLayout, i);
            }
            scrollView.removeAllViews();
            scrollView.addView(linearLayout);
        } else {
            scrollView.removeAllViews();
            addTextView(result);
        }
    }
}
