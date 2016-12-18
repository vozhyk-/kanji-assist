package re.neutrino.kanji_assist;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class AssistSession extends VoiceInteractionSession {
    AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        onHandleAssist(new RealAssistStructure(structure));
    }

    private void onHandleAssist(AnyAssistStructure structure) {
        saveStructureForDebug(this, structure);

        ScreenText selected = getSelectedTextToDisplay(structure);

        new DictionaryPopup(getWindow(), getContext()).show(selected);
    }

    private static void saveStructureForDebug(AssistSession assistSession, AnyAssistStructure structure) {
        if (!BuildConfig.DEBUG)
            return;

        final FakeAssistStructure fakeStructure =
                new FakeAssistStructure(structure);

        if (!assistSession.isExternalStorageWritable()) {
            Log.w("saveStructureForDebug",
                    "External storage not writable, not saving assist structure");
            return;
        }

        // TODO Ask for permission interactively

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "assist.bin");
        if (!file.getParentFile().mkdirs()) {
            Log.e("saveStructureForDebug", "Directory not created, not saving assist structure");
        }

        try (FileOutputStream output = new FileOutputStream(file)) {
            new ObjectOutputStream(output).writeObject(fakeStructure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @NonNull
    private ScreenText getSelectedTextToDisplay(AnyAssistStructure structure) {
        ScreenText result = new TextExtractor(structure).getSelectedText();
        if (result == null) {
            return new ScreenText("[No text selected]", new Point(500, 500));
        }
        return result;
    }

}
