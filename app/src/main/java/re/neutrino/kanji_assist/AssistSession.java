package re.neutrino.kanji_assist;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class AssistSession extends VoiceInteractionSession {

    private TextExtractor textExtractor;
    private DictionaryPopup dictionaryPopup;

    AssistSession(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        final View result = super.onCreateContentView();

        dictionaryPopup = new DictionaryPopup(getWindow(), getContext());
        getWindow().getWindow().getDecorView()
                .setOnTouchListener(new OnTouchListener());

        return result;
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        onHandleAssist(new RealAssistStructure(structure));
    }

    private void onHandleAssist(AnyAssistStructure structure) {
        getWindow().show();

        textExtractor = new TextExtractor(structure);

        saveStructureForDebug(this, structure);

        ScreenText selected = textExtractor.getSelectedText();
        if (selected != null) {
            dictionaryPopup.show(selected);
        }
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

    private class OnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final PointF touchLocation =
                    new PointF(event.getX(), event.getY());
            Log.d(getClass().getName(),
                    touchLocation.toString());

            dictionaryPopup.show(textExtractor.getTouchedText(touchLocation));

            return false;
        }
    }
}
