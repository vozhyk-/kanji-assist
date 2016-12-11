package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

class AssistSession extends VoiceInteractionSession {
    AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        String selected = getSelectedTextToDisplay(structure);

        showPopup(selected);
    }

    @NonNull
    private String getSelectedTextToDisplay(AssistStructure structure) {
        String result = TextExtractor.getSelectedText(structure);
        if (result != null) {
            Toast.makeText(getContext(),
                    "Found selected text", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(),
                    "Selected text not found", Toast.LENGTH_SHORT)
                    .show();
            result = "[No text selected]";
        }
        return result;
    }

    private void showPopup(String text) {
        Dialog dialog = getWindow();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Log.d("set_pos (pre), x", String.valueOf(layoutParams.x));
        Log.d("set_pos (pre), y", String.valueOf(layoutParams.y));
        layoutParams.gravity = Gravity.NO_GRAVITY;
        layoutParams.x = 100;
        layoutParams.y = 100;
        window.setAttributes(layoutParams);
        Log.d(getContext().getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(text);
        layoutParams = window.getAttributes();
        Log.d("set_pos (post), x", String.valueOf(layoutParams.x));
        Log.d("set_pos (post), y", String.valueOf(layoutParams.y));
    }

}
