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

import android.provider.BaseColumns;

public final class DictionaryDbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DictionaryDbContract() {}

    /* Inner classes that defines the table contents */
    public static class DefinitionEntry implements BaseColumns {
        public static final String TABLE_NAME = "definition";
        public static final String COLUMN_NAME_SENSE = "sense";
    }

    public static class ExampleEntry implements BaseColumns {
        public static final String TABLE_NAME = "examples";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_SPELLING = "spelling";
    }

    public static class Definition2Example implements BaseColumns {
        public static final String TABLE_NAME = "definition2example";
        public static final String COLUMN_NAME_DEFINITION_KEY = "definition_key";
        public static final String COLUMN_NAME_EXAMPLE_KEY = "example_key";
    }

    public static class InputKanjiEntry implements BaseColumns {
        public static final String TABLE_NAME = "input_kanji";
        public static final String COLUMN_NAME_KANJI = "word";
    }

    public static class Kanji2Example implements BaseColumns {
        public static final String TABLE_NAME = "kanji2definition";
        public static final String COLUMN_NAME_KANJI_KEY = "kanji_key";
        public static final String COLUMN_NAME_EXAMPLE_KEY = "example_key";
    }
}
