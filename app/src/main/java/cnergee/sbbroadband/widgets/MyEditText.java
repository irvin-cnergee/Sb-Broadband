package cnergee.sbbroadband.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import cnergee.sbbroadband.R;


/**
 * Created by Siddhesh on 9/19/2016.
 */
public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyEditText(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

}
