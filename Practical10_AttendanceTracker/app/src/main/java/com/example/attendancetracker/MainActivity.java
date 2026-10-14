package com.example.attendancetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private int blue, deepBlue, paleBlue, ink, muted, white, green, red;
    private LinearLayout content;
    private android.content.SharedPreferences prefs;
    private JSONArray subjects;
    private String student = "Student";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        blue = getColor(R.color.blue_primary);
        deepBlue = getColor(R.color.blue_deep);
        paleBlue = getColor(R.color.blue_soft);
        ink = getColor(R.color.ink);
        muted = getColor(R.color.muted);
        white = Color.WHITE;
        green = getColor(R.color.success);
        red = getColor(R.color.danger);
        prefs = getSharedPreferences("attendance_data", MODE_PRIVATE);
        loadData();
        showLogin();
    }

    private void loadData() {
        try { subjects = new JSONArray(prefs.getString("subjects", "[]")); }
        catch (Exception e) { subjects = new JSONArray(); }
        if (subjects.length() == 0) {
            try {
                JSONObject a = new JSONObject(); a.put("name", "Data Structures"); a.put("present", 0); a.put("absent", 0); a.put("cancelled", 0); a.put("history", new JSONArray()); subjects.put(a);
                JSONObject b = new JSONObject(); b.put("name", "Operating Systems"); b.put("present", 0); b.put("absent", 0); b.put("cancelled", 0); b.put("history", new JSONArray()); subjects.put(b);
                JSONObject c = new JSONObject(); c.put("name", "Java Programming"); c.put("present", 0); c.put("absent", 0); c.put("cancelled", 0); c.put("history", new JSONArray()); subjects.put(c);
                saveData();
            } catch (Exception ignored) { }
        }
    }

    private void saveData() { prefs.edit().putString("subjects", subjects.toString()).apply(); }

    private void setup(String title, String subtitle) {
        setContentView(R.layout.activity_main);
        TextView heading = findViewById(R.id.screenTitle);
        TextView subheading = findViewById(R.id.screenSubtitle);
        heading.setText(title);
        subheading.setText(subtitle);
        content = findViewById(R.id.contentContainer);
    }

    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(ink);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setPadding(0, dp(4), 0, dp(4));
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private TextView label(String value) {
        TextView t = text(value.toUpperCase(Locale.ROOT), 11, true);
        t.setTextColor(muted); t.setLetterSpacing(0.07f);
        return t;
    }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }

    private LinearLayout panel() {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(17), dp(15), dp(17), dp(15)); box.setBackgroundResource(R.drawable.bg_card);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(13));
        content.addView(box, lp); return box;
    }

    private Button button(String caption, int color, int textColor, Runnable action) {
        Button b = new Button(this); b.setText(caption); b.setAllCaps(false); b.setTextSize(14);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD); b.setTextColor(textColor);
        b.setBackground(rounded(color, 14)); b.setMinHeight(dp(48)); b.setPadding(dp(14), dp(8), dp(14), dp(8));
        b.setElevation(dp(1)); b.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(5), 0, dp(5));
        b.setLayoutParams(lp); return b;
    }

    private Button primary(String caption, Runnable action) { return button(caption, blue, white, action); }
    private Button secondary(String caption, Runnable action) { return button(caption, paleBlue, deepBlue, action); }

    private EditText field(String hint, int inputType) {
        EditText e = new EditText(this); e.setSingleLine(true); e.setHint(hint); e.setTextSize(15);
        e.setTextColor(ink); e.setHintTextColor(muted); e.setInputType(inputType);
        e.setPadding(dp(14), dp(12), dp(14), dp(12)); e.setBackgroundResource(R.drawable.bg_input);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(6), 0, dp(9));
        e.setLayoutParams(lp); return e;
    }

    private void showLogin() {
        setup("Lecture Attendance Tracker", "Your classes, your goals, all in one place.");
        View spacer = new View(this); content.addView(spacer, new LinearLayout.LayoutParams(1, dp(18)));
        LinearLayout welcome = panel();
        TextView icon = text("✦", 30, true); icon.setTextColor(blue); welcome.addView(icon);
        welcome.addView(text("Welcome back!", 25, true));
        TextView desc = text("Let's keep your attendance on track.", 14, false); desc.setTextColor(muted); welcome.addView(desc);
        content.addView(label("Your details"));
        EditText name = field("Enter your name", 1);
        EditText password = field("Password (demo only)", 129);
        content.addView(name); content.addView(password);
        content.addView(primary("Log in  →", () -> {
            student = name.getText().toString().trim(); if (student.isEmpty()) student = "Student";
            prefs.edit().putString("student_name", student).apply(); showDashboard();
        }));
        TextView note = text("No account setup needed for this practical demo.", 12, false);
        note.setTextColor(muted); note.setGravity(Gravity.CENTER); note.setPadding(0, dp(16), 0, dp(5)); content.addView(note);
    }

    private void showDashboard() {
        student = prefs.getString("student_name", student);
        setup("Hello, " + student + "!", "Small steps make a big difference.");
        content.addView(text("Your overview", 21, true));
        TextView intro = text("Here's how your classes are going.", 14, false); intro.setTextColor(muted); content.addView(intro);
        int p = total("present"), a = total("absent"), c = total("cancelled");
        int pct = (p + a == 0) ? 0 : Math.round(100f * p / (p + a));
        LinearLayout overall = panel(); overall.setBackground(rounded(blue, 22));
        TextView small = text("OVERALL ATTENDANCE", 12, true); small.setTextColor(getColor(R.color.blue_pale)); overall.addView(small);
        TextView big = text(pct + "%", 42, true); big.setTextColor(white); big.setPadding(0, dp(4), 0, 0); overall.addView(big);
        ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100); progress.setProgress(pct); progress.setProgressTintList(ColorStateList.valueOf(white));
        progress.setProgressBackgroundTintList(ColorStateList.valueOf(0x667FB2F2));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(7)); pp.setMargins(0, dp(8), 0, dp(8)); overall.addView(progress, pp);
        TextView status = text(p + " present   •   " + a + " absent", 13, false); status.setTextColor(white); overall.addView(status);
        TextView cancel = text(c + " cancelled lectures excluded from percentage", 11, false); cancel.setTextColor(getColor(R.color.blue_pale)); overall.addView(cancel);

        content.addView(label("At a glance"));
        LinearLayout stats = new LinearLayout(this); stats.setOrientation(LinearLayout.HORIZONTAL);
        addStat(stats, "SUBJECTS", String.valueOf(subjects.length()), blue);
        addStat(stats, "PRESENT", String.valueOf(p), green);
        addStat(stats, "ABSENT", String.valueOf(a), red);
        LinearLayout.LayoutParams statsLp = new LinearLayout.LayoutParams(-1, -2); statsLp.setMargins(0, 0, 0, dp(14)); content.addView(stats, statsLp);

        content.addView(label("Your workspace"));
        content.addView(primary("✓   Mark lecture attendance", this::showAttendance));
        content.addView(secondary("▦   Manage subjects & target", this::showSubjects));
        content.addView(secondary("◷   Attendance history", this::showHistory));
        content.addView(button("Log out", 0xFFFFFFFF, muted, this::showLogin));
        int target = prefs.getInt("target", 75);
        TextView targetNote = text("Your attendance target is " + target + "%.", 12, false);
        targetNote.setTextColor(muted); targetNote.setGravity(Gravity.CENTER); targetNote.setPadding(0, dp(12), 0, 0); content.addView(targetNote);
    }

    private void addStat(LinearLayout row, String caption, String value, int color) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(11), dp(12), dp(8), dp(12)); box.setBackgroundResource(R.drawable.bg_card);
        TextView v = text(value, 24, true); v.setTextColor(color); box.addView(v);
        TextView l = text(caption, 9, true); l.setTextColor(muted); l.setLetterSpacing(0.04f); box.addView(l);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1); lp.setMargins(0, 0, dp(7), 0); row.addView(box, lp);
    }

    private int total(String key) {
        int sum = 0; for (int i = 0; i < subjects.length(); i++) sum += subjects.optJSONObject(i).optInt(key, 0); return sum;
    }

    private void showSubjects() {
        setup("Subjects & Target", "Make your timetable work for you.");
        content.addView(text("Attendance goal", 21, true));
        LinearLayout targetCard = panel();
        targetCard.addView(text("Set your minimum attendance target.", 14, false));
        EditText target = field("Target percentage (1–100)", 2); target.setText(String.valueOf(prefs.getInt("target", 75))); targetCard.addView(target);
        targetCard.addView(primary("Save attendance target", () -> {
            try { int n = Integer.parseInt(target.getText().toString()); if (n < 1 || n > 100) throw new Exception(); prefs.edit().putInt("target", n).apply(); toast("Target saved: " + n + "%"); }
            catch (Exception e) { toast("Enter a target between 1 and 100"); }
        }));
        content.addView(text("Your subjects", 21, true));
        for (int i = 0; i < subjects.length(); i++) {
            final int idx = i; JSONObject s = subjects.optJSONObject(i);
            LinearLayout box = panel(); box.addView(text("▰  " + s.optString("name"), 17, true));
            int pct = percent(s); TextView info = text("" + pct + "% attendance  •  " + s.optInt("present") + " present, " + s.optInt("absent") + " absent", 13, false); info.setTextColor(muted); box.addView(info);
            ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal); bar.setMax(100); bar.setProgress(pct); bar.setProgressTintList(ColorStateList.valueOf(pct >= prefs.getInt("target", 75) ? green : blue));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(6)); bp.setMargins(0, dp(8), 0, dp(8)); box.addView(bar, bp);
            box.addView(secondary("Delete subject", () -> new AlertDialog.Builder(this).setTitle("Delete subject?").setMessage("Remove this subject and its attendance history?").setNegativeButton("Cancel", null).setPositiveButton("Delete", (d, w) -> { JSONArray n = new JSONArray(); for (int j = 0; j < subjects.length(); j++) if (j != idx) n.put(subjects.optJSONObject(j)); subjects = n; saveData(); showSubjects(); }).show()));
        }
        content.addView(primary("＋  Add subject", () -> {
            EditText input = field("Subject name", 1);
            new AlertDialog.Builder(this).setTitle("Add a subject").setView(input).setNegativeButton("Cancel", null).setPositiveButton("Add", (d, w) -> {
                String n = input.getText().toString().trim(); if (n.isEmpty()) { toast("Enter a subject name"); return; }
                try { JSONObject s = new JSONObject(); s.put("name", n); s.put("present", 0); s.put("absent", 0); s.put("cancelled", 0); s.put("history", new JSONArray()); subjects.put(s); saveData(); showSubjects(); } catch (Exception ignored) { }
            }).show();
        }));
        content.addView(secondary("←  Back to dashboard", this::showDashboard));
    }

    private int percent(JSONObject s) { int p = s.optInt("present"), a = s.optInt("absent"); return p + a == 0 ? 0 : Math.round(100f * p / (p + a)); }

    private void showAttendance() {
        setup("Mark attendance", "Choose a subject, then record today's lecture.");
        if (subjects.length() == 0) { content.addView(text("You don't have any subjects yet. Add one first.", 15, false)); content.addView(primary("Manage subjects", this::showSubjects)); }
        for (int i = 0; i < subjects.length(); i++) {
            final int idx = i; JSONObject s = subjects.optJSONObject(i);
            LinearLayout box = panel(); box.addView(text(s.optString("name"), 18, true));
            TextView pct = text("Current attendance: " + percent(s) + "%", 13, false); pct.setTextColor(muted); box.addView(pct);
            box.addView(button("✓  Present", green, white, () -> record(idx, "Present")));
            box.addView(button("✕  Absent", red, white, () -> record(idx, "Absent")));
            box.addView(secondary("—  Cancelled lecture", () -> record(idx, "Cancelled")));
            box.addView(secondary("↶  Undo last entry", () -> undo(idx)));
        }
        content.addView(secondary("←  Back to dashboard", this::showDashboard));
    }

    private void record(int idx, String status) {
        JSONObject s = subjects.optJSONObject(idx); String key = status.toLowerCase(Locale.ROOT);
        try {
            s.put(key, s.optInt(key) + 1); JSONArray h = s.optJSONArray("history"); if (h == null) h = new JSONArray();
            JSONObject entry = new JSONObject(); entry.put("status", status); entry.put("date", new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date())); h.put(entry); s.put("history", h);
            saveData(); toast(status + " recorded"); showAttendance();
        } catch (Exception ignored) { }
    }

    private void undo(int idx) {
        JSONObject s = subjects.optJSONObject(idx); JSONArray h = s.optJSONArray("history");
        if (h == null || h.length() == 0) { toast("No entry to undo"); return; }
        JSONObject last = h.optJSONObject(h.length() - 1); String key = last.optString("status").toLowerCase(Locale.ROOT);
        JSONArray nh = new JSONArray();
        try {
            s.put(key, Math.max(0, s.optInt(key) - 1));
            for (int i = 0; i < h.length() - 1; i++) {
                nh.put(h.opt(i));
            }
            s.put("history", nh);
        } catch (org.json.JSONException e) {
            toast("Could not undo attendance");
            return;
        }
        saveData();
        toast("Last entry undone");
        showAttendance();
    }

    private void showHistory() {
        setup("Attendance history", "Every recorded lecture, in one place.");
        boolean any = false;
        for (int i = 0; i < subjects.length(); i++) {
            JSONObject s = subjects.optJSONObject(i); LinearLayout box = panel(); box.addView(text(s.optString("name"), 17, true));
            JSONArray h = s.optJSONArray("history");
            if (h != null && h.length() > 0) {
                for (int j = h.length() - 1; j >= 0; j--) {
                    JSONObject e = h.optJSONObject(j); String status = e.optString("status");
                    TextView entry = text(e.optString("date") + "  •  " + status, 13, false);
                    entry.setTextColor("Present".equalsIgnoreCase(status) ? green : ("Absent".equalsIgnoreCase(status) ? red : muted));
                    box.addView(entry); any = true;
                }
            } else {
                TextView empty = text("No records for this subject yet.", 13, false); empty.setTextColor(muted); box.addView(empty);
            }
        }
        if (!any && subjects.length() == 0) content.addView(text("No subjects or attendance records yet.", 14, false));
        content.addView(secondary("←  Back to dashboard", this::showDashboard));
    }

    private void toast(String message) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }
}
