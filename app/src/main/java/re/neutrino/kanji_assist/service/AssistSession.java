package re.neutrino.kanji_assist.service;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.assist_structure.RealAssistStructure;
import re.neutrino.kanji_assist.dictionary_popup.DictionaryPopup;
import re.neutrino.kanji_assist.text_extractor.ScreenText;
import re.neutrino.kanji_assist.text_extractor.TextExtractor;

class AssistSession extends VoiceInteractionSession {

    private TextExtractor textExtractor;
    private AssistStructureVisualizer visualizer;
    private DictionaryPopup dictionaryPopup;

    AssistSession(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        final View result = super.onCreateContentView();

        visualizer = new AssistStructureVisualizer(getWindow());
        dictionaryPopup = new DictionaryPopup(getWindow(), getContext());

        getWindow().getWindow().getDecorView()
                .setOnTouchListener(new OnTouchListener());

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

        textExtractor = new TextExtractor(structure);

        new AssistStructureSaver().saveStructureForDebug(structure);

        visualizer.show(structure);

        ScreenText selected = textExtractor.getSelectedText();
        if (selected != null) {
            dictionaryPopup.show(selected);
        }
    }

    private class OnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final PointF touchLocation =
                    new PointF(event.getX(), event.getY());
            Log.d(getClass().getName(),
                    touchLocation.toString());

            final ScreenText text = textExtractor.getTouchedText(touchLocation);
            if (text != null) {
                dictionaryPopup.show(text);
                return true;
            }

            return false;
        }
    }
}
