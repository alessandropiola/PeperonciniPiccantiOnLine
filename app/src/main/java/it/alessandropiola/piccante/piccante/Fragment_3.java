package it.alessandropiola.piccante.piccante;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;

import it.alessandropiola.piccante.piccante.R;
import it.alessandropiola.piccante.piccante.util.IabHelper;
import it.alessandropiola.piccante.piccante.util.IabResult;
import it.alessandropiola.piccante.piccante.util.Purchase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_3#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Fragment_3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    IabHelper mHelper = null;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_3 newInstance(String param1, String param2) {
        Fragment_3 fragment = new Fragment_3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment_3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg2fQNkOEDdj3uuDpBYlKlxGmEoy+TNhGxMujX+3971AgxEj5mGlzhhBTTkdU55pbbbVX1rU6Vsp7jxDB8QW+7Bm8zwQ8wS8n80EvMw9hnpEk5O8tBbXUFt+dyut9nFmSIx8ZFH04IfwPtvJIebwYOVSjYGova+AgPSEzzvjqbLVx58zMLeriBmshZ+sWsEc8VMZMY8sQuJgKWqDtcKzChK1DyGVkgmq9wnts4OQdh2cfBzgoNNG2yZqI2ZUSJlw5WHQ6FyHNqGDpBlcCh2YqqxQPUlVP3p5J1MthBbiuVlkWZuJQKK2yxCr/2lsm1bTKb78CBZ4nEP2N/FYVEWtGNQIDAQAB";

        mHelper = new IabHelper(getActivity().getApplicationContext(), base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d("TAG", "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d("TAG", "In-app Billing is set up OK");
                                           }
                                       }
                                   });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_3, container, false);
        Button bBuy = (Button) view.findViewById(R.id.buttonMolto);
        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mHelper.launchPurchaseFlow(getActivity(), "piccante3", 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");

            }

        });

        return view;
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals("piccante3")) {
                //consumeItem();
                //buyButton.setEnabled(false);
            }



        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

}
