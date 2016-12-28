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
        visualizer = new AssistStructureVisualizer(getWindow(), dictionaryPopup);
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
