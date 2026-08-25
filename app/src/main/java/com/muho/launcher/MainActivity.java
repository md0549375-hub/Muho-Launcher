package com.muho.launcher;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class MainActivity extends Activity {
    private LinearLayout root;
    private LinearLayout appsPanel;
    private GestureDetector gestures;
    private float downX;
    private float downY;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(8, 9, 13));
        getWindow().setNavigationBarColor(Color.rgb(8, 9, 13));
        buildUi();
    }

    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView label(String text, float size) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextColor(Color.WHITE);
        v.setTextSize(size);
        v.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        v.setPadding(dp(16), dp(10), dp(16), dp(10));
        return v;
    }

    private void buildUi() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(8, 9, 13));
        root.setPadding(dp(18), dp(22), dp(18), dp(12));

        TextView title = label("MUHO", 30);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = label("Launcher • System Lab", 14);
        subtitle.setTextColor(Color.LTGRAY);
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, -2));

        TextView hint = label("↑ Uygulamalar   ↓ System Lab   ← → Sayfa", 13);
        hint.setTextColor(Color.GRAY);
        root.addView(hint, new LinearLayout.LayoutParams(-1, -2));

        View spacer = new View(this);
        root.addView(spacer, new LinearLayout.LayoutParams(1, 0, 1));

        TextView lab = label("SYSTEM LAB\nGesture ve launcher özelliklerini buradan geliştireceğiz.", 17);
        lab.setBackgroundColor(Color.rgb(20, 22, 29));
        lab.setOnClickListener(v -> showSystemLab());
        root.addView(lab, new LinearLayout.LayoutParams(-1, dp(110)));

        TextView apps = label("UYGULAMALAR", 16);
        apps.setOnClickListener(v -> showApps());
        root.addView(apps, new LinearLayout.LayoutParams(-1, dp(62)));

        setContentView(root);

        gestures = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onDown(MotionEvent e) { return true; }
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                if (Math.abs(dy) > Math.abs(dx) && Math.abs(dy) > dp(70)) {
                    if (dy < 0) showApps(); else showSystemLab();
                    return true;
                }
                if (Math.abs(dx) > dp(90)) return true;
                return false;
            }
        });
    }

    private void showSystemLab() {
        if (appsPanel != null) { root.removeView(appsPanel); appsPanel = null; }
        TextView panel = label("SYSTEM LAB\n\n• Gesture sistemi\n• Tema motoru\n• Plugin API\n• Launcher ayarları\n\nGeliştirme merkezi hazır.\n\n↓ Ana ekrana dön", 17);
        panel.setBackgroundColor(Color.rgb(18, 20, 27));
        panel.setOnClickListener(v -> buildUi());
        root.removeAllViews();
        root.addView(panel, new LinearLayout.LayoutParams(-1, -1));
    }

    private void showApps() {
        root.removeAllViews();
        appsPanel = new LinearLayout(this);
        appsPanel.setOrientation(LinearLayout.VERTICAL);
        appsPanel.setBackgroundColor(Color.rgb(8, 9, 13));
        TextView back = label("← MUHO LAUNCHER", 18);
        back.setOnClickListener(v -> buildUi());
        appsPanel.addView(back, new LinearLayout.LayoutParams(-1, -2));

        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ApplicationInfo> apps = new ArrayList<>();
        for (android.content.pm.ResolveInfo info : pm.queryIntentActivities(query, 0)) {
            if (info.activityInfo != null && info.activityInfo.packageName != getPackageName()) apps.add(info.activityInfo.applicationInfo);
        }
        Collections.sort(apps, Comparator.comparing(a -> pm.getApplicationLabel(a).toString(), String.CASE_INSENSITIVE_ORDER));

        for (ApplicationInfo app : apps) {
            TextView item = label(pm.getApplicationLabel(app).toString(), 16);
            item.setOnClickListener(v -> {
                Intent launch = pm.getLaunchIntentForPackage(app.packageName);
                if (launch != null) startActivity(launch);
            });
            appsPanel.addView(item, new LinearLayout.LayoutParams(-1, dp(52)));
        }
        root.addView(appsPanel, new LinearLayout.LayoutParams(-1, -1));
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { downX = event.getX(); downY = event.getY(); }
        return gestures != null && gestures.onTouchEvent(event) || super.dispatchTouchEvent(event);
    }
}
