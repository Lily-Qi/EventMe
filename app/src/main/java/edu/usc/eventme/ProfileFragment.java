package edu.usc.eventme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView userPhoto;
    private TextView userName, userEmail, userBirthday;
    private Button logOutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = profileView.findViewById(R.id.userName);
        userBirthday = profileView.findViewById(R.id.userBirthday);
        userEmail = profileView.findViewById(R.id.userEmail);
        userPhoto = profileView.findViewById(R.id.userPhoto);
        logOutBtn = profileView.findViewById(R.id.logoutButton);
        currentUser = mAuth.getCurrentUser();


        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            userBirthday.setText(user.getUserBirthday());
                            userName.setText(user.getUserName());
                            userEmail.setText(user.getUserEmail());
                            Picasso.get().load(user.getUserPhoto()).into(userPhoto);
                            logOutBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mAuth.signOut();
                                    Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                                    startActivity(mainActivity);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage(e.getMessage());
                        }
                    });

                } else {
                    userPhoto.getLayoutParams().width = 1;
                    userPhoto.setVisibility(View.INVISIBLE);
                    profileView.findViewById(R.id.eventView).getLayoutParams().height = 0;
                    profileView.findViewById(R.id.breaker2).getLayoutParams().height = 0;
                    userName.setText("Log in\nRegister events you like");
                    userName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    userBirthday.setText("");
                    userEmail.setText("");
                    logOutBtn.setText("Log In");
                    logOutBtn.setBackgroundColor(getResources().getColor(R.color.orange_theme));
                    logOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                            startActivity(mainActivity);
                        }
                    });
                }
            }
        });
        return profileView;
    }

    //show message
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }



}