package com.example.habittracker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;




public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
String input1;
String input2;
String input3;
    private OnBottomSheetDismissListener dismissListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        // Find your UI components for the original EditText
       // EditText userInputEditText = view.findViewById(R.id.userInputEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Find your new UI components for TextView1, TextView2, and TextView3
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);

        EditText editText1 = view.findViewById(R.id.editText1);
        EditText editText2 = view.findViewById(R.id.editText2);
        EditText editText3 = view.findViewById(R.id.editText3);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the save button click event
                input1 = editText1.getText().toString();
                input2 = editText2.getText().toString();
                input3 = editText3.getText().toString();

                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("input1", input1);
                editor.putString("input2", input2);
                editor.putString("input3", input3);
                editor.apply();

                // Dismiss the bottom sheet
                dismiss();



            }
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }
    public void setOnBottomSheetDismissListener(OnBottomSheetDismissListener listener) {
        dismissListener = listener;
    }
    public interface OnBottomSheetDismissListener {
        void onDismiss();
    }
}

