package re.neutrino.kanji_assist;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DictionaryParser {
    final static String debugName = "dictionaryParser";
    JsonReader reader;
    ArrayList<Example> examples = new ArrayList<>();
    ArrayList<Sense> senses = new ArrayList<>();

    public DictionaryParser(InputStream inputStream) throws UnsupportedEncodingException {
        reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
    }
    public void read() throws IOException {
        try {
            readResponse();
        } finally {
            reader.close();
        }
    }

    private void readResponse() throws IOException {
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
                            readExamples();
                        } else if (inside_name.equals("senses")) {
                            readSenses();
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
    }

    class Sense {
        ArrayList<String> definitions = new ArrayList<>();

        public String toString() {
            return "definitions: " + definitions.toString();
        }
    }

    private void readSenses() throws  IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            senses.add(readSense());
        }
        reader.endArray();
    }

    Sense readSense() throws IOException {
        Sense sense = new Sense();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("english_definitions")) {
                reader.beginArray();
                while (reader.hasNext())
                    sense.definitions.add(reader.nextString());
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(debugName, sense.toString());
        return sense;
    }

    class Example {
        String word;
        String reading;

        public String toString() {
            return "word: " + word + ", reading: " + reading;
        }
    }

    private void readExamples() throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            examples.add(readExample());
        }
        reader.endArray();
    }

    Example readExample() throws IOException {
        Example example = new Example();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("word")) {
                example.word = reader.nextString();
            } else if (name.equals("reading")) {
                example.reading = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(debugName, example.toString());
        return example;
    }
}
