package com.spaceside.thinger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4, editText5, editText6; //numeric text fields
    ToggleButton lockButton; //button to toggle the "locked" state declared below
    Button clearButton;
    boolean locked; //state representing whether fields are accurate in proportion to one another
    double unit; //an arbitrary unit proportionate to the values entered before fields are locked
    double value1PerUnit, value2PerUnit, value3PerUnit, value4PerUnit, value5PerUnit, value6PerUnit;
    //values present in the fields when lock is toggled on
    TextWatcher watcher; //if fields are locked, will call the update method when a field is changed

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fields are not locked in relationships yet
        locked = false;

        //instantiate text fields
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);

        //Detect when the user activates the lock button, sets the lock variable
        lockButton = (ToggleButton) findViewById(R.id.lockButton);
        lockButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //if the button has been set to lock, calculate the values per gram/grams per value
                    locked = true;
                    calculate();
                }else {
                    locked = false;
                }
            }
        });

        //create and assign a textwatcher to the fields
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //when a field is edited and the lock is on, update the rest
            @Override
            public void afterTextChanged(final Editable wasEdited) {
                if (locked) {
                    update(wasEdited);
                }
            }
        };

        editText1.addTextChangedListener(watcher);
        editText2.addTextChangedListener(watcher);
        editText3.addTextChangedListener(watcher);
        editText4.addTextChangedListener(watcher);
        editText5.addTextChangedListener(watcher);
        editText6.addTextChangedListener(watcher);

        clearButton = (Button) findViewById(R.id.clear_button);

    }

    //calculate the values per one unit
    public void calculate(){
        //set units to one
        unit = 1;
        
        //retrieve values from the edittexts if they contain values
        if((!TextUtils.isEmpty(editText1.getText().toString()))
                && TextUtils.isDigitsOnly(editText1.getText().toString())){
            value1PerUnit = Double.parseDouble(editText1.getText().toString());
        }
        else {value1PerUnit = 0;}

        if((!TextUtils.isEmpty(editText2.getText().toString()))
                && TextUtils.isDigitsOnly(editText2.getText().toString())){
            value2PerUnit = Double.parseDouble(editText2.getText().toString());
        }
        else {value2PerUnit = 0;}

        if((!TextUtils.isEmpty(editText3.getText().toString()))
                && TextUtils.isDigitsOnly(editText3.getText().toString())){
            value3PerUnit = Double.parseDouble(editText3.getText().toString());
        }
        else {value3PerUnit = 0;}

        if((!TextUtils.isEmpty(editText4.getText().toString()))
                && TextUtils.isDigitsOnly(editText4.getText().toString())){
            value4PerUnit = Double.parseDouble(editText4.getText().toString());
        }
        else {value4PerUnit = 0;}

        if((!TextUtils.isEmpty(editText5.getText().toString()))
                && TextUtils.isDigitsOnly(editText5.getText().toString())){
            value5PerUnit = Double.parseDouble(editText5.getText().toString());
        }
        else {value5PerUnit = 0;}

        if((!TextUtils.isEmpty(editText6.getText().toString()))
                && TextUtils.isDigitsOnly(editText6.getText().toString())){
            value6PerUnit = Double.parseDouble(editText6.getText().toString());
        }
        else {value6PerUnit = 0;}

    }

    //round double values to the preferred number of decimal places
    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    //when a value is changed while the fields are locked, change other values to keep proportions
    public void update(Editable wasEdited){
        
        /*
        check which field was changed, calculate new number of units,
        and multiply other fields by that new number of units
         */
        if(wasEdited == editText1.getEditableText()
                && (!TextUtils.isEmpty(editText1.getText().toString()))
                && TextUtils.isDigitsOnly(editText1.getText().toString())){
            unit = (Double.parseDouble(editText1.getText().toString()))/value1PerUnit;
            editText2.setText("" + round((value2PerUnit * unit), 4));
            editText3.setText("" + round((value3PerUnit * unit), 4));
            editText4.setText("" + round((value4PerUnit * unit), 4));
            editText5.setText("" + round((value5PerUnit * unit), 4));
            editText6.setText("" + round((value6PerUnit * unit), 4));
        }
        else if (wasEdited == editText2.getEditableText()
                && (!TextUtils.isEmpty(editText2.getText().toString()))
                && TextUtils.isDigitsOnly(editText2.getText().toString())){
            unit = (Double.parseDouble(editText2.getText().toString()))/value2PerUnit;
            editText1.setText("" + round((value1PerUnit * unit), 4));
            editText3.setText("" + round((value3PerUnit * unit), 4));
            editText4.setText("" + round((value4PerUnit * unit), 4));
            editText5.setText("" + round((value5PerUnit * unit), 4));
            editText6.setText("" + round((value6PerUnit * unit), 4));
        }
        else if (wasEdited == editText3.getEditableText()
                && (!TextUtils.isEmpty(editText3.getText().toString()))
                && TextUtils.isDigitsOnly(editText3.getText().toString())){
            unit = (Double.parseDouble(editText3.getText().toString()))/value3PerUnit;
            editText1.setText("" + round((value1PerUnit * unit), 4));
            editText2.setText("" + round((value2PerUnit * unit), 4));
            editText4.setText("" + round((value4PerUnit * unit), 4));
            editText5.setText("" + round((value5PerUnit * unit), 4));
            editText6.setText("" + round((value6PerUnit * unit), 4));
        }
        else if (wasEdited == editText4.getEditableText()
                && (!TextUtils.isEmpty(editText4.getText().toString()))
                && TextUtils.isDigitsOnly(editText4.getText().toString())){
            unit = (Double.parseDouble(editText4.getText().toString()))/value4PerUnit;
            editText1.setText("" + round((value1PerUnit * unit), 4));
            editText2.setText("" + round((value2PerUnit * unit), 4));
            editText3.setText("" + round((value3PerUnit * unit), 4));
            editText5.setText("" + round((value5PerUnit * unit), 4));
            editText6.setText("" + round((value6PerUnit * unit), 4));
        }
        else if (wasEdited == editText5.getEditableText()
                && (!TextUtils.isEmpty(editText5.getText().toString()))
                && TextUtils.isDigitsOnly(editText5.getText().toString())){
            unit = (Double.parseDouble(editText5.getText().toString()))/value5PerUnit;
            editText1.setText("" + round((value1PerUnit * unit), 4));
            editText2.setText("" + round((value2PerUnit * unit), 4));
            editText3.setText("" + round((value3PerUnit * unit), 4));
            editText4.setText("" + round((value4PerUnit * unit), 4));
            editText6.setText("" + round((value6PerUnit * unit), 4));
        }
        else if (wasEdited == editText6.getEditableText()
                && (!TextUtils.isEmpty(editText6.getText().toString()))
                && TextUtils.isDigitsOnly(editText6.getText().toString())){
            unit = (Double.parseDouble(editText6.getText().toString()))/value6PerUnit;
            editText1.setText("" + round((value1PerUnit * unit), 4));
            editText2.setText("" + round((value2PerUnit * unit), 4));
            editText3.setText("" + round((value3PerUnit * unit), 4));
            editText4.setText("" + round((value4PerUnit * unit), 4));
            editText5.setText("" + round((value5PerUnit * unit), 4));
        }
    }

    public void onPause(){
        super.onPause();

        preferenceSettings = getPreferences(Activity.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        preferenceEditor.putBoolean("isLocked", locked);

        preferenceEditor.putString("units", "" + unit);

        preferenceEditor.putString("value1", editText1.getText().toString());
        preferenceEditor.putString("value2", editText2.getText().toString());
        preferenceEditor.putString("value3", editText3.getText().toString());
        preferenceEditor.putString("value4", editText4.getText().toString());
        preferenceEditor.putString("value5", editText5.getText().toString());
        preferenceEditor.putString("value6", editText6.getText().toString());
        preferenceEditor.commit();
    }

    public void onResume(){
        super.onResume();

        preferenceSettings = getPreferences(Activity.MODE_PRIVATE);

        locked = preferenceSettings.getBoolean("isLocked", false);
        lockButton.setChecked(locked);

        unit = Double.parseDouble(preferenceSettings.getString("units", "1"));

        editText1.setText(preferenceSettings.getString("value1", ""));
        editText2.setText(preferenceSettings.getString("value2", ""));
        editText3.setText(preferenceSettings.getString("value3", ""));
        editText4.setText(preferenceSettings.getString("value4", ""));
        editText5.setText(preferenceSettings.getString("value5", ""));
        editText6.setText(preferenceSettings.getString("value6", ""));
    }

    public void clear(View view){

        locked = false;
        lockButton.setChecked(false);

        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
        editText6.setText("");

        unit = 0;

        value1PerUnit = 0;
        value2PerUnit = 0;
        value3PerUnit = 0;
        value4PerUnit = 0;
        value5PerUnit = 0;
        value6PerUnit = 0;

    }
}
