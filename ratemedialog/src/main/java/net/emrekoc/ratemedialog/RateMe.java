package net.emrekoc.ratemedialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import java.util.concurrent.TimeUnit;

/**
 * Created by emre on 24/05/16.
 */
public class RateMe {

  private static final String NAME = RateMe.class.getName();
  private static final String LAST_SHOWN = "lastShown";
  private static final String PER_HOUR = "perHour";
  private static final String STYLE = "style";
  private static final String NEVER_SHOW = "neverShow";

  public static void register(Context context, int perHour) {
    if (perHour < 1) {
      throw new RuntimeException("Hour cannot be smaller than 1");
    }
    SharedPreferences sharedPreferences = getSharedPreferences(context);
    sharedPreferences.edit().putInt(PER_HOUR, perHour).commit();

    long lastShown = sharedPreferences.getLong(LAST_SHOWN, -1);
    if (lastShown == -1) {
      sharedPreferences.edit().putLong(LAST_SHOWN, System.currentTimeMillis()).commit();
    }
  }

  public static void register(Context context, int perHour, @StyleRes int style) {
    register(context, perHour);
    getSharedPreferences(context).edit().putInt(STYLE, style).commit();
  }

  private static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
  }

  public static void checkForShow(Context context, final AlertDialog.OnClickListener listener) {
    SharedPreferences sharedPreferences = getSharedPreferences(context);
    if (sharedPreferences.getBoolean(NEVER_SHOW, false)) {
      return;
    }
    int perHour = sharedPreferences.getInt(PER_HOUR, 1);
    long lastShown = sharedPreferences.getLong(LAST_SHOWN, -1);
    long currentTimeMilis = System.currentTimeMillis();
    if (currentTimeMilis - lastShown >= TimeUnit.HOURS.toMillis(perHour)) {
      sharedPreferences.edit().putLong(LAST_SHOWN, currentTimeMilis).commit();
      showImmediately(context, listener);
    }
  }

  public static void showImmediately(final Context context,
      final AlertDialog.OnClickListener listener) {
    final SharedPreferences sharedPreferences = getSharedPreferences(context);
    if (sharedPreferences.getBoolean(NEVER_SHOW, false)) {
      return;
    }

    final AlertDialog.OnClickListener clickListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
          listener.onClick(dialog, which);
        }
        switch (which) {
          case DialogInterface.BUTTON_POSITIVE:
            sharedPreferences.edit().putBoolean(NEVER_SHOW, true).commit();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + context.getPackageName()));
            context.startActivity(intent);
            break;
          case DialogInterface.BUTTON_NEGATIVE:
            break;
          case DialogInterface.BUTTON_NEUTRAL:
            sharedPreferences.edit().putBoolean(NEVER_SHOW, true).commit();
            break;
        }
      }
    };

    int style = sharedPreferences.getInt(STYLE, -1);

    AlertDialog.Builder adb =
        style == -1 ? new AlertDialog.Builder(context) : new AlertDialog.Builder(context, style);
    adb.setNegativeButton(context.getString(R.string.rate_me_button_negative), clickListener);
    adb.setPositiveButton(context.getString(R.string.rate_me_button_positive), clickListener);
    adb.setNeutralButton(context.getString(R.string.rate_me_button_never_show_again),
        clickListener);
    adb.setTitle(context.getString(R.string.rate_me_dialog_title));
    adb.setMessage(context.getString(R.string.rate_me_dialog_message));
    adb.create().show();
  }

  public static void checkForShow(Context context) {
    checkForShow(context, null);
  }

  public static void showImmediately(Context context) {
    showImmediately(context, null);
  }
}
