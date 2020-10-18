package ean.ecom.eanshopadmin.launching;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ean.ecom.eanshopadmin.R;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseAuth;

public class AuthActivity  extends AppCompatActivity {
    public static AppCompatActivity authActivity;
    private FrameLayout parentFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );
        authActivity = this;
        parentFrameLayout = findViewById( R.id.auth_frameLayout );

        // Default Fragment...
        setFragment( new SignInFragment() );

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
//            SignInFragment.disableCloseSignFormButton = false;
            // back key...
            firebaseAuth.signOut();
        }
        return super.onKeyDown( keyCode, event );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        firebaseAuth.signOut();
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

}
