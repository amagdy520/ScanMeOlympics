package com.scan.me.Drawer;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.WriterException;
import com.scan.me.AboutApp;
import com.scan.me.ContactUS;
import com.scan.me.GlideApp;
import com.scan.me.QRCode;
import com.scan.me.R;
import com.scan.me.SplashScreen;
import com.scan.me.User.User;
import com.scan.me.User.UserDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    @BindView(R.id.drawer_list)
    ListView listView;//navigation List
    View view;
    private DrawerLayout mDrawerLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.image)
    ImageView image;
    private boolean isOpen;
    private User user;
    private String uid;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ButterKnife.bind(this, view);
        setList();
        return view;
    }

    private void setList() {

        final List<DrawerData> drawerDataList = new ArrayList<>();
        drawerDataList.add(new DrawerData(R.string.profile, R.drawable.ic_profile));
        drawerDataList.add(new DrawerData(R.string.share, R.drawable.ic_share));
        drawerDataList.add(new DrawerData(R.string.my_code, R.drawable.ic_qr_code));
        drawerDataList.add(new DrawerData(R.string.contact_us, R.drawable.ic_live_help));
        drawerDataList.add(new DrawerData(R.string.about, R.drawable.ic_team));
        drawerDataList.add(new DrawerData(R.string.log_out, R.drawable.ic_logout));


        DrawerListAdapter adapter = new DrawerListAdapter(getActivity(), drawerDataList);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if(user.getId()==null){
                            Toast.makeText(getActivity(), "Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent=new Intent(getActivity(), UserDetails.class);
                        Bundle bundle=new Bundle();
                        bundle.putString(UserDetails.USER_ID,user.getId());
                        startActivity(intent.putExtras(bundle));
                        break;
                    case 1:
                        share();
                        break;
                    case 2:
                        qr();
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),ContactUS.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(),AboutApp.class));
                        break;
                    case 5:
                        signOut();
                        break;
                }
            }
        });


    }

    /**
     * get Data from Main Map and set it to the Drawer
     *
     * @param userData :User Data
     */
    public void setUserData(User userData) {
        user=userData;
        uid = userData.getUid();
//
        if (user.getImage() != null) {
            GlideApp.with(getActivity())
                    .load(userData.getImage())
                    .placeholder(R.drawable.user_pic)
                    .error(R.drawable.user_pic)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.image);

        }
        this.name.setText(userData.getName());

    }

    void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), SplashScreen.class));
        getActivity().finish();
    }

    void qr() {
        if (uid == null) {
            return;
        }
        try {
            Bitmap qrCodeBitmap = QRCode.encodeAsBitmap(getActivity(), uid);
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.qr_code_dialog);
            ImageView qrCodeImageView = (ImageView) dialog.findViewById(R.id.qr_code);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
            dialog.show();
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "SHARE THE  APP");
        startActivity(Intent.createChooser(intent, "Share the app with..."));
    }


    public void setUpDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        final ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(getActivity(),
                        drawerLayout,
                        toolbar,
                        R.string.app_name,
                        R.string.app_name
                ) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        isOpen = true;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        isOpen = false;
                    }
                };
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    public class DrawerData {

        int title;
        int image;

        public DrawerData(int title, int image) {
            this.title = title;
            this.image = image;
        }

    }
}