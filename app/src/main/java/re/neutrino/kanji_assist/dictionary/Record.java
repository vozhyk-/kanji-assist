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


import com.orm.SugarRecord;

import java.util.ArrayList;

public class Record extends SugarRecord {
    String kanji;
    //ArrayList<Entry> entry_list;

    public Record() {
    }

    /*public Record(String kanji, ArrayList<Entry> entries) {
        this.kanji = kanji;
        this.entry_list = entries;
    }*/
}
