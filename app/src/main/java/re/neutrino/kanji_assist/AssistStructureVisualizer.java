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
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

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

    public void clear() {
        removeAllViews();
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
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect rect, int depth) {
                if (!nodeHasText(node)) {
                    return null;
                }

                final String offset = AssistStructureWalker.getLogOffset(depth);

                final TextView textView = recreateTextView(node, rect, depth,
                        offset);
                addView(textView);
                return null;
            }

            private boolean nodeHasText(AnyAssistStructure.ViewNode node) {
                return node.getText() != null &&
                        !node.getText().toString().isEmpty();
            }
        });
    }

    @NonNull
    private TextView recreateTextView(AnyAssistStructure.ViewNode node,
                                      Rect rect, int depth, String offset) {
        final TextView result = (TextView)
                inflater.inflate(R.layout.recreated_textview, this, false);

        Log.d(TAG, offset + nodeToString(node, rect));

        result.setText(node.getText());

        setTextSize(result, node, offset);

        result.setTypeface(result.getTypeface(), node.getTextStyle());

        result.setAlpha(node.getAlpha());

        result.setOnClickListener(new OnClickListener(
                new ScreenText(result.getText().toString(), rect)));
        result.setCustomSelectionActionModeCallback(
                new SelectionCallback(result, node));

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        node.getWidth(), node.getHeight());
        params.setMargins(rect.left, rect.top, 0, 0);
        result.setLayoutParams(params);

        return result;
    }

    public static String nodeToString(AnyAssistStructure.ViewNode node, Rect rect) {
        StringBuilder msg = new StringBuilder()
                .append(node.getText())
                .append("@")
                .append(rect.toShortString())
                .append(", class name: ")
                .append(node.getClassName())
                .append(", text size: ")
                .append(node.getTextSize())
                .append(", scroll: ")
                .append(new Point(node.getScrollX(), node.getScrollY()))
                .append(", elevation: ")
                .append(node.getElevation())
                .append(", alpha: ")
                .append(node.getAlpha());

        if (node.getTextLineBaselines() != null)
            msg = msg.append(", line baselines: ")
                    .append(Arrays.toString(node.getTextLineBaselines()));

        if (node.getTextLineCharOffsets() != null)
            msg = msg.append(", line char offsets: ")
                    .append(Arrays.toString(node.getTextLineCharOffsets()));

        if (node.getTransformation() != null)
            msg = msg.append(", transformation: ")
                    .append(node.getTransformation());

        return msg.toString();
    }

    private void setTextSize(TextView textView,
                             AnyAssistStructure.ViewNode node,
                             String offset) {
        /*
         * TODO Test this method:
         *   Use chrome_overlapping_paragraph.json
         *     for a structure with text size in DP.
         *   Use settings_with_baselines.json
         *     for a structure with text size in PX.
         */
        final float textSize = node.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

        int textHeight = getTextHeight(textView, node);
        Log.d(TAG, String.format("%sText height: %d, node height: %d",
                offset, textHeight, node.getHeight()));

        if (textHeight <= node.getHeight()) {
            Log.d(TAG, offset + "Using DP is fine");
            return;
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        textHeight = getTextHeight(textView, node);
        Log.d(TAG, offset + "Using pixels, new text height: " + textHeight);
    }

    private int getTextHeight(TextView textView, AnyAssistStructure.ViewNode node) {
        final int[] lineBaselines = node.getTextLineBaselines();
        final int[] lineCharOffsets = node.getTextLineCharOffsets();

        if (lineBaselines == null || lineCharOffsets == null)
            // TODO Test with characters with a small height (e.g. "o").
            // In that case this might be wrong
            // (space above the character might not be taken into account)
            // and we may need to use the bottom of the view as the baseline.
            return getSingleLineHeight(textView, 0);

        final int lastLineBaseline = lineBaselines[lineBaselines.length - 1];
        final int lastLineOffset = lineCharOffsets[lineCharOffsets.length - 1];
        return lastLineBaseline + getSingleLineHeight(textView, lastLineOffset);
    }

    private int getSingleLineHeight(TextView textView, int start) {
        final String text = textView.getText().toString();
        return getTextBounds(textView, text, start, text.length())
                .height();
    }

    @NonNull
    private Rect getTextBounds(TextView textView, String text, int start, int end) {
        Rect result = new Rect();
        textView.getPaint().getTextBounds(text, start, end, result);
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

    private class SelectionCallback implements ActionMode.Callback {
        private final TextView textView;
        private final AnyAssistStructure.ViewNode node;

        public SelectionCallback(TextView textView, AnyAssistStructure.ViewNode node) {
            this.textView = textView;
            this.node = node;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(
                    R.menu.recreated_text_view_selection_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // http://stackoverflow.com/a/27990050/795068
            menu.removeItem(android.R.id.selectAll);
            menu.removeItem(android.R.id.cut);
            menu.removeItem(android.R.id.copy);
            menu.removeItem(android.R.id.shareText);

            if (!BuildConfig.DEBUG)
                menu.removeItem(R.id.inspect);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.show_dictionary:
                    showPopupForAssistSelection();
                    mode.finish();
                    return true;
                case R.id.inspect:
                    inspectView();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        private void showPopupForAssistSelection() {
            final int start = textView.getSelectionStart();
            final int end = textView.getSelectionEnd();
            final String selectedText = textView.getText()
                    .subSequence(start, end)
                    .toString();

            final Rect rect = new Rect();
            textView.getGlobalVisibleRect(rect);

            showPopup(new ScreenText(selectedText, rect));
        }

        private void inspectView() {
            Log.d(TAG, "inspect: " + nodeToString(node, new Rect()));
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
