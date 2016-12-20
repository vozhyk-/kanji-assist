package re.neutrino.kanji_assist.text_extractor;

import android.graphics.Rect;

public class ScreenText {
    private String text;
    private Rect rect;

    public ScreenText(String text, Rect rect) {
        this.text = text;
        this.rect = rect;
    }

    @Override
    public String toString() {
        return getText() + " @ " + getRect();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenText) {
            final ScreenText other = (ScreenText) obj;
            return getText().equals(other.getText()) &&
                    getRect().equals(other.getRect());
        }
        return super.equals(obj);
    }

    public String getText() {
        return text;
    }

    public Rect getRect() {
        return rect;
    }
}
