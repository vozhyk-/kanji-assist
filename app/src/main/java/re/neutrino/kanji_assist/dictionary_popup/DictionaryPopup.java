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

package re.neutrino.kanji_assist.dictionary_popup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.dictionary.Entry;
import re.neutrino.kanji_assist.dictionary.Example;
import re.neutrino.kanji_assist.dictionary.Sense;
import re.neutrino.kanji_assist.text_extractor.ScreenText;

public class DictionaryPopup extends GridLayout {
    private String TAG = getClass().getName();
    private final static String debugName = "dictionaryPopup";

    private Dictionary dictionary;
    private Context context;
    private ColorRotator colorRotator;
    private ScrollView scrollView;


    public DictionaryPopup(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.popup, this);

        this.dictionary = new Dictionary(context);
        this.context = context;
        this.colorRotator = new ColorRotator();
        this.scrollView = (ScrollView) this.findViewById(R.id.scrollView);
    }

    public DictionaryPopup(Context context) {
        this(context, null);
    }

    public void show(ScreenText screenText) {

        close();

        final Resources resources = getContext().getResources();
        float vertical_margin = resources.getDimension(
                R.dimen.popup_vertical_margin);
        float height = resources.getDimension(R.dimen.popup_height);

        PointF popupPosition = getPopupPosition(
                screenText, vertical_margin, height);

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, (int) height);
        params.setMargins((int) popupPosition.x, (int) popupPosition.y, 0, 0);

        setLayoutParams(params);

        TextView textSelected = (TextView) findViewById(R.id.textSelected);
        textSelected.setText(screenText.getText());

        dictionary.get_definition(this, screenText.getText());

        Button button = (Button) findViewById(R.id.buttonClose);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                close();
            }});

        show();
    }

    @NonNull
    private PointF getPopupPosition(ScreenText screenText, float vertical_margin, float height) {
        final String tag = TAG + ".show";

        Log.d(tag, String.valueOf(height));

        final Rect textRect = screenText.getRect();
        Log.d(tag, String.valueOf(textRect));

        WindowManager wm = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        Log.d(tag, displaySize.toString());

        PointF result = new PointF();
        if (textRect.bottom > displaySize.y/2) {
            // bottom half
            Log.d(tag, "bottom");
            result.y = textRect.bottom - height + vertical_margin;
        } else {
            // top half
            Log.d(tag, "top");
            result.y = textRect.bottom + vertical_margin;
        }
        //sanititze
        if (result.y < vertical_margin) {
            Log.w(tag, "Y value below vertical margin's: " + result.y);
            result.y = vertical_margin;
        }
        if (result.y > (displaySize.y - height - vertical_margin)) {
            Log.w(tag, "bottom value below vertical margin's: " + result.y);
            result.y = (displaySize.y - height - vertical_margin);
        }
        return result;
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    private void close() {
        setVisibility(GONE);
    }

    private class ColorRotator {
        private int colors[] = {
                ContextCompat.getColor(context, R.color.colorAlternate1),
                ContextCompat.getColor(context, R.color.colorAlternate2)};
        private int index = 0;
        private int size = 2;

        private int getNextColor() {
            index = (index+1) % size;
            return colors[index];
        }
    }

    public void addTextView(LinearLayout linearLayout, Entry i) {
        LinearLayout definitionLayout =
                (LinearLayout) View.inflate(context, R.layout.definition, null);
        TextView word = (TextView) definitionLayout.findViewById(R.id.word);
        TextView spelling = (TextView) definitionLayout.findViewById(R.id.spelling);
        String word_text = "";
        String spelling_text = "";
        for (Example j:i.examples) {
            if (j.getWord() == null) {
                Log.d(debugName, "null word, skipping");
            } else {
                word_text += j.getWord() + "\n";
            }
            if (j.getReading() == null) {
                Log.d(debugName, "null reading, skipping");
            } else {
                spelling_text += j.getReading() + "\n";
            }
        }
        word.setText(word_text);
        spelling.setText(spelling_text);
        TextView sense = (TextView) definitionLayout.findViewById(R.id.sense);
        String sense_text = "";
        for (Sense j:i.senses) {
            for (String k:j.getDefinitions()) {
                sense_text += k + "\n";
            }
        }
        sense.setText(sense_text);
        definitionLayout.setBackgroundColor(colorRotator.getNextColor());
        linearLayout.addView(definitionLayout);
    }

    public void addTextView(String i) {
        TextView textView = new TextView(context);
        textView.setText(i);
        scrollView.addView(textView);
    }

    public Dictionary getDictionary() {
        return this.dictionary;
    }
}
