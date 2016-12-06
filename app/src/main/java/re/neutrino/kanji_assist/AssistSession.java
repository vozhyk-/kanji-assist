package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AssistSession extends VoiceInteractionSession {
    public AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        String selected = getSelectedText(structure);
        if (selected == null)
            selected = "[No text selected]";

        showPopup(selected);
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

    private String getSelectedText(AssistStructure structure) {
        Toast.makeText(getContext(), "Begin scan", Toast.LENGTH_SHORT).show();

        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AssistStructure.WindowNode win =
                    structure.getWindowNodeAt(i);
            final String selected = walkViews(win.getRootViewNode(), 0);
            if (selected != null) {
                Toast.makeText(getContext(),
                        "Found selected text", Toast.LENGTH_SHORT)
                        .show();
                return selected;
            }
        }

        Toast.makeText(getContext(),
                "Selected text not found", Toast.LENGTH_SHORT)
                .show();
        return null;
    }

    private String walkViews(AssistStructure.ViewNode node, int depth) {
        if (node.getVisibility() != View.VISIBLE)
            return null;

        if (node.getTextSelectionStart() != -1 &&
                node.getTextSelectionStart() != node.getTextSelectionEnd()) {
            return node.getText().subSequence(
                        node.getTextSelectionStart(),
                        node.getTextSelectionEnd())
                    .toString();
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            final String selected = walkViews(node.getChildAt(i), depth + 1);
            if (selected != null)
                return selected;
        }

        return null;
    }
}
