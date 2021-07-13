package com.example.pixelwallpaper.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelwallpaper.FullScreenWallpaper;
import com.example.pixelwallpaper.Model.Post;
import com.example.pixelwallpaper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPost;

    public PhotoAdapter() {
    }

    public PhotoAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item,parent,false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Post post = mPost.get(position);
        Picasso.get().load(post.getImageUrl()).placeholder(R.drawable.postunavalable).into(holder.postImage);
        holder.btnDel.setVisibility(View.VISIBLE);

        if(!post.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.btnDel.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.startActivity(new Intent(mContext, FullScreenWallpaper.class)
                        .putExtra("originalUrl",mPost.get(position).getImageUrl()));

            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FancyGifDialog.Builder(mContext).setTitle("Delete Post").setMessage("Are you sure to delete this post")
                        .setNegativeBtnText("Cancel").setPositiveBtnBackground(R.color.red) // or pass it like R.color.positiveButton
                        .setPositiveBtnText("Ok") // or pass it like android.R.string.ok
                        .setNegativeBtnBackground(R.color.grey).isCancellable(true).OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        FirebaseStorage.getInstance().getReferenceFromUrl(post.getImageUrl()).delete();
                        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getPostId()).removeValue();
                        Toast.makeText(mContext, "Post Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                }).build();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView postImage;
        public ImageButton btnDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
            btnDel = itemView.findViewById(R.id.btn_del);
        }
    }


}
