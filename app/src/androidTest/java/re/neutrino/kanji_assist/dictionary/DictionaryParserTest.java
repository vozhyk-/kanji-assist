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

import org.junit.Test;

import java.io.InputStream;

import re.neutrino.kanji_assist.BasicTest;
import re.neutrino.kanji_assist.R;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DictionaryParserTest extends BasicTest {

    @Test
    public void read() throws Exception {
        try (InputStream input = openResource(R.raw.dictionaryparse_1)) {
            DictionaryParser parser = new DictionaryParser(input);
            parser.read();
            assertTrue("Examples size mismatch",
                    parser.getExamples().size() == 21);
            assertTrue("Senses size mismatch",
                    parser.getSenses().size() == 18);
        }
    }

    @Test
    public void read_nullWords() throws Exception {
        try (InputStream input = openResource(
                R.raw.dictionaryparse_null_words)) {
            final DictionaryParser parser = new DictionaryParser(input);
            parser.read();

            assertThat(parser.getExamples().get(0).getWord(), notNullValue());
        }
    }

    // FIXME This should really be a test of DictionaryBackgroundTask.
    // The parser is correct in returning nulls.
    // It's the UI that shouldn't show nulls to the user.
    @Test
    public void read_nullReadings() throws Exception {
        try (InputStream input = openResource(
                R.raw.dictionaryparse_null_readings)) {
            final DictionaryParser parser = new DictionaryParser(input);
            parser.read();

            assertThat(parser.getExamples().get(2).getReading(), notNullValue());
        }
    }

    private InputStream openResource(int res) {
        return context.getResources().openRawResource(res);
    }

}