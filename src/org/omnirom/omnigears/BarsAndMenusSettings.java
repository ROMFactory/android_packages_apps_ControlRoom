/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.omnirom.omnigears;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

public class BarsAndMenusSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "BarsAndMenusSettings";

    private static final String STATUS_BAR_BATTERY = "status_bar_battery";
    private static final String STATUS_BAR_NOTIF_COUNT = "status_bar_notif_count";

    private ListPreference mStatusBarBattery;
    private CheckBoxPreference mStatusBarNotifCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.bars_and_menus_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mStatusBarBattery = (ListPreference) prefSet.findPreference(STATUS_BAR_BATTERY);
        int statusBarBattery = Settings.System.getInt(resolver, Settings.System.STATUS_BAR_BATTERY, 0);
        mStatusBarBattery.setValue(String.valueOf(statusBarBattery));
        mStatusBarBattery.setSummary(mStatusBarBattery.getEntry());
        mStatusBarBattery.setOnPreferenceChangeListener(this);

        mStatusBarNotifCount = (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_NOTIF_COUNT);
        mStatusBarNotifCount.setChecked(Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_NOTIF_COUNT, 0) == 1);
        mStatusBarNotifCount.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mStatusBarBattery) {
            int statusBarBattery = Integer.valueOf((String) objValue);
            int index = mStatusBarBattery.findIndexOfValue((String) objValue);
            Settings.System.putInt(resolver, Settings.System.STATUS_BAR_BATTERY, statusBarBattery);
            mStatusBarBattery.setSummary(mStatusBarBattery.getEntries()[index]);
        } else if (preference == mStatusBarNotifCount) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(resolver, Settings.System.STATUS_BAR_NOTIF_COUNT, value ? 1 : 0);
        } else {
            return false;
        }

        return true;
    }
}

