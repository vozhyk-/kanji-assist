package re.neutrino.kanji_assist;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;

public class AssistSession extends VoiceInteractionSession {
    public AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        Log.d(getContext().getPackageName(),
                ((Integer)structure.getWindowNodeCount()).toString());
    }
}
