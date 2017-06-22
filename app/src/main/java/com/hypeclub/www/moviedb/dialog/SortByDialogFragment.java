package com.hypeclub.www.moviedb.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import static com.hypeclub.www.moviedb.utilities.Preference.sortBy;

/**
 * Created by Jo on 18-Jun-17.
 */

public class SortByDialogFragment extends DialogFragment {

    SortByListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (SortByListener) context;
    }

    public interface SortByListener {
        public void sort(int which);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Sort by")
                .setItems(sortBy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.sort(which);
                    }
                });

        return builder.create();
    }
}
