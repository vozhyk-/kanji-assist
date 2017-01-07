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
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest {
    Context context;

    @Test
    public void example() throws Exception {
        context = InstrumentationRegistry.getContext();
        Dictionary dic = new Dictionary();
        dic.openDb(context);
        String[] test_data = {"test_word", "test_spelling"};
        long id = dic.put_example(test_data[0], test_data[1]);
        assertTrue("insert failed", id >= 0);
        String[] test = dic.get_example(id);
        assertTrue("word mismatch", test_data[0].equals(test[0]));
        assertTrue("spelling mismatch", test_data[1].equals(test_data[1]));
        dic.closeDb();
    }
}