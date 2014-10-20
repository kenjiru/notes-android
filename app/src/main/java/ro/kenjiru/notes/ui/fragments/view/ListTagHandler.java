package ro.kenjiru.notes.ui.fragments.view;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;

import org.xml.sax.XMLReader;

public class ListTagHandler implements Html.TagHandler {
    private int listLevel = 0;
    private final int LIST_INDENT = 10;
    private final int LIST_ITEM_INDENT = LIST_INDENT * 2;
    private final int bulletMargin = new BulletSpan(LIST_INDENT).getLeadingMargin(true);

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("ul")) {
            processUnorderedList(opening, output);
        } else if (tag.equalsIgnoreCase("li")) {
            processListItem(opening, output);
        }
    }

    private void processUnorderedList(boolean opening, Editable output) {
        addNewLineIfNeeded(output);
        if (opening) {
            markElementStart(output, new Ul());
            listLevel++;
        } else {
            wrapWithElements(output, Ul.class, new Ul());
            listLevel--;
        }
    }

    private void processListItem(boolean opening, Editable output) {
        addNewLineIfNeeded(output);

        if (opening) {
            markElementStart(output, new Li());
        } else {
            LeadingMarginSpan.Standard leadingMarginSpan = new LeadingMarginSpan.Standard(getLeadingMargin());
            CustomBulletSpan bulletSpan = new CustomBulletSpan(getBulletMargin(), getBulletType());

            wrapWithElements(output, Li.class, leadingMarginSpan, bulletSpan);
        }
    }

    private CustomBulletSpan.BulletType getBulletType() {
        if (listLevel % 3 == 1) {
            return CustomBulletSpan.BulletType.FULL_CIRCLE;
        } else if (listLevel % 3 == 2) {
            return CustomBulletSpan.BulletType.EMPTY_CIRCLE;
        } else {
            return CustomBulletSpan.BulletType.TRIANGLE;
        }
    }

    private void markElementStart(Editable output, Object markerObject) {
        int length = output.length();

        output.setSpan(markerObject, length, length, Spanned.SPAN_MARK_MARK);
    }

    private void wrapWithElements(Editable output, Class markerClass, Object... elements) {
        int length = output.length();
        Object ulMarkerObject = getLast(output, markerClass);
        int startPosition = output.getSpanStart(ulMarkerObject);

        output.removeSpan(ulMarkerObject);

        if (startPosition != length) {
            for (Object element : elements) {
                output.setSpan(element, startPosition, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private int getLeadingMargin() {
        return (listLevel - 1) * LIST_ITEM_INDENT;
    }

    private int getBulletMargin() {
        int bulletMargin = LIST_INDENT;

        if (listLevel > 1) {
            bulletMargin -= this.bulletMargin;

            if (listLevel > 2) {
                bulletMargin -= (listLevel - 2) * LIST_ITEM_INDENT;
            }
        }
        return bulletMargin;
    }

    private Object getLast(Editable text, Class kind) {
        Object[] spans = text.getSpans(0, text.length(), kind);

        if (spans.length == 0) {
            return null;
        } else {
            for(int i = spans.length; i>0; i--) {
                if(text.getSpanFlags(spans[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return spans[i-1];
                }
            }
            return null;
        }
    }

    private void addNewLineIfNeeded(Editable output) {
        int length = output.length() - 1;

        if (length > 0 && output.charAt(length) != '\n') {
            output.append("\n");
        }
    }
}

// Marker classes
class Ul {}
class Li {}