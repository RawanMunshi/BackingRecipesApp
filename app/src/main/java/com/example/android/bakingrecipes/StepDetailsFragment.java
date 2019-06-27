package com.example.android.bakingrecipes;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipes.Models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment {

    private Step stepDetails;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private TextView stepFullDescriptionTv;
    private ImageView imageView;

    private PreviousButtonOnClickListener previousButtonCallback;
    private NextButtonOnClickListener nextButtonCallback;

    public interface PreviousButtonOnClickListener {
        void onPreviousButtonClicked(int id);
    }

    public interface NextButtonOnClickListener {
        void onNextButtonClicked(int id);
    }

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null){
            stepDetails = (Step) savedInstanceState.getParcelable("step");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);


        stepFullDescriptionTv = view.findViewById(R.id.step_full_description);

        // if the phone is portrait
        if(stepFullDescriptionTv != null) {
            stepFullDescriptionTv.setText(stepDetails.getDescription());
            Button previousButton = view.findViewById(R.id.previous_button);
            Button nextButton = view.findViewById(R.id.next_button);

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (previousButtonCallback != null) {
                        int id = stepDetails.getId();
                        previousButtonCallback.onPreviousButtonClicked(id);
                    }
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = stepDetails.getId();
                    nextButtonCallback.onNextButtonClicked(id);
                }
            });
        }

        String stepVideo = stepDetails.getVideoURL();
        String stepImage = stepDetails.getThumbnailURL();

        playerView  = (PlayerView) view.findViewById(R.id.playerView);
        imageView = view.findViewById(R.id.imageView);

        // check if the media is a video or an image
        if(isVideoURL(stepVideo.toLowerCase())){
            Uri videoUri = Uri.parse(stepVideo);
            initializePlayer(videoUri);
        }
        else if (isVideoURL(stepImage.toLowerCase())) {
            Uri videoUri = Uri.parse(stepImage);
            initializePlayer(videoUri);
        }
        else if(isImageURL(stepImage.toLowerCase())){
            displayImage(stepImage);
        } else {
            imageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    /**
     * Function used to load and display the image
     * @param imageUrl
     */
    public void displayImage (String imageUrl){
        imageView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.INVISIBLE);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.nomedia)
                .error(R.drawable.nomedia)
                .into(imageView);
    }

    /**
     * Function used to check if the url is for a video
     * @param videoURL
     * @return
     */
    public boolean isVideoURL (final String videoURL){
        boolean isVideo =   videoURL.endsWith(".mp4") ||
                            videoURL.endsWith(".wav") ||
                            videoURL.endsWith(".flv") ;
        return isVideo;
    }

    /**
     * Function used to check if the url is for an image
     * @param imageURL
     * @return
     */
    public boolean isImageURL (final String imageURL){
        boolean isImage =   imageURL.endsWith(".png") ||
                imageURL.endsWith(".jpg");
        return isImage;
    }

    /**
     * Function used to initialize the video player
     * @param videoUri
     */
    private void initializePlayer(Uri videoUri) {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingRecipes");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(exoPlayer != null) {
            releasePlayer();
        }
    }

    public Step getStepDetails() {
        return stepDetails;
    }

    public void setStepDetails(Step stepDetails) {
        this.stepDetails = stepDetails;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("step",stepDetails);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            previousButtonCallback = (PreviousButtonOnClickListener) context;
            nextButtonCallback = (NextButtonOnClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(this.toString());
        }
    }
}
