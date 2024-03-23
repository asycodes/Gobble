package com.sutd.t4app.ui.map;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;

/**
 * A simple {@link Fragment} subclass.
// * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragmentActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragmentActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragmentActivity newInstance(String param1, String param2) {
        MapFragmentActivity fragment = new MapFragmentActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Button b = view.findViewById(R.id.showBottomSheet);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

                TextInputLayout textInputLayout1 = view1.findViewById(R.id.textFieldLayout);
                TextInputEditText editText = view1.findViewById(R.id.editText);
                Button dismissBtn = view1.findViewById(R.id.dismiss);

                dismissBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText.getText().toString().isEmpty()) {
                            textInputLayout1.setError("Please type something");
                        } else {
                            Toast.makeText(getActivity(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss (DialogInterface dialogInterface) {
                        Toast.makeText(getActivity(), "Bottom sheet dismissed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        WebView customMap=view.findViewById(R.id.webView);
        customMap.getSettings().setJavaScriptEnabled(true);
        customMap.getSettings().setDomStorageEnabled(true);
        customMap.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        customMap.loadUrl("https://www.google.com/maps/d/edit?mid=14ulTPfYFZocGg-psSoU9M4Oy2xzxB3A&usp=sharing");

        customMap.setWebViewClient(new WebViewClient());


        return view;
    }




}