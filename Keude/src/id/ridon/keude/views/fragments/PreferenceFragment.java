package id.ridon.keude.views.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.text.TextUtils;

import id.ridon.keude.Preferences;
import id.ridon.keude.PreferencesActivity;
import id.ridon.keude.R;
import id.ridon.keude.installer.CheckRootAsyncTask;
import id.ridon.keude.installer.Installer;

public class PreferenceFragment
        extends android.support.v4.preference.PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static String[] summariesToUpdate = {
        Preferences.PREF_UPD_INTERVAL,
        Preferences.PREF_UPD_WIFI_ONLY,
        Preferences.PREF_UPD_NOTIFY,
        Preferences.PREF_UPD_HISTORY,
        Preferences.PREF_ROOTED,
        Preferences.PREF_INCOMP_VER,
        Preferences.PREF_THEME,
        Preferences.PREF_PERMISSIONS,
        Preferences.PREF_COMPACT_LAYOUT,
        Preferences.PREF_IGN_TOUCH,
        Preferences.PREF_LOCAL_REPO_BONJOUR,
        Preferences.PREF_LOCAL_REPO_NAME,
        Preferences.PREF_LOCAL_REPO_HTTPS,
        Preferences.PREF_CACHE_APK,
        Preferences.PREF_EXPERT,
        Preferences.PREF_ROOT_INSTALLER,
        Preferences.PREF_SYSTEM_INSTALLER,
        Preferences.PREF_ENABLE_PROXY,
        Preferences.PREF_PROXY_HOST,
        Preferences.PREF_PROXY_PORT,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    protected void onoffSummary(String key, int on, int off) {
        CheckBoxPreference pref = (CheckBoxPreference)findPreference(key);
        if (pref.isChecked()) {
            pref.setSummary(on);
        } else {
            pref.setSummary(off);
        }
    }

    protected void entrySummary(String key) {
        ListPreference pref = (ListPreference)findPreference(key);
        pref.setSummary(pref.getEntry());
    }

    protected void textSummary(String key, int resId) {
        EditTextPreference pref = (EditTextPreference)findPreference(key);
        pref.setSummary(getString(resId, pref.getText()));
    }

    protected void updateSummary(String key, boolean changing) {

        int result = 0;

        if (key.equals(Preferences.PREF_UPD_INTERVAL)) {
            ListPreference pref = (ListPreference)findPreference(
                    Preferences.PREF_UPD_INTERVAL);
            int interval = Integer.parseInt(pref.getValue());
            Preference onlyOnWifi = findPreference(
                    Preferences.PREF_UPD_WIFI_ONLY);
            onlyOnWifi.setEnabled(interval > 0);
            if (interval == 0) {
                pref.setSummary(R.string.update_interval_zero);
            } else {
                pref.setSummary(pref.getEntry());
            }

        } else if (key.equals(Preferences.PREF_UPD_WIFI_ONLY)) {
            onoffSummary(key, R.string.automatic_scan_wifi_on,
                    R.string.automatic_scan_wifi_off);

        } else if (key.equals(Preferences.PREF_UPD_NOTIFY)) {
            onoffSummary(key, R.string.notify_on,
                R.string.notify_off);

        } else if (key.equals(Preferences.PREF_UPD_HISTORY)) {
            textSummary(key, R.string.update_history_summ);

        } else if (key.equals(Preferences.PREF_PERMISSIONS)) {
            onoffSummary(key, R.string.showPermissions_on,
                R.string.showPermissions_off);

        } else if (key.equals(Preferences.PREF_COMPACT_LAYOUT)) {
            onoffSummary(key, R.string.compactlayout_on,
                R.string.compactlayout_off);

        } else if (key.equals(Preferences.PREF_THEME)) {
            entrySummary(key);
            if (changing) {
                result |= PreferencesActivity.RESULT_RESTART;
                getActivity().setResult(result);
            }

        } else if (key.equals(Preferences.PREF_INCOMP_VER)) {
            onoffSummary(key, R.string.show_incompat_versions_on,
                R.string.show_incompat_versions_off);

        } else if (key.equals(Preferences.PREF_ROOTED)) {
            onoffSummary(key, R.string.rooted_on,
                R.string.rooted_off);

        } else if (key.equals(Preferences.PREF_IGN_TOUCH)) {
            onoffSummary(key, R.string.ignoreTouch_on,
                R.string.ignoreTouch_off);

        } else if (key.equals(Preferences.PREF_LOCAL_REPO_BONJOUR)) {
            onoffSummary(key, R.string.local_repo_bonjour_on,
                R.string.local_repo_bonjour_off);

        } else if (key.equals(Preferences.PREF_LOCAL_REPO_NAME)) {
            textSummary(key, R.string.local_repo_name_summary);

        } else if (key.equals(Preferences.PREF_LOCAL_REPO_HTTPS)) {
            onoffSummary(key, R.string.local_repo_https_on,
                R.string.local_repo_https_off);

        } else if (key.equals(Preferences.PREF_CACHE_APK)) {
            onoffSummary(key, R.string.cache_downloaded_on,
                R.string.cache_downloaded_off);

        } else if (key.equals(Preferences.PREF_EXPERT)) {
            onoffSummary(key, R.string.expert_on,
                R.string.expert_off);

        } else if (key.equals(Preferences.PREF_ROOT_INSTALLER)) {
            onoffSummary(key, R.string.root_installer_on,
                    R.string.root_installer_off);

        } else if (key.equals(Preferences.PREF_SYSTEM_INSTALLER)) {
            onoffSummary(key, R.string.system_installer_on,
                    R.string.system_installer_off);

        } else if (key.equals(Preferences.PREF_ENABLE_PROXY)) {
            CheckBoxPreference pref = (CheckBoxPreference) findPreference(key);
            pref.setSummary(R.string.enable_proxy_summary);

        } else if (key.equals(Preferences.PREF_PROXY_HOST)) {
            EditTextPreference textPref = (EditTextPreference) findPreference(key);
            String text = Preferences.get().getProxyHost();
            if (TextUtils.isEmpty(text) || text.equals(Preferences.DEFAULT_PROXY_HOST))
                textPref.setSummary(R.string.proxy_host_summary);
            else
                textPref.setSummary(text);

        } else if (key.equals(Preferences.PREF_PROXY_PORT)) {
            EditTextPreference textPref = (EditTextPreference) findPreference(key);
            int port = Preferences.get().getProxyPort();
            if (port == Preferences.DEFAULT_PROXY_PORT)
                textPref.setSummary(R.string.proxy_port_summary);
            else
                textPref.setSummary(String.valueOf(port));

        }
    }

    /**
     * Initializes RootInstaller preference. This method ensures that the preference can only be checked and persisted
     * when the user grants root access for F-Droid.
     */
    protected void initRootInstallerPreference() {
        CheckBoxPreference pref = (CheckBoxPreference) findPreference(Preferences.PREF_ROOT_INSTALLER);

        // we are handling persistence ourself!
        pref.setPersistent(false);

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                final CheckBoxPreference pref = (CheckBoxPreference) preference;

                if (pref.isChecked()) {
                    CheckRootAsyncTask checkTask = new CheckRootAsyncTask(getActivity(), new CheckRootAsyncTask.CheckRootCallback() {

                        @Override
                        public void onRootCheck(boolean rootGranted) {
                            if (rootGranted) {
                                // root access granted
                                SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                                editor.putBoolean(Preferences.PREF_ROOT_INSTALLER, true);
                                editor.commit();
                                pref.setChecked(true);
                            } else {
                                // root access denied
                                SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                                editor.putBoolean(Preferences.PREF_ROOT_INSTALLER, false);
                                editor.commit();
                                pref.setChecked(false);

                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                                alertBuilder.setTitle(R.string.root_access_denied_title);
                                alertBuilder.setMessage(getActivity().getString(R.string.root_access_denied_body));
                                alertBuilder.setNeutralButton(android.R.string.ok, null);
                                alertBuilder.create().show();
                            }
                        }
                    });
                    checkTask.execute();
                } else {
                    SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                    editor.putBoolean(Preferences.PREF_ROOT_INSTALLER, false);
                    editor.commit();
                    pref.setChecked(false);
                }

                return true;
            }
        });
    }

    /**
     * Initializes SystemInstaller preference, which can only be enabled when F-Droid is installed as a system-app
     */
    protected void initSystemInstallerPreference() {
        CheckBoxPreference pref = (CheckBoxPreference) findPreference(Preferences.PREF_SYSTEM_INSTALLER);

        // we are handling persistence ourself!
        pref.setPersistent(false);

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                final CheckBoxPreference pref = (CheckBoxPreference) preference;

                if (pref.isChecked()) {
                    if (Installer.hasSystemPermissions(getActivity(), getActivity().getPackageManager())) {
                        // system-permission are granted, i.e. F-Droid is a system-app
                        SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                        editor.putBoolean(Preferences.PREF_SYSTEM_INSTALLER, true);
                        editor.commit();
                        pref.setChecked(true);
                    } else {
                        // system-permission not available
                        SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                        editor.putBoolean(Preferences.PREF_SYSTEM_INSTALLER, false);
                        editor.commit();
                        pref.setChecked(false);

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setTitle(R.string.system_permission_denied_title);
                        alertBuilder.setMessage(getActivity().getString(R.string.system_permission_denied_body));
                        alertBuilder.setNeutralButton(android.R.string.ok, null);
                        alertBuilder.create().show();
                    }
                } else {
                    SharedPreferences.Editor editor = pref.getSharedPreferences().edit();
                    editor.putBoolean(Preferences.PREF_SYSTEM_INSTALLER, false);
                    editor.commit();
                    pref.setChecked(false);
                }

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        for (String key : summariesToUpdate) {
            updateSummary(key, false);
        }

        initRootInstallerPreference();
        initSystemInstallerPreference();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        updateSummary(key, true);
    }


}
