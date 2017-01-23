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
    private ArrayList<Entry> entries = new ArrayList<>();

    public ArrayList<Entry> getEntries() { return entries; }

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
                    entries.add(readEntry());
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private Entry readEntry() throws IOException {
        Entry entry = new Entry();

        reader.beginObject();
        while (reader.hasNext()) {
            String inside_name = reader.nextName();
            if (inside_name.equals("japanese")) {
                readExamples(entry);
            } else if (inside_name.equals("senses")) {
                readSenses(entry);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (entry.examples.size() != entry.senses.size())
            Log.w(debugName, "size mismatch: "
                    + entry.examples.size() + " vs. " + entry.senses.size());
        return entry;
    }

    private void readSenses(Entry entry) throws  IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            entry.senses.add(readSense());
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

    private void readExamples(Entry entry) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            entry.examples.add(readExample());
        }
        reader.endArray();
    }

    private Example readExample() throws IOException {
        String word = "";
        String reading = "";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("word")) {
                word = reader.nextString();
            } else if (name.equals("reading")) {
                reading = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Example example = new Example(word, reading);
        Log.d(debugName, example.toString());
        return example;
    }
}
