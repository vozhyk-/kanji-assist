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

import static org.junit.Assert.*;

public class DictionaryParserTest extends BasicTest {

    @Test
    public void read() throws Exception {
        InputStream inputStream = context.getResources().openRawResource(R.raw.dictionaryparse_1);
        DictionaryParser dictionaryParser = new DictionaryParser(inputStream);
        dictionaryParser.read();
        assertTrue("Examples size mismatch",
                dictionaryParser.getExamples().size() == 21);
        assertTrue("Senses size mismatch",
                dictionaryParser.getSenses().size() == 18);
        inputStream.close();
    }

}