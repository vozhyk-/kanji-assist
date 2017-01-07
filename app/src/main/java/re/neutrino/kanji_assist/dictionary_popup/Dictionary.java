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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import re.neutrino.kanji_assist.dictionary.DictionaryDbContract;
import re.neutrino.kanji_assist.dictionary.DictionaryDbHelper;
import re.neutrino.kanji_assist.dictionary.DictionaryParser;

class Dictionary {
    private final String debugName = "Dictionary";
    private DictionaryDbHelper dbHelper;

    void openDb(Context context) {
        dbHelper = new DictionaryDbHelper(context);
    }

    void closeDb() {
        dbHelper.close();
    }

    long put_example(String word, String spelling) {
        if (dbHelper == null) {
            Log.e(debugName, "put_example: db not opened");
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DictionaryDbContract.ExampleEntry.COLUMN_NAME_WORD, word);
        values.put(DictionaryDbContract.ExampleEntry.COLUMN_NAME_SPELLING, spelling);

        return db.insert(DictionaryDbContract.ExampleEntry.TABLE_NAME,
                null, values);
    }

    String[] get_example(long id) {
        if (dbHelper == null) {
            Log.e(debugName, "get_example: db not opened");
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DictionaryDbContract.ExampleEntry._ID,
                DictionaryDbContract.ExampleEntry.COLUMN_NAME_WORD,
                DictionaryDbContract.ExampleEntry.COLUMN_NAME_SPELLING
        };
        String selection = DictionaryDbContract.ExampleEntry._ID + "= ?";
        String[] selctionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                DictionaryDbContract.ExampleEntry.TABLE_NAME,
                projection,
                selection,
                selctionArgs,
                null,
                null,
                null
        );
        String[] ret = null;
        if (cursor.moveToFirst()) {
            String word = cursor.getString(cursor.getColumnIndex(
                    DictionaryDbContract.ExampleEntry.COLUMN_NAME_WORD));
            String spelling = cursor.getString(cursor.getColumnIndex(
                    DictionaryDbContract.ExampleEntry.COLUMN_NAME_SPELLING));
            String[] example = {word, spelling};
            ret = example;
        }
        cursor.close();
        return ret;
    }

    void get_definition(DictionaryPopup dictionaryPopup, String text) {
        DictionaryBackgroundTask dictionaryBackgroundTask = new
                DictionaryBackgroundTask(dictionaryPopup);
        Log.d("dictionary", text);
        dictionaryBackgroundTask.execute(text);
    }
}
