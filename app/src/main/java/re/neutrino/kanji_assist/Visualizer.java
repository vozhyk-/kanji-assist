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
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.dictionary_popup.DictionaryPopup;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker.AbsoluteViewNode;
import re.neutrino.kanji_assist.text_extractor.ScreenText;
import re.neutrino.kanji_assist.text_extractor.TextExtractor;

public class Visualizer extends RelativeLayout {
    private DictionaryPopup dictionaryPopup;

    private final LayoutInflater inflater;

    private TextExtractor textExtractor;

    private String TAG = getClass().getName();

    private List<TextView> recreatedViews = new ArrayList<>();

    public Visualizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.visualizer, this);

        dictionaryPopup = new DictionaryPopup(getContext());
    }

    public Visualizer(Context context) {
        this(context, null);
    }

    public void show(AnyAssistStructure structure) {
        textExtractor = new TextExtractor(structure);

        showPopupForSelectedText();
        recreateTextViews(structure);
    }

    public void clear() {
        removeAllViews();
        recreatedViews.clear();
    }

    private void addRecreatedView(TextView textView) {
        recreatedViews.add(textView);
        addView(textView);
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
            public ScreenText run(AbsoluteViewNode absNode) {
                if (!absNode.hasText()) {
                    return null;
                }

                final TextView textView = recreateViewWithSize(absNode);
                addRecreatedView(textView);
                return null;
            }
        });
    }

    @NonNull
    private TextView recreateViewWithSize(AbsoluteViewNode absNode) {
        final TextView result = recreateView(absNode);
        setSize(absNode, result);
        return result;
    }

    @NonNull
    private TextView recreateView(AbsoluteViewNode absNode) {
        final AnyAssistStructure.ViewNode node = absNode.getNode();
        final Rect rect = absNode.getRect();
        final String logOffset = absNode.getLogOffset();

        final TextView result = (TextView)
                inflater.inflate(R.layout.recreated_textview, this, false);

        Log.d(TAG, logOffset + absNode.toString());

        result.setTag(absNode);
        result.setText(absNode.getTextToDisplay());
        setTextSize(result, node, logOffset);
        result.setTypeface(result.getTypeface(), node.getTextStyle());
        result.setAlpha(node.getAlpha());

        result.setOnClickListener(new OnClickListener(
                new ScreenText(result.getText().toString(), rect)));
        result.setCustomSelectionActionModeCallback(
                new SelectionCallback(result, absNode));
        return result;
    }

    private void setSize(AbsoluteViewNode absNode, TextView recreatedView) {
        final AnyAssistStructure.ViewNode node = absNode.getNode();
        final Rect rect = absNode.getRect();

        final LayoutParams params = new LayoutParams(
                node.getWidth(), node.getHeight());
        params.setMargins(rect.left, rect.top, 0, 0);
        recreatedView.setLayoutParams(params);
    }

    private void showOverlappingViews(TextView view) {
        final ArrayList<AbsoluteViewNode> overlappingViews =
                findOverlappingNodes(view);

        Log.d(TAG, "overlapping views:");
        for (AbsoluteViewNode v: overlappingViews) {
            Log.d(TAG, "    " + v);
        }

        final ListView container = (ListView) inflater.inflate(
                R.layout.overlapping_views_popup, this, false);
        container.setAdapter(new ArrayAdapter<AbsoluteViewNode>(
                getContext(), 0, overlappingViews
        ) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return recreateView(getItem(i));
            }
        });
        addView(container);
    }

    private ArrayList<AbsoluteViewNode> findOverlappingNodes(
            TextView selected) {
        final ArrayList<AbsoluteViewNode> result = new ArrayList<>();

        final Rect selectedRect = viewGetGlobalVisibleRect(selected);
        for (TextView view: recreatedViews) {
            final Rect rect = viewGetGlobalVisibleRect(view);
            if (Rect.intersects(selectedRect, rect)) {
                final AbsoluteViewNode absNode = (AbsoluteViewNode) view.getTag();
                result.add(absNode);
            }
        }

        return result;
    }

    @NonNull
    private static Rect viewGetGlobalVisibleRect(View view) {
        final Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
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
            Log.d(TAG, offset + "Using DP might be fine");
            float textSizeLimit = 24;
            if (textSize <= textSizeLimit) {
                Log.d(TAG, offset + "No, text size > limit");
                return;
            }
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
        private final AbsoluteViewNode absNode;

        public SelectionCallback(TextView textView, AbsoluteViewNode absNode) {
            this.textView = textView;
            this.absNode = absNode;
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
                    break;
                case R.id.overlapping_views:
                    showOverlappingViews();
                    break;
                case R.id.inspect:
                    inspectView();
                    break;
                default:
                    return false;
            }

            mode.finish();
            return true;
        }

        private void showPopupForAssistSelection() {
            final int start = textView.getSelectionStart();
            final int end = textView.getSelectionEnd();
            final String selectedText = textView.getText()
                    .subSequence(start, end)
                    .toString();

            final Rect rect = viewGetGlobalVisibleRect(textView);

            showPopup(new ScreenText(selectedText, rect));
        }

        private void showOverlappingViews() {
            Visualizer.this.showOverlappingViews(textView);
        }

        private void inspectView() {
            Log.d(TAG, "inspect: " + absNode);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
