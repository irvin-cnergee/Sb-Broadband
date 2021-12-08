package developer.app.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.widgets.MyTextView;


/**
 * Created by Sandip on 8/3/2016.
 */
public class DialogUtils {

   static NiftyDialogBuilder dialog_box;

    public static void show_dialog(final Context ctx, String title,
                                   String message, final boolean finish) {
        Effectstype effect = Effectstype.Slidetop;
        dialog_box = NiftyDialogBuilder.getInstance(ctx);
        dialog_box
				/*
				 * .withTitle("Confirmation") .withTitle(null) no title
				 * .withTitleColor(ctx.getString(R.color.theme_color))
				 */
                // def
                .withTitle(null)
                .withDividerColor("#eeeeee")
                // def
                // .withMessage(Message) //.withMessage(null) no Msg
                .withMessage(null)
                .withMessageColor("#eeeeee")
                // def | withMessageColor(int resid)
                .withDialogColor("#eeeeee")
                // def | withDialogColor(int resid) //def
                .withIcon(
                        ctx.getResources().getDrawable(R.mipmap.ic_launcher))
                .isCancelableOnTouchOutside(finish?false:true) // def | isCancelable(true)
                .withDuration(700)
                .isCancelable(finish?false:true)
                // def
                .withEffect(effect).setCustomView(R.layout.dialog_box, ctx)// def
                // Effectstype.Slidetop
                .show();

        MyTextView tv_meesage = (MyTextView) dialog_box.findViewById(R.id.tv_message);
        MyTextView tv_title = (MyTextView) dialog_box.findViewById(R.id.tv_title);

        Button btn_ok = (Button) dialog_box
                .findViewById(R.id.btn_next);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dialog_box != null) {
                    dialog_box.dismiss();
                }
                if (finish) {
                    ((Activity) ctx).finish();
                }
            }
        });

        tv_meesage.setText(message);
        tv_title.setText(title);
    }

}
