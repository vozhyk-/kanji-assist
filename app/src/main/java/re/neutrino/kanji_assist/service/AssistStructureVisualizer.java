package re.neutrino.kanji_assist.service;

import android.app.Dialog;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker;
import re.neutrino.kanji_assist.text_extractor.ScreenText;

class AssistStructureVisualizer {
    private final Dialog dialog;
    private final LayoutInflater inflater;
    private String TAG = getClass().getName();

    public AssistStructureVisualizer(Dialog dialog) {
        this.dialog = dialog;

        inflater = dialog.getLayoutInflater();
    }

    public void show(AnyAssistStructure structure) {
        final RelativeLayout layout = (RelativeLayout)
                inflater.inflate(R.layout.visualizer, null);
        dialog.setContentView(layout);

        final AssistStructureWalker structureWalker =
                new AssistStructureWalker(structure);

        structureWalker.walkWindows(new AssistStructureWalker.Walker() {
            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect position, int depth) {
                if (node.getText() == null)
                    return null;

                final TextView textView =
                        recreateTextView(node, position, depth, layout);
                layout.addView(textView);
                return null;
            }
        });
    }

    @NonNull
    private TextView recreateTextView(AnyAssistStructure.ViewNode node,
                                      Rect position, int depth, RelativeLayout layout) {
        final TextView result = (TextView)
                inflater.inflate(R.layout.recreated_textview, layout, false);

        final String offset = AssistStructureWalker.getLogOffset(depth);

        result.setText(node.getText());
        result.setTextSize(TypedValue.COMPLEX_UNIT_PX, node.getTextSize());
        result.setTypeface(result.getTypeface(), node.getTextStyle());
        // TODO Text {color, background color}

        result.setAlpha(node.getAlpha());
        result.setElevation(node.getElevation());
        // TODO Transformation

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        node.getWidth(), node.getHeight());
        params.setMargins(position.left, position.top, 0, 0);
        result.setLayoutParams(params);

        return result;
    }
}
