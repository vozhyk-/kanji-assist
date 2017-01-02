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

package re.neutrino.kanji_assist;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.assist_structure.FakeAssistStructure;

public class AssistStructureDebugUtil {
    private Context context;

    public AssistStructureDebugUtil(Context context) {
        this.context = context;
    }

    public FakeAssistStructure readAssistStructure(int res) throws IOException {
        try (InputStream input = context.getResources()
                .openRawResource(res)) {
            try (InputStreamReader reader = new InputStreamReader(input)) {
                return FakeAssistStructure.fromJSON(reader);
            }
        }
    }

    public void saveStructureForDebug(AnyAssistStructure structure) {
        if (!BuildConfig.DEBUG)
            return;

        final FakeAssistStructure fakeStructure =
                new FakeAssistStructure(structure);

        if (!isExternalStorageWritable()) {
            Log.w(getClass().getName(),
                    "External storage not writable, not saving assist structure");
            return;
        }

        // TODO Ask for permission interactively

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "assist.json");
        file.getParentFile().mkdirs();

        try (FileOutputStream output = new FileOutputStream(file)) {
            try (OutputStreamWriter writer = new OutputStreamWriter(output)) {
                writer.write(fakeStructure.toJSON());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(getClass().getName(),
                "Saved assist structure to " + file);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
