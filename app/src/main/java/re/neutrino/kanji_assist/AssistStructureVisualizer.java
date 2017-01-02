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

package re.neutrino.kanji_assist;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.dictionary_popup.DictionaryPopup;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker;
import re.neutrino.kanji_assist.text_extractor.ScreenText;
import re.neutrino.kanji_assist.text_extractor.TextExtractor;

public class AssistStructureVisualizer extends RelativeLayout {
    private DictionaryPopup dictionaryPopup;

    private final LayoutInflater inflater;

    private TextExtractor textExtractor;

    private String TAG = getClass().getName();

    public AssistStructureVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.visualizer, this);

        dictionaryPopup = new DictionaryPopup(getContext());
    }

    public AssistStructureVisualizer(Context context) {
        this(context, null);
    }

    public void show(AnyAssistStructure structure) {
        textExtractor = new TextExtractor(structure);
        showPopupForSelectedText();

        recreateTextViews(structure);
    }

    private void showPopupForSelectedText() {
        ScreenText selected = textExtractor.getSelectedText();
        if (selected != null) {
            showPopup(selected);
        }
    }

    private void showPopup(ScreenText text) {
        removeView(dictionaryPopup);

        // TODO Use max elevation of text views
        dictionaryPopup.setElevation(1000);
        addView(dictionaryPopup);

        dictionaryPopup.show(text);
    }

    private void recreateTextViews(AnyAssistStructure structure) {
        final AssistStructureWalker structureWalker =
                new AssistStructureWalker(structure);

        structureWalker.walkWindows(new AssistStructureWalker.Walker() {
            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect position, int depth) {
                if (node.getText() == null ||
                        node.getText().toString().isEmpty())
                    return null;

                final TextView textView =
                        recreateTextView(node, position, depth);
                addView(textView);
                return null;
            }
        });
    }

    @NonNull
    private TextView recreateTextView(AnyAssistStructure.ViewNode node,
                                      Rect position, int depth) {
        final TextView result = (TextView)
                inflater.inflate(R.layout.recreated_textview, this, false);

        final String offset = AssistStructureWalker.getLogOffset(depth);

        result.setText(node.getText());
        result.setTypeface(result.getTypeface(), node.getTextStyle());
        // TODO Text {color, background color}

        result.setAlpha(node.getAlpha());
        result.setElevation(dpToPixels(depth));
        // TODO Transformation

        result.setOnClickListener(new OnClickListener(
                new ScreenText(result.getText().toString(), position)));

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        node.getWidth(), node.getHeight());
        params.setMargins(position.left, position.top, 0, 0);
        result.setLayoutParams(params);

        return result;
    }

    private float dpToPixels(float dp) {
        return dp / getContext().getResources().getDisplayMetrics().density;
    }

    private class OnClickListener implements View.OnClickListener {
        private final ScreenText screenText;

        public OnClickListener(ScreenText screenText) {
            this.screenText = screenText;
        }

        @Override
        public void onClick(View view) {
            showPopup(screenText);
        }
    }
}
