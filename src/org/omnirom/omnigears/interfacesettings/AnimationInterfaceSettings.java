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

package org.omnirom.omnigears.interfacesettings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.ViewConfiguration;
import android.util.Log;

import org.omnirom.omnigears.ui.AnimBarPreference;
import org.omnirom.omnigears.ui.FlingBarPreference;
import org.omnirom.omnigears.ui.ScrollBarPreference;
import org.omnirom.omnigears.ui.OverScrollBarPreference;
import org.omnirom.omnigears.ui.OverFlingBarPreference;
import org.omnirom.omnigears.ui.ListViewBarPreference;
import org.omnirom.omnigears.ui.ImeBarPreference;
import com.android.internal.util.aokp.AwesomeAnimationHelper;

import java.util.Arrays;

public class AnimationInterfaceSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "AnimationInterfaceSettings";

    private static final String ACTIVITY_OPEN = "activity_open";
    private static final String ACTIVITY_CLOSE = "activity_close";
    private static final String TASK_OPEN = "task_open";
    private static final String TASK_CLOSE = "task_close";
    private static final String TASK_MOVE_TO_FRONT = "task_move_to_front";
    private static final String TASK_MOVE_TO_BACK = "task_move_to_back";
    private static final String ANIMATION_DURATION = "animation_duration";
    private static final String ANIMATION_NO_OVERRIDE = "animation_no_override";
    private static final String WALLPAPER_OPEN = "wallpaper_open";
    private static final String WALLPAPER_CLOSE = "wallpaper_close";
    private static final String WALLPAPER_INTRA_OPEN = "wallpaper_intra_open";
    private static final String WALLPAPER_INTRA_CLOSE = "wallpaper_intra_close";
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";
    private static final String LISTVIEW_ANIM_DURATION = "listview_anim_duration";

    private static final String ANIMATION_FLING_VELOCITY = "animation_fling_velocity";
    private static final String ANIMATION_SCROLL_FRICTION = "animation_scroll_friction";
    private static final String ANIMATION_OVERSCROLL_DISTANCE = "animation_overscroll_distance";
    private static final String ANIMATION_OVERFLING_DISTANCE = "animation_overfling_distance";
    private static final float MULTIPLIER_SCROLL_FRICTION = 670f;
    private static final int MULTIPLIER_FLING = 100;

    private static final String IME_ENTER_ANIMATION = "ime_enter_animation";
    private static final String IME_EXIT_ANIMATION = "ime_exit_animation";
    private static final String IME_INTERPOLATOR = "ime_interpolator";
    private static final String IME_ANIM_DURATION = "ime_anim_duration";

    private ListPreference mActivityOpenPref;
    private ListPreference mActivityClosePref;
    private ListPreference mTaskOpenPref;
    private ListPreference mTaskClosePref;
    private ListPreference mTaskMoveToFrontPref;
    private ListPreference mTaskMoveToBackPref;
    private ListPreference mWallpaperOpen;
    private ListPreference mWallpaperClose;
    private ListPreference mWallpaperIntraOpen;
    private ListPreference mWallpaperIntraClose;
    private AnimBarPreference mAnimationDuration;
    private ListViewBarPreference mListViewDuration;
    private CheckBoxPreference mAnimNoOverride;
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    private FlingBarPreference mAnimationFling;
    private ScrollBarPreference mAnimationScroll;
    private OverScrollBarPreference mAnimationOverScroll;
    private OverFlingBarPreference mAnimationOverFling;

    private ListPreference mAnimationImeEnter;
    private ListPreference mAnimationImeExit;
    private ListPreference mAnimationImeInterpolator;
    private ImeBarPreference mAnimationImeDuration;

    private int[] mAnimations;
    private String[] mAnimationsStrings;
    private String[] mAnimationsNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.animation_interface_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mAnimations = AwesomeAnimationHelper.getAnimationsList();
        int animqty = mAnimations.length;
        mAnimationsStrings = new String[animqty];
        mAnimationsNum = new String[animqty];
        for (int i = 0; i < animqty; i++) {
            mAnimationsStrings[i] = AwesomeAnimationHelper.getProperName(getActivity().getResources(), mAnimations[i]);
            mAnimationsNum[i] = String.valueOf(mAnimations[i]);
        }

        mAnimNoOverride = (CheckBoxPreference) prefSet.findPreference(ANIMATION_NO_OVERRIDE);
        mAnimNoOverride.setChecked(Settings.System.getInt(resolver,
                Settings.System.ANIMATION_CONTROLS_NO_OVERRIDE, 0) == 1);
        mAnimNoOverride.setOnPreferenceChangeListener(this);

        mActivityOpenPref = (ListPreference) prefSet.findPreference(ACTIVITY_OPEN);
        mActivityOpenPref.setOnPreferenceChangeListener(this);
        if (getProperVal(mActivityOpenPref) != null) {
             mActivityOpenPref.setValue(getProperVal(mActivityOpenPref));
             mActivityOpenPref.setSummary(getProperSummary(mActivityOpenPref));
        }
        mActivityOpenPref.setEntries(mAnimationsStrings);
        mActivityOpenPref.setEntryValues(mAnimationsNum);

        mActivityClosePref = (ListPreference) prefSet.findPreference(ACTIVITY_CLOSE);
        mActivityClosePref.setOnPreferenceChangeListener(this);
        if (getProperVal(mActivityClosePref) != null) {
             mActivityClosePref.setValue(getProperVal(mActivityClosePref));
             mActivityClosePref.setSummary(getProperSummary(mActivityClosePref));
        }
        mActivityClosePref.setEntries(mAnimationsStrings);
        mActivityClosePref.setEntryValues(mAnimationsNum);

        mTaskOpenPref = (ListPreference) prefSet.findPreference(TASK_OPEN);
        mTaskOpenPref.setOnPreferenceChangeListener(this);
        if (getProperVal(mTaskOpenPref) != null) {
             mTaskOpenPref.setValue(getProperVal(mTaskOpenPref));
             mTaskOpenPref.setSummary(getProperSummary(mTaskOpenPref));
        }
        mTaskOpenPref.setEntries(mAnimationsStrings);
        mTaskOpenPref.setEntryValues(mAnimationsNum);

        mTaskClosePref = (ListPreference) prefSet.findPreference(TASK_CLOSE);
        mTaskClosePref.setOnPreferenceChangeListener(this);
        if (getProperVal(mTaskClosePref) != null) {
             mTaskClosePref.setValue(getProperVal(mTaskClosePref));
             mTaskClosePref.setSummary(getProperSummary(mTaskClosePref));
        }
        mTaskClosePref.setEntries(mAnimationsStrings);
        mTaskClosePref.setEntryValues(mAnimationsNum);

        mTaskMoveToFrontPref = (ListPreference) prefSet.findPreference(TASK_MOVE_TO_FRONT);
        mTaskMoveToFrontPref.setOnPreferenceChangeListener(this);
        if (getProperVal(mTaskMoveToFrontPref) != null) {
             mTaskMoveToFrontPref.setValue(getProperVal(mTaskMoveToFrontPref));
             mTaskMoveToFrontPref.setSummary(getProperSummary(mTaskMoveToFrontPref));
        }
        mTaskMoveToFrontPref.setEntries(mAnimationsStrings);
        mTaskMoveToFrontPref.setEntryValues(mAnimationsNum);

        mTaskMoveToBackPref = (ListPreference) prefSet.findPreference(TASK_MOVE_TO_BACK);
        mTaskMoveToBackPref.setOnPreferenceChangeListener(this);
        if (getProperVal(mTaskMoveToBackPref) != null) {
             mTaskMoveToBackPref.setValue(getProperVal(mTaskMoveToBackPref));
             mTaskMoveToBackPref.setSummary(getProperSummary(mTaskMoveToBackPref));
        }
        mTaskMoveToBackPref.setEntries(mAnimationsStrings);
        mTaskMoveToBackPref.setEntryValues(mAnimationsNum);

        mWallpaperOpen = (ListPreference) prefSet.findPreference(WALLPAPER_OPEN);
        mWallpaperOpen.setOnPreferenceChangeListener(this);
        if (getProperVal(mWallpaperOpen) != null) {
             mWallpaperOpen.setValue(getProperVal(mWallpaperOpen));
             mWallpaperOpen.setSummary(getProperSummary(mWallpaperOpen));
        }
        mWallpaperOpen.setEntries(mAnimationsStrings);
        mWallpaperOpen.setEntryValues(mAnimationsNum);

        mWallpaperClose = (ListPreference) prefSet.findPreference(WALLPAPER_CLOSE);
        mWallpaperClose.setOnPreferenceChangeListener(this);
        if (getProperVal(mWallpaperClose) != null) {
             mWallpaperClose.setValue(getProperVal(mWallpaperClose));
             mWallpaperClose.setSummary(getProperSummary(mWallpaperClose));
        }
        mWallpaperClose.setEntries(mAnimationsStrings);
        mWallpaperClose.setEntryValues(mAnimationsNum);

        mWallpaperIntraOpen = (ListPreference) prefSet.findPreference(WALLPAPER_INTRA_OPEN);
        mWallpaperIntraOpen.setOnPreferenceChangeListener(this);
        if (getProperVal(mWallpaperIntraOpen) != null) {
             mWallpaperIntraOpen.setValue(getProperVal(mWallpaperIntraOpen));
             mWallpaperIntraOpen.setSummary(getProperSummary(mWallpaperIntraOpen));
        }
        mWallpaperIntraOpen.setEntries(mAnimationsStrings);
        mWallpaperIntraOpen.setEntryValues(mAnimationsNum);

        mWallpaperIntraClose = (ListPreference) prefSet.findPreference(WALLPAPER_INTRA_CLOSE);
        mWallpaperIntraClose.setOnPreferenceChangeListener(this);
        if (getProperVal(mWallpaperIntraClose) != null) {
             mWallpaperIntraClose.setValue(getProperVal(mWallpaperIntraClose));
             mWallpaperIntraClose.setSummary(getProperSummary(mWallpaperIntraClose));
        }
        mWallpaperIntraClose.setEntries(mAnimationsStrings);
        mWallpaperIntraClose.setEntryValues(mAnimationsNum);

        int defaultDuration = Settings.System.getInt(resolver,
                Settings.System.ANIMATION_CONTROLS_DURATION, 0);
        mAnimationDuration = (AnimBarPreference) prefSet.findPreference(ANIMATION_DURATION);
        mAnimationDuration.setInitValue((int) (defaultDuration));
        mAnimationDuration.setOnPreferenceChangeListener(this);

        mListViewAnimation = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_ANIMATION);
        if (getProperVal(mListViewAnimation) != null) {
             mListViewAnimation.setValue(getProperVal(mListViewAnimation));
             mListViewAnimation.setSummary(getListAnimationName(Integer.valueOf(getProperVal(mListViewAnimation))));
        }
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_INTERPOLATOR);
        if (getProperVal(mListViewInterpolator) != null) {
             mListViewInterpolator.setValue(getProperVal(mListViewInterpolator));
             mListViewInterpolator.setSummary(getListInterpolatorName(Integer.valueOf(getProperVal(mListViewInterpolator))));
        }
        mListViewInterpolator.setOnPreferenceChangeListener(this);

        int listviewDuration = Settings.System.getInt(resolver,
                Settings.System.LISTVIEW_DURATION, 500);
        mListViewDuration = (ListViewBarPreference) prefSet.findPreference(LISTVIEW_ANIM_DURATION);
        mListViewDuration.setInitValue((int) (listviewDuration));
        mListViewDuration.setOnPreferenceChangeListener(this);

        float defaultScroll = Settings.System.getFloat(resolver,
                Settings.System.CUSTOM_SCROLL_FRICTION, ViewConfiguration.DEFAULT_SCROLL_FRICTION);
        mAnimationScroll = (ScrollBarPreference) prefSet.findPreference(ANIMATION_SCROLL_FRICTION);
        mAnimationScroll.setInitValue((int) (defaultScroll * MULTIPLIER_SCROLL_FRICTION));
        mAnimationScroll.setOnPreferenceChangeListener(this);

        int defaultFling = Settings.System.getInt(resolver,
                Settings.System.CUSTOM_FLING_VELOCITY, ViewConfiguration.DEFAULT_MAXIMUM_FLING_VELOCITY);
        mAnimationFling = (FlingBarPreference) prefSet.findPreference(ANIMATION_FLING_VELOCITY);
        mAnimationFling.setInitValue((int) (defaultFling / MULTIPLIER_FLING));
        mAnimationFling.setOnPreferenceChangeListener(this);

        int defaultOverScroll = Settings.System.getInt(resolver,
                Settings.System.CUSTOM_OVERSCROLL_DISTANCE, ViewConfiguration.DEFAULT_OVERSCROLL_DISTANCE);
        mAnimationOverScroll = (OverScrollBarPreference) prefSet.findPreference(ANIMATION_OVERSCROLL_DISTANCE);
        mAnimationOverScroll.setInitValue((int) (defaultOverScroll));
        mAnimationOverScroll.setOnPreferenceChangeListener(this);

        int defaultOverFling = Settings.System.getInt(resolver,
                Settings.System.CUSTOM_OVERFLING_DISTANCE, ViewConfiguration.DEFAULT_OVERFLING_DISTANCE);
        mAnimationOverFling = (OverFlingBarPreference) prefSet.findPreference(ANIMATION_OVERFLING_DISTANCE);
        mAnimationOverFling.setInitValue((int) (defaultOverFling));
        mAnimationOverFling.setOnPreferenceChangeListener(this);

        mAnimationImeEnter = (ListPreference) prefSet.findPreference(IME_ENTER_ANIMATION);
        mAnimationImeEnter.setOnPreferenceChangeListener(this);
        if (getProperVal(mAnimationImeEnter) != null) {
             mAnimationImeEnter.setValue(getProperVal(mAnimationImeEnter));
             mAnimationImeEnter.setSummary(getProperSummary(mAnimationImeEnter));
        }
        mAnimationImeEnter.setEntries(mAnimationsStrings);
        mAnimationImeEnter.setEntryValues(mAnimationsNum);

        mAnimationImeExit = (ListPreference) prefSet.findPreference(IME_EXIT_ANIMATION);
        mAnimationImeExit.setOnPreferenceChangeListener(this);
        if (getProperVal(mAnimationImeExit) != null) {
             mAnimationImeExit.setValue(getProperVal(mAnimationImeExit));
             mAnimationImeExit.setSummary(getProperSummary(mAnimationImeExit));
        }
        mAnimationImeExit.setEntries(mAnimationsStrings);
        mAnimationImeExit.setEntryValues(mAnimationsNum);

        mAnimationImeInterpolator = (ListPreference) prefSet.findPreference(IME_INTERPOLATOR);
        if (getProperVal(mAnimationImeInterpolator) != null) {
             mAnimationImeInterpolator.setValue(getProperVal(mAnimationImeInterpolator));
             mAnimationImeInterpolator.setSummary(getListInterpolatorName(Integer.valueOf(getProperVal(mAnimationImeInterpolator))));
        }
        mAnimationImeInterpolator.setOnPreferenceChangeListener(this);

        int imeDuration = Settings.System.getInt(resolver,
                Settings.System.ANIMATION_IME_DURATION, 500);
        mAnimationImeDuration = (ImeBarPreference) prefSet.findPreference(IME_ANIM_DURATION);
        mAnimationImeDuration.setInitValue(imeDuration);
        mAnimationImeDuration.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mAnimNoOverride) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(resolver, Settings.System.ANIMATION_CONTROLS_NO_OVERRIDE, value ? 1 : 0);
        } else if (preference == mListViewAnimation) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver, Settings.System.LISTVIEW_ANIMATION, val);
            mListViewAnimation.setSummary(getListAnimationName(val));
        } else if (preference == mListViewInterpolator) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver, Settings.System.LISTVIEW_INTERPOLATOR, val);
            mListViewInterpolator.setSummary(getListInterpolatorName(val));
        } else if (preference == mActivityOpenPref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[0], val);
            mActivityOpenPref.setSummary(getProperSummary(mActivityOpenPref));
        } else if (preference == mActivityClosePref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[1], val);
            mActivityClosePref.setSummary(getProperSummary(mActivityClosePref));
        } else if (preference == mTaskOpenPref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[2], val);
            mTaskOpenPref.setSummary(getProperSummary(mTaskOpenPref));
        } else if (preference == mTaskClosePref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[3], val);
            mTaskClosePref.setSummary(getProperSummary(mTaskClosePref));
        } else if (preference == mTaskMoveToFrontPref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[4], val);
            mTaskMoveToFrontPref.setSummary(getProperSummary(mTaskMoveToFrontPref));
        } else if (preference == mTaskMoveToBackPref) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[5], val);
            mTaskMoveToBackPref.setSummary(getProperSummary(mTaskMoveToBackPref));
        } else if (preference == mWallpaperOpen) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[6], val);
            mWallpaperOpen.setSummary(getProperSummary(mWallpaperOpen));
        } else if (preference == mWallpaperClose) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[7], val);
            mWallpaperClose.setSummary(getProperSummary(mWallpaperClose));
        } else if (preference == mWallpaperIntraOpen) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[8], val);
            mWallpaperIntraOpen.setSummary(getProperSummary(mWallpaperIntraOpen));
        } else if (preference == mWallpaperIntraClose) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[9], val);
            mWallpaperIntraClose.setSummary(getProperSummary(mWallpaperIntraClose));
        } else if (preference == mAnimationDuration) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_CONTROLS_DURATION,
                    val);
        } else if (preference == mAnimationScroll) {
            int val = Integer.parseInt((String) objValue);
            if (val > 0) {
                Settings.System.putFloat(resolver,
                   Settings.System.CUSTOM_SCROLL_FRICTION,
                   ((float) (val / MULTIPLIER_SCROLL_FRICTION)));
            } else {
                Settings.System.putFloat(resolver,
                    Settings.System.CUSTOM_SCROLL_FRICTION,
                    ViewConfiguration.DEFAULT_SCROLL_FRICTION);
            }
        } else if (preference == mAnimationFling) {
            int val = Integer.parseInt((String) objValue);
            if (val > 0) {
                Settings.System.putInt(resolver,
                    Settings.System.CUSTOM_FLING_VELOCITY,
                    (val * MULTIPLIER_FLING));
            } else {
                Settings.System.putInt(resolver,
                    Settings.System.CUSTOM_FLING_VELOCITY,
                    ViewConfiguration.DEFAULT_MAXIMUM_FLING_VELOCITY);
            }
        } else if (preference == mAnimationOverScroll) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.CUSTOM_OVERSCROLL_DISTANCE,
                    val);
        } else if (preference == mAnimationOverFling) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.CUSTOM_OVERFLING_DISTANCE,
                    val);
        } else if (preference == mListViewDuration) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.LISTVIEW_DURATION,
                    val);
        } else if (preference == mAnimationImeDuration) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_IME_DURATION,
                    val);
        } else if (preference == mAnimationImeEnter) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_IME_ENTER,
                    val);
            mAnimationImeEnter.setSummary(getProperSummary(mAnimationImeEnter));
        } else if (preference == mAnimationImeExit) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_IME_EXIT,
                    val);
            mAnimationImeExit.setSummary(getProperSummary(mAnimationImeExit));
        } else if (preference == mAnimationImeInterpolator) {
            int val = Integer.parseInt((String) objValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_IME_INTERPOLATOR,
                    val);
            mAnimationImeInterpolator.setSummary(getListInterpolatorName(val));
        } else {
            return false;
        }
        return true;
    }

    private String getProperSummary(Preference preference) {
        String mString = "";
        if (preference == mActivityOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[0];
        } else if (preference == mActivityClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[1];
        } else if (preference == mTaskOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[2];
        } else if (preference == mTaskClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[3];
        } else if (preference == mTaskMoveToFrontPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[4];
        } else if (preference == mTaskMoveToBackPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[5];
        } else if (preference == mWallpaperOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[6];
        } else if (preference == mWallpaperClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[7];
        } else if (preference == mWallpaperIntraOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[8];
        } else if (preference == mWallpaperIntraClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[9];
        } else if (preference == mAnimationImeEnter) {
            mString = Settings.System.ANIMATION_IME_ENTER;
        } else if (preference == mAnimationImeExit) {
            mString = Settings.System.ANIMATION_IME_EXIT;
        }

        String mNum = Settings.System.getString(getActivity().getContentResolver(), mString);
        return AwesomeAnimationHelper.getProperName(getActivity().getResources(), Integer.valueOf(mNum));
    }

    private String getProperVal(Preference preference) {
        String mString = "";
        if (preference == mActivityOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[0];
        } else if (preference == mActivityClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[1];
        } else if (preference == mTaskOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[2];
        } else if (preference == mTaskClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[3];
        } else if (preference == mTaskMoveToFrontPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[4];
        } else if (preference == mTaskMoveToBackPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[5];
        } else if (preference == mWallpaperOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[6];
        } else if (preference == mWallpaperClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[7];
        } else if (preference == mWallpaperIntraOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[8];
        } else if (preference == mWallpaperIntraClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[9];
        } else if (preference == mListViewAnimation) {
            mString = Settings.System.LISTVIEW_ANIMATION;
        } else if (preference == mListViewInterpolator) {
            mString = Settings.System.LISTVIEW_INTERPOLATOR;
        } else if (preference == mAnimationImeEnter) {
            mString = Settings.System.ANIMATION_IME_ENTER;
        } else if (preference == mAnimationImeExit) {
            mString = Settings.System.ANIMATION_IME_EXIT;
        } else if (preference == mAnimationImeInterpolator) {
            mString = Settings.System.ANIMATION_IME_INTERPOLATOR;
        }

        return Settings.System.getString(getActivity().getContentResolver(), mString);
    }

    private String getListAnimationName(int index) {
    	String[] str = getActivity().getResources().getStringArray(R.array.listview_animation_entries);
    	return str[index];
    }

    private String getListInterpolatorName(int index) {
    	String[] str = getActivity().getResources().getStringArray(R.array.listview_interpolator_entries);
    	return str[index];
    }

}
