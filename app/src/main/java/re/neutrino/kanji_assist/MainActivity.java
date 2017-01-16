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

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ensureAssistantEnabled();
    }

    private void ensureAssistantEnabled() {
        if (!isAssistantEnabled()) {
            Toast.makeText(this,
                    R.string.please_enable_assistant, Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));
        }
    }

    private boolean isAssistantEnabled() {
        final String assistantComponent = Settings.Secure.getString(
                getContentResolver(),
                "voice_interaction_service");

        if (assistantComponent == null)
            return false;

        final ComponentName cn =
                ComponentName.unflattenFromString(assistantComponent);

        if (cn == null)
            return false;

        final String assistantPackage = cn.getPackageName();

        return assistantPackage.equals(getPackageName());
    }
}
