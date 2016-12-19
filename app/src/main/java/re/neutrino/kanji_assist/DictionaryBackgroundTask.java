package re.neutrino.kanji_assist;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
            ArrayList<Definition> definitionArrayList = readJsonStream(inputStream);
            Log.d(debugName, definitionArrayList.toString());
            connection.disconnect();
            return definitionArrayList.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(debugName, "end");
        return null;
    }

    public ArrayList<Definition> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readResponse(reader);
        } finally {
            reader.close();
        }
    }

    public ArrayList<Definition> readResponse(JsonReader reader) throws IOException {
        ArrayList<Definition> definitions = new ArrayList<>();
        String data = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("data")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String inside_name = reader.nextName();
                        if (inside_name.equals("japanese")) {
                            reader.beginArray();
                            while (reader.hasNext()) {
                                definitions.add(readDefinition(reader));
                            }
                            reader.endArray();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return definitions;
    }

    class Definition {
        String word;
        String reading;

        public String toString() {
            return "word: " + word + ", reading: " + reading;
        }
    }

    Definition readDefinition(JsonReader reader) throws IOException {
        Definition definition = new Definition();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("word")) {
                definition.word = reader.nextString();
            } else if (name.equals("reading")) {
                definition.reading = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(debugName, definition.toString());
        return definition;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}
