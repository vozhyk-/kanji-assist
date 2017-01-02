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

package re.neutrino.kanji_assist.assist_structure;

import android.content.ComponentName;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;

public class FakeAssistStructure implements AnyAssistStructure, Parcelable {
    private final ArrayList<WindowNode> windowNodes = new ArrayList<>();
    private final ComponentName activityComponent;

    public FakeAssistStructure(AnyAssistStructure structure) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++)
            windowNodes.add(new WindowNode(structure.getWindowNodeAt(i)));

        activityComponent = structure.getActivityComponent();
    }

    public static final Creator<FakeAssistStructure> CREATOR =
            new Creator<FakeAssistStructure>() {
        @Override
        public FakeAssistStructure createFromParcel(Parcel in) {
            return fromJSON(in.readString());
        }

        @Override
        public FakeAssistStructure[] newArray(int size) {
            return new FakeAssistStructure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(toJSON());
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static FakeAssistStructure fromJSON(String json) {
        return new Gson().fromJson(json, FakeAssistStructure.class);
    }

    public static FakeAssistStructure fromJSON(Reader reader) {
        return new Gson().fromJson(reader, FakeAssistStructure.class);
    }

    @Override
    public int getWindowNodeCount() {
        return windowNodes.size();
    }

    @Override
    public AnyAssistStructure.WindowNode getWindowNodeAt(int i) {
        return windowNodes.get(i);
    }

    @Override
    public ComponentName getActivityComponent() {
        return activityComponent;
    }

    private static String charSequenceToString(CharSequence text) {
        return text != null ? text.toString() : null;
    }

    private class WindowNode implements AnyAssistStructure.WindowNode, Serializable {
        private final ViewNode rootViewNode;
        private final int displayId;

        private final int left;
        private final int top;
        private final int width;
        private final int height;

        private final String title;

        WindowNode(AnyAssistStructure.WindowNode window) {
            this.rootViewNode = new ViewNode(window.getRootViewNode());
            this.displayId = window.getDisplayId();

            this.left = window.getLeft();
            this.top = window.getTop();
            this.width = window.getWidth();
            this.height = window.getHeight();

            this.title = charSequenceToString(window.getTitle());
        }

        @Override
        public AnyAssistStructure.ViewNode getRootViewNode() {
            return rootViewNode;
        }

        @Override
        public int getDisplayId() {
            return displayId;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public int getTop() {
            return top;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

    private class ViewNode implements AnyAssistStructure.ViewNode, Serializable {
        private final int visibility;
        private final boolean isAssistBlocked;

        private final String className;

        private final int left;
        private final int top;
        private final int width;
        private final int height;
        private final int scrollX;
        private final int scrollY;

        private final String text;
        private final String hint;
        private final String contentDescription;

        private final int textSelectionStart;
        private final int textSelectionEnd;
        private final int[] textLineBaselines;
        private final int[] textLineCharOffsets;
        private final float textSize;

        private final float alpha;
        private final float elevation;
        private final int textColor;
        private final int textBackgroundColor;
        private final int textStyle;
        private final Matrix transformation;

        private final int id;
        private final String idEntry;
        private final String idPackage;
        private final String idType;

        private final Bundle extras;

        private final boolean isAccessibilityFocused;
        private final boolean isActivated;
        private final boolean isFocusable;
        private final boolean isFocused;
        private final boolean isSelected;

        private final boolean isEnabled;
        private final boolean isClickable;
        private final boolean isContextClickable;
        private final boolean isLongClickable;
        private final boolean isCheckable;
        private final boolean isChecked;

        private final ArrayList<ViewNode> children = new ArrayList<>();

        ViewNode(AnyAssistStructure.ViewNode node) {
            this.visibility = node.getVisibility();
            this.isAssistBlocked = node.isAssistBlocked();

            this.className = node.getClassName();

            this.left = node.getLeft();
            this.top = node.getTop();
            this.width = node.getWidth();
            this.height = node.getHeight();
            this.scrollX = node.getScrollX();
            this.scrollY = node.getScrollY();

            this.text = charSequenceToString(node.getText());
            this.hint = node.getHint();
            this.contentDescription = charSequenceToString(
                    node.getContentDescription());

            this.textSelectionStart = node.getTextSelectionStart();
            this.textSelectionEnd = node.getTextSelectionEnd();
            this.textLineBaselines = node.getTextLineBaselines();
            this.textLineCharOffsets = node.getTextLineCharOffsets();
            this.textSize = node.getTextSize();

            this.alpha = node.getAlpha();
            this.elevation = node.getElevation();
            this.textColor = node.getTextColor();
            this.textBackgroundColor = node.getTextBackgroundColor();
            this.textStyle = node.getTextStyle();
            this.transformation = node.getTransformation();

            this.id = node.getId();
            this.idEntry = node.getIdEntry();
            this.idPackage = node.getIdPackage();
            this.idType = node.getIdType();

            this.extras = node.getExtras();

            this.isAccessibilityFocused = node.isAccessibilityFocused();
            this.isActivated = node.isActivated();
            this.isFocusable = node.isFocusable();
            this.isFocused = node.isFocused();
            this.isSelected = node.isSelected();

            this.isEnabled = node.isEnabled();
            this.isClickable = node.isClickable();
            this.isContextClickable = node.isContextClickable();
            this.isLongClickable = node.isLongClickable();
            this.isCheckable = node.isCheckable();
            this.isChecked = node.isChecked();

            for (int i = 0; i < node.getChildCount(); i++)
                children.add(new ViewNode(node.getChildAt(i)));
        }

        @Override
        public int getVisibility() {
            return visibility;
        }

        @Override
        public boolean isAssistBlocked() {
            return isAssistBlocked;
        }

        @Override
        public String getClassName() {
            return className;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public int getTop() {
            return top;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int getScrollX() {
            return scrollX;
        }

        @Override
        public int getScrollY() {
            return scrollY;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public String getHint() {
            return hint;
        }

        @Override
        public String getContentDescription() {
            return contentDescription;
        }

        @Override
        public int getTextSelectionStart() {
            return textSelectionStart;
        }

        @Override
        public int getTextSelectionEnd() {
            return textSelectionEnd;
        }

        @Override
        public int[] getTextLineBaselines() {
            return textLineBaselines;
        }

        @Override
        public int[] getTextLineCharOffsets() {
            return textLineCharOffsets;
        }

        @Override
        public float getTextSize() {
            return textSize;
        }

        @Override
        public float getAlpha() {
            return alpha;
        }

        @Override
        public float getElevation() {
            return elevation;
        }

        @Override
        public int getTextColor() {
            return textColor;
        }

        @Override
        public int getTextBackgroundColor() {
            return textBackgroundColor;
        }

        @Override
        public int getTextStyle() {
            return textStyle;
        }

        @Override
        public Matrix getTransformation() {
            return transformation;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getIdEntry() {
            return idEntry;
        }

        @Override
        public String getIdPackage() {
            return idPackage;
        }

        @Override
        public String getIdType() {
            return idType;
        }

        @Override
        public Bundle getExtras() {
            return extras;
        }

        @Override
        public boolean isAccessibilityFocused() {
            return isAccessibilityFocused;
        }

        @Override
        public boolean isActivated() {
            return isActivated;
        }

        @Override
        public boolean isFocusable() {
            return isFocusable;
        }

        @Override
        public boolean isFocused() {
            return isFocused;
        }

        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public boolean isEnabled() {
            return isEnabled;
        }

        @Override
        public boolean isClickable() {
            return isClickable;
        }

        @Override
        public boolean isContextClickable() {
            return isContextClickable;
        }

        @Override
        public boolean isLongClickable() {
            return isLongClickable;
        }

        @Override
        public boolean isCheckable() {
            return isCheckable;
        }

        @Override
        public boolean isChecked() {
            return isChecked;
        }

        @Override
        public int getChildCount() {
            return children.size();
        }

        @Override
        public AnyAssistStructure.ViewNode getChildAt(int i) {
            return children.get(i);
        }
    }
}
