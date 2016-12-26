package re.neutrino.kanji_assist.dictionary;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DictionaryParser {
    private final static String debugName = "dictionaryParser";
    private JsonReader reader;
    private ArrayList<Example> examples = new ArrayList<>();
    private ArrayList<Sense> senses = new ArrayList<>();

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

    public ArrayList<Example> getExamples() {
        return examples;
    }

    public ArrayList<Sense> getSenses() {
        return senses;
    }

    public class Sense {
        private ArrayList<String> definitions = new ArrayList<>();

        public String toString() {
            return "definitions: " + getDefinitions().toString();
        }

        public ArrayList<String> getDefinitions() {
            return definitions;
        }
    }

    private void readSenses() throws  IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            getSenses().add(readSense());
        }
        reader.endArray();
    }

    private Sense readSense() throws IOException {
        Sense sense = new Sense();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("english_definitions")) {
                reader.beginArray();
                while (reader.hasNext())
                    sense.getDefinitions().add(reader.nextString());
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(debugName, sense.toString());
        return sense;
    }

    public class Example {
        private String word;
        private String reading;

        public String toString() {
            return "word: " + getWord() + ", reading: " + getReading();
        }

        public String getWord() {
            return word;
        }

        public String getReading() {
            return reading;
        }
    }

    private void readExamples() throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            getExamples().add(readExample());
        }
        reader.endArray();
    }

    private Example readExample() throws IOException {
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
