package re.neutrino.kanji_assist;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.widget.Toast;

class AssistSession extends VoiceInteractionSession {
    AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        ScreenText selected = getSelectedTextToDisplay(structure);

        new DictionaryPopup(getWindow(), getContext()).show(selected);
    }

    @NonNull
    private ScreenText getSelectedTextToDisplay(AssistStructure structure) {
        ScreenText result = new TextExtractor(structure).getSelectedText();
        if (result != null) {
            Toast.makeText(getContext(),
                    "Found selected text", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(),
                    "Selected text not found", Toast.LENGTH_SHORT)
                    .show();
            result = new ScreenText("[No text selected]", new Point(500, 500));
        }
        return result;
    }

}
