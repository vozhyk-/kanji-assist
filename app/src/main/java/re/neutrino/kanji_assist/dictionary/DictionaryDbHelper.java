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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_DEFINITION_TABLE =
            "CREATE TABLE " + DictionaryDbContract.DefinitionEntry.TABLE_NAME + " (" +
                    DictionaryDbContract.DefinitionEntry._ID + " INTEGER PRIMARY KEY," +
                    DictionaryDbContract.DefinitionEntry.COLUMN_NAME_SENSE +" TEXT," +
                    DictionaryDbContract.DefinitionEntry.COLUMN_NAME_SPELLING + " TEXT)";

    private static final String SQL_CREATE_KANJI_TABLE =
            "CREATE TABLE " + DictionaryDbContract.InputKanjiEntry.TABLE_NAME + " (" +
                    DictionaryDbContract.InputKanjiEntry._ID + " INTEGER PRIMARY KEY," +
                    DictionaryDbContract.InputKanjiEntry.COLUMN_NAME_KANJI + " TEXT)";

    private static final String SQL_CREATE_KANJI2DEF_TABLE =
            "CREATE TABLE " + DictionaryDbContract.Kanji2Definition.TABLE_NAME + " (" +
                    DictionaryDbContract.Kanji2Definition.COLUMN_NAME_KANJI_KEY + " INTEGER," +
                    "FOREIGN KEY(" + DictionaryDbContract.Kanji2Definition.COLUMN_NAME_KANJI_KEY + ") " +
                    "REFERENCES " + DictionaryDbContract.InputKanjiEntry.TABLE_NAME +
                    "(" + DictionaryDbContract.InputKanjiEntry._ID + ")," +
                    DictionaryDbContract.Kanji2Definition.COLUMN_NAME_DEFINITION_KEY + " INTEGER," +
                    "FOREIGN KEY(" + DictionaryDbContract.Kanji2Definition.COLUMN_NAME_DEFINITION_KEY + ") " +
                    "REFERENCES " + DictionaryDbContract.DefinitionEntry.TABLE_NAME +
                    "(" + DictionaryDbContract.DefinitionEntry._ID + "))";

    private static final String SQL_DELETE_DEFINITION_TABLE =
            "DROP TABLE IF EXISTS " + DictionaryDbContract.DefinitionEntry.TABLE_NAME;

    private static final String SQL_DELETE_KANJI_TABLE =
            "DROP TABLE IF EXISTS " + DictionaryDbContract.InputKanjiEntry.TABLE_NAME;

    private static final String SQL_DELETE_KANJI2DEF_TABLE =
            "DROP TABLE IF EXISTS " + DictionaryDbContract.Kanji2Definition.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "KanjiAssistDefinitionCache.db";

    public DictionaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEFINITION_TABLE);
        db.execSQL(SQL_CREATE_KANJI_TABLE);
        db.execSQL(SQL_CREATE_KANJI2DEF_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_KANJI2DEF_TABLE);
        db.execSQL(SQL_DELETE_KANJI_TABLE);
        db.execSQL(SQL_DELETE_DEFINITION_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
