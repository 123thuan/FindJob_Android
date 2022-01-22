package sict.thuan.jobadmin.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import sict.thuan.jobadmin.Model.User;
import sict.thuan.jobadmin.R;

public class EditProfileActivity extends AppCompatActivity {

    ImageView close, image_profile;
    TextView save, tv_change;
    MaterialEditText fullname, username, bio, state, sex, skill, work, experience, salary,tv_birthday, email, phone;
    FirebaseUser firebaseUser;
    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        close = findViewById(R.id.close);
        image_profile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        tv_change = findViewById(R.id.tv_change);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
        state = findViewById(R.id.state);
        tv_birthday = findViewById(R.id.tv_birthday);
        sex = findViewById(R.id.sex);
        skill = findViewById(R.id.skill);
        work = findViewById(R.id.work);
        experience = findViewById(R.id.experience);
        salary = findViewById(R.id.salary);
        email = findViewById(R.id.edit_email);
        phone = findViewById(R.id.edit_phone);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                fullname.setText(user.getFullname());
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                state.setText(user.getState());
                tv_birthday.setText(user.getBirthday());
                sex.setText(user.getSex());
                work.setText(user.getWork());
                skill.setText(user.getSkill());
                experience.setText(user.getExperience());
                salary.setText(user.getSalary());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fullname.getText().toString(),
                        username.getText().toString(),
                        bio.getText().toString(),
                        state.getText().toString(),
                        tv_birthday.getText().toString(),
                        sex.getText().toString(),
                        work.getText().toString(),
                        skill.getText().toString(),
                        experience.getText().toString(),
                        salary.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString());
                Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateProfile(String fullname, String username, String bio, String states, String birthdays, String sex, String work, String skills, String experiences, String salary, String phone, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("fullname", fullname);
        hm.put("username", username);
        hm.put("bio", bio);
        hm.put("state",states);
        hm.put("birthday",birthdays);
        hm.put("sex",sex);
        hm.put("work", work);
        hm.put("skill",skills);
        hm.put("experience",experiences);
        hm.put("salary",salary);
        hm.put("phone",phone);
        hm.put("Email",email);


        reference.updateChildren(hm);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
        } else {
            Toast.makeText(EditProfileActivity.this,"WRONG", Toast.LENGTH_SHORT).show();
        }
    }

}

