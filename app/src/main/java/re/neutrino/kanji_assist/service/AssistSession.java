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

package re.neutrino.kanji_assist.service;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.view.View;
import android.widget.Toast;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.assist_structure.RealAssistStructure;
import re.neutrino.kanji_assist.dictionary_popup.DictionaryPopup;

class AssistSession extends VoiceInteractionSession {

    private AssistStructureVisualizer visualizer;
    private DictionaryPopup dictionaryPopup;

    AssistSession(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        final View result = super.onCreateContentView();

        dictionaryPopup = new DictionaryPopup(getWindow(), getContext());
        visualizer = new AssistStructureVisualizer(
                getWindow(), getContext(), dictionaryPopup);
        return result;
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        onHandleAssist(RealAssistStructure.createFrom(structure));
    }

    private void onHandleAssist(AnyAssistStructure structure) {
        if (structure == null) {
            Toast.makeText(getContext(),
                    "Please make sure you have enabled" +
                            " \"Use text from screen\"" +
                            " in Assist & voice input settings",
                    Toast.LENGTH_SHORT).show();
            hide();
            return;
        }
        getWindow().show();

        new AssistStructureSaver().saveStructureForDebug(structure);

        visualizer.show(structure);
    }

}
