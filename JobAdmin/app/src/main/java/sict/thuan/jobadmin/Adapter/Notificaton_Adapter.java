package sict.thuan.jobadmin.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import sict.thuan.jobadmin.Activities.MainActivity;
import sict.thuan.jobadmin.R;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.List;

import sict.thuan.jobadmin.Fragment.PostDetailFragment;
import sict.thuan.jobadmin.Fragment.ProfileFragment;
import sict.thuan.jobadmin.Model.Notification;
import sict.thuan.jobadmin.Model.Post;
import sict.thuan.jobadmin.Model.User;

import static android.content.Context.MODE_PRIVATE;

public class Notificaton_Adapter extends RecyclerSwipeAdapter<Notificaton_Adapter.ImageViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;
    private FirebaseUser firebaseUser;
    String mCode;

    public Notificaton_Adapter(Context context, List<Notification> notification, String code){
        mContext = context;
        mNotification = notification;
        mCode = code;
    }

    @NonNull
    @Override
    public Notificaton_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification, parent, false);
        return new Notificaton_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Notificaton_Adapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Notification notification = mNotification.get(position);
        holder.Edit.setVisibility(View.VISIBLE);
        isFollowing(notification.getUserid(), holder.Edit);
        holder.Delete.setVisibility(View.VISIBLE);
        getUserInfo(holder.image_profile, holder.username, notification.getUserid());

        if (notification.isIspost()) {
            holder.post_image.setVisibility(View.VISIBLE);
            getPostImage(holder.post_image, notification.getPostid());
        } else {
            holder.post_image.setVisibility(View.GONE);
        }if (notification.getUserid().equals(firebaseUser.getUid())){
            holder.Edit.setVisibility(View.GONE);
        }
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wrapper1));
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wraper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isIspost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("postid", notification.getPostid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();
                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", notification.getUserid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });
        holder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", notification.getUserid());
                mContext.startActivity(intent);

            }
        });
        holder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on Share " + holder.username.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Edit.getText().toString().equals("confirm")) {
                    FirebaseDatabase.getInstance().getReference().child("confirm").child(firebaseUser.getUid())
                            .child("cancel").child(notification.getUserid()).setValue(true);

                    addNotification(notification.getUserid());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("confirm").child(firebaseUser.getUid())
                            .child("cancel").child(notification.getUserid()).removeValue();

                }
            }

        });

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.create();
                alertBuilder.setMessage("Bạn chắc chắn muốn xóa");
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeIteminView(notification);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                alertBuilder.show();
            }
        });

        mItemManger.bindView(holder.itemView, position);
    }

    private void removeIteminView(Notification notification){
        int curentPosition=mNotification.indexOf(notification);
        mNotification.remove(curentPosition);
        DatabaseReference nodeRoot= FirebaseDatabase.getInstance().getReference("Notifications");
        nodeRoot.child("thua").removeValue();
        notifyItemRemoved(curentPosition);
    }

    private void addNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "đã xác nhận yêu cầu của bạn");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image;
        public TextView username, text;
        public SwipeLayout swipeLayout;
        public TextView Delete;
        public Button Edit;
        public TextView Share;
        public ImageButton btnLocation;

        public ImageViewHolder(View itemView) {
            super(itemView);

            swipeLayout =  itemView.findViewById(R.id.swipe);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
            Delete =  itemView.findViewById(R.id.Delete);
            Edit = itemView.findViewById(R.id.Edit);
            Share =  itemView.findViewById(R.id.Share);
            btnLocation = itemView.findViewById(R.id.btnLocation);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostImage(final ImageView post_image, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getPostimage()).into(post_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void isFollowing(final String userid, final Button button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("confirm").child(firebaseUser.getUid()).child("cancel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("cancel");
                } else{
                    button.setText("confirm");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}