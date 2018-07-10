package istanbul.codify.muudy.ui.compose;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialcamera.MaterialCamera;
import com.blankj.utilcode.util.StringUtils;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.CreatePostTextRequest;
import istanbul.codify.muudy.api.pojo.request.GetWordsWithFilterRequest;
import istanbul.codify.muudy.api.pojo.request.NewPostRequest;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.helper.BlurBuilder;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.SeasonSelectionEvent;
import istanbul.codify.muudy.model.event.ShareEvent;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.photo.PhotoActivity;
import istanbul.codify.muudy.ui.video.VideoActivity;
import istanbul.codify.muudy.utils.AndroidUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

final class ComposePresenter extends BasePresenter<ComposeView> {

    private static final int VIDEO_REQUEST = 243;

    private final List<Selectable> mSelecteds = new ArrayList<>();
    private final SelectedAdapter mAdapter = new SelectedAdapter(mSelecteds);
    public Uri mPhoto;
    public Uri mVideo;
    String selectedSeason = "";

    private Boolean isMediaSelected = false;
    @Override
    public void attachView(ComposeView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");

                            view.onSendClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_search))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Search clicked");
                            view.onSearchClicked();
                        }));

        mDisposables.add(
                mAdapter
                        .removeClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(selectable -> {
                            Logcat.v("Selected remove clicked");

                            mView.onSelectedRemoved(selectable);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_picture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture clicked");

                            mView.onPictureClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_cancel))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture cancelled");

                            mView.onMediaCancelClicked();
                        }));

        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setAdapter(mAdapter);
    }

    void getWordsWithFilter() {
        GetWordsWithFilterRequest request = new GetWordsWithFilterRequest(mSelecteds);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWordsWithFilter(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsWithFilterResponse>() {
                            @Override
                            protected void success(GetWordsWithFilterResponse response) {
                                mView.onLoaded(response.data.topCategories, response.data.activites);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(ArrayList<Category> categories) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        mDisposables.add(
                categoryAdapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(category -> {
                            Logcat.v("Category clicked");

                            mView.onCategoryClicked(category);
                        }));
        findViewById(R.id.compose_category_recycler, RecyclerView.class).setAdapter(categoryAdapter);
    }

    void bind(ArrayList<Category> categories, ArrayList<Activity> activities) {
        bind(categories);

        ActivityAdapter activityAdapter = new ActivityAdapter(activities, getSelectedActivity());
        mDisposables.add(
                activityAdapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(activity -> {
                            Logcat.v("Activity clicked");

                            mView.onActivityClicked(activity);
                        }));
        findViewById(R.id.compose_activity_recycler, RecyclerView.class).setAdapter(activityAdapter);

        if(mSelecteds.size() == 0){
            hideSelectedWords();
        }else{
            showSelectedWords();
        }

        if(activities.size() == 0) {
            hideActivities();
        }else{

            showActivities();
        }

        if(categories.size() == 0){
            hideCategories();
        }else{
            showCategories();
        }
    }

    void showSelectedWords(){
        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        findViewById(R.id.compose_no_selected_word, AppCompatTextView.class).setVisibility(View.INVISIBLE);
    }

    void hideSelectedWords(){
        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setVisibility(View.INVISIBLE);
        findViewById(R.id.compose_no_selected_word, AppCompatTextView.class).setVisibility(View.VISIBLE);
    }


    void showActivities(){
        findViewById(R.id.compose_activity_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        findViewById(R.id.compose_no_activity_selected, AppCompatTextView.class).setVisibility(View.INVISIBLE);
    }

    void hideActivities(){
        findViewById(R.id.compose_activity_recycler, RecyclerView.class).setVisibility(View.INVISIBLE);
        findViewById(R.id.compose_no_activity_selected, AppCompatTextView.class).setVisibility(View.VISIBLE);
    }

    void showCategories(){
        findViewById(R.id.compose_category_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        findViewById(R.id.compose_no_situation, AppCompatTextView.class).setVisibility(View.INVISIBLE);
    }

    void hideCategories(){
        findViewById(R.id.compose_category_recycler, RecyclerView.class).setVisibility(View.INVISIBLE);
        findViewById(R.id.compose_no_situation, AppCompatTextView.class).setVisibility(View.VISIBLE);
    }

    void addSelected(Selectable selectable) {
        if (!mSelecteds.contains(selectable)) {
            if (selectable instanceof Activity) {
                Iterator<Selectable> iterator = mSelecteds.iterator();
                while (iterator.hasNext()) {
                    Selectable next = iterator.next();
                    if (next instanceof Activity) {
                        iterator.remove();
                    }
                }
            }

            mSelecteds.add(selectable);
            mAdapter.notifyDataSetChanged(mSelecteds);
        }
    }

    void addSelected(List<? extends Selectable> selectables) {
        for (Selectable selectable : selectables) {
            addSelected(selectable);
        }
    }

    void removeSelected(Selectable selectable) {
        if (mSelecteds.contains(selectable)) {
            mSelecteds.remove(selectable);
            mAdapter.notifyDataSetChanged(mSelecteds);
        }
    }

    void removeSeries(){
        if (mSelecteds.size() - 1 != -1) {
            mSelecteds.remove(mSelecteds.size() - 1);
            mAdapter.notifyDataSetChanged(mSelecteds);
        }
    }

    List<Selectable> getSelecteds() {
        return mSelecteds;
    }

    void mediaClick(){
        if(getMediaType() == PostMediaType.IMAGE){
            PhotoActivity.start(mPhoto.toString(),true);
        }else if (getMediaType() == PostMediaType.VIDEO){
            VideoActivity.start(mVideo);
        }
    }

    void selectPhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<List<Uri>>>) granted -> granted ? RxGallery.gallery(activity, false).toObservable() : Observable.empty())
                        .subscribe(uris -> {
                            Uri uri = uris.get(0);
                            Logcat.v("Selected uri for photo is " + uri.toString());
                            mView.onGalleryPhotoSelected(uri);
                        })
        );
    }

    void capturePhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)

                        .flatMap((Function<Boolean, ObservableSource<Uri>>) granted -> granted ? RxGallery.photoCapture(activity).toObservable() : Observable.empty())
                        .subscribe(uri -> {
                            Logcat.v("Selected uri for photo is " + uri.toString());


                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void bindPhoto(@Nullable Uri photo) {
        mPhoto = photo;

        View picture = findViewById(R.id.compose_picture);
        File file = new File(getRealPathFromURI(photo));
        adjustImageOrientation(file);
        int height = (int)Math.round(AndroidUtils.convertDpToPixel(150,getContext()));


        Picasso
                .with(getContext())
                .load(Uri.fromFile(file))
                .resize(picture.getWidth(), height)
                .centerCrop()
                .into(findViewById(R.id.compose_picture, AppCompatImageButton.class));

        findViewById(R.id.compose_cancel).setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = picture.getLayoutParams();
        params.height = height;

        findViewById(R.id.compose_picture).setLayoutParams(params);
    }

    public void adjustImageOrientation(File _image) {

        try {
            ExifInterface exif = new ExifInterface(Uri.fromFile(_image).getEncodedPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees;
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotationInDegrees = 90;
            } else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotationInDegrees = 180;
            } else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotationInDegrees = 270;
            } else {
                rotationInDegrees = 0;
            }
            if (rotation != 0f) {
                Bitmap bitmap = BitmapFactory.decodeFile(_image.getAbsolutePath(), new BitmapFactory.Options());
                Bitmap finalBitmap = changeRotate(bitmap, rotationInDegrees);
                saveImage(finalBitmap, _image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Code: 13");
        }
    }

    public void saveImage(Bitmap finalBitmap, File _image) {
        if (_image.exists()) _image.delete();
        try {
            FileOutputStream out = new FileOutputStream(_image);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Code: 14");
        }
    }

    public Bitmap changeRotate(Bitmap bitmap, int rotationInDegrees) {

        Bitmap mBitmap = bitmap;
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationInDegrees);
            mBitmap = Bitmap.createBitmap(mBitmap , 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public File createDirAndImageFile() {
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Muudy");
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(imagesFolder, timeStamp + ".jpg");
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source;
        FileChannel destination;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    void bindGalleryPhoto(@Nullable Uri photo) {
        mPhoto = photo;

        File file = createDirAndImageFile();
        try {
            copyFile(new File(getRealPathFromURI(photo)), file);
            adjustImageOrientation(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Code: 12");
        }
        View picture = findViewById(R.id.compose_picture);

        int height = (int)Math.round(AndroidUtils.convertDpToPixel(150,getContext()));

       Picasso
                .with(getContext())
                .load(Uri.fromFile(file))
                .resize(picture.getWidth(), height)
                .centerCrop()
                .into(findViewById(R.id.compose_picture, AppCompatImageButton.class));

        //findViewById(R.id.compose_picture, AppCompatImageButton.class).setImageBitmap(BitmapFactory.decodeFile(photo.getPath()));

        findViewById(R.id.compose_cancel).setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = picture.getLayoutParams();
        params.height = height;

        findViewById(R.id.compose_picture).setLayoutParams(params);

    }

    void bindVideo(@Nullable Uri video) {
        mVideo = video;
        Bitmap videoBitmap;
        try {
            videoBitmap = ThumbnailUtils.createVideoThumbnail(video.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

            //findViewById(R.id.compose_picture, AppCompatImageButton.class).setImageBitmap(videoBitmap);

            View picture = findViewById(R.id.compose_picture);

            int height = (int)Math.round(AndroidUtils.convertDpToPixel(150,getContext()));



            findViewById(R.id.compose_picture, AppCompatImageButton.class).setScaleType(AppCompatImageView.ScaleType.CENTER_CROP);

            findViewById(R.id.compose_cancel).setVisibility(View.VISIBLE);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            ViewGroup.LayoutParams params = picture.getLayoutParams();
            params.height = height;
            params.width = width;
            findViewById(R.id.compose_picture, AppCompatImageButton.class).setImageBitmap(scaleCenterCrop(videoBitmap,150,width));

            findViewById(R.id.compose_picture,AppCompatImageView.class).setLayoutParams(params);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        findViewById(R.id.compose_cancel).setVisibility(View.VISIBLE);
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    void cancel() {
        mPhoto = null;
        mVideo = null;

        findViewById(R.id.compose_picture, AppCompatImageButton.class).setImageResource(R.drawable.ic_image_add);
        findViewById(R.id.compose_cancel).setVisibility(View.GONE);

        int height = (int)Math.round(AndroidUtils.convertDpToPixel(50,getContext()));
        ViewGroup.LayoutParams params = findViewById(R.id.compose_picture, AppCompatImageButton.class).getLayoutParams();
        params.height = height;

        findViewById(R.id.compose_picture, AppCompatImageButton.class).setLayoutParams(params);

        findViewById(R.id.compose_picture, AppCompatImageButton.class).setScaleType(AppCompatImageView.ScaleType.CENTER);

    }

    void createTextPost() {
        if (!isValid()) {
            IllegalStateException exception = new IllegalStateException("Paylaşım yapmak için durum veya aktivite seçmelisiniz");
            mView.onError(exception);
            return;
        }

        CreatePostTextRequest request = new CreatePostTextRequest(mSelecteds);
        request.token = AccountUtils.tokenLegacy(getContext());
        request.extraStringForSeries = selectedSeason;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .createPostText(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CreateTextPostResponse>() {
                            @Override
                            protected void success(CreateTextPostResponse response) {
                                View content = findViewById(R.id.compose_main_container).getRootView();

                                Bitmap bitmap = BlurBuilder.blur(content);
                                mView.onLoaded(response.data, bitmap);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mView.onError(error);
                            }
                        }));
    }

    @SuppressLint("MissingPermission")
    void post(ShareEvent event) {
        LocationServices
                .getFusedLocationProviderClient(getContext())
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null) {
                        Location location = new Location("default");
                        location.setLatitude(40.991955);
                        location.setLatitude(28.712913);
                        return location;
                    }

                    return result;
                })
                .addOnSuccessListener(location ->
                        mDisposables.add(
                                Single
                                        .just(location)
                                        .subscribeOn(Schedulers.io())
                                        .flatMap((Function<Location, SingleSource<NewPostResponse>>) point -> {
                                            NewPostRequest request = new NewPostRequest(mSelecteds);
                                            request.token = AccountUtils.token(getContext());
                                            request.text = event.sentence;
                                            request.visibility = event.visibility;
                                            request.coordinates = Coordinate.from(location);
                                            request.mediaType = getMediaType();
                                            request.mediaData = getMediaData();

                                            VideoResult video = getVideoResult();
                                            if (video != null) {
                                                request.videoThumbnailPath = video.thumbImage;
                                                request.videoUploadedPath = video.video;
                                            }

                                            return ApiManager
                                                    .getInstance()
                                                    .newPost(request)
                                                    .observeOn(AndroidSchedulers.mainThread());
                                        })
                                        .subscribe(new ServiceConsumer<NewPostResponse>() {
                                            @Override
                                            protected void success(NewPostResponse response) {
                                                Logcat.v("New post created with id " + response.data.id);

                                                mView.onLoaded(response.data);
                                            }

                                            @Override
                                            protected void error(ApiError error) {
                                                Logcat.e(error);

                                                mView.onError(error);
                                            }
                                        })))
                .addOnFailureListener(Logcat::e);
    }

    private Activity getSelectedActivity() {
        if (mSelecteds.isEmpty()) {
            return null;
        }

        for (Selectable selected : mSelecteds) {
            if (selected != null && selected instanceof Activity) {
                return (Activity) selected;
            }
        }

        return null;
    }

    private String getMediaData() {
        if (mPhoto == null) {
            return null;
        }

        try {
            InputStream input = getContext().getContentResolver().openInputStream(mPhoto);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            byte[] bytes = output.toByteArray();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            Logcat.e(e);

            return null;
        }
    }

    private VideoResult getVideoResult() {
        try {
            if (mVideo != null) {
                File file = new File(mVideo.toString());
                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("Image", file.getName(), body);

                Response<UploadVideoResponse> response =
                        ApiManager
                                .getInstance()
                                .uploadVideo(part)
                                .execute();

                if (response.body() != null) {
                    if (StringUtils.isEmpty(response.body().errMes)) {
                        return response.body().data;
                    }
                }
            }
        } catch (Exception e) {
            Logcat.e(e);
        }

        return null;
    }

    public PostMediaType getMediaType() {
        if (mPhoto != null) {
            return PostMediaType.IMAGE;
        }

        if (mVideo != null) {
            return PostMediaType.VIDEO;
        }

        return PostMediaType.NONE;
    }

    public boolean isValid() {
        for (Selectable selected : mSelecteds) {
            if (selected instanceof Activity) {
                return true;
            }

            if (selected instanceof Word) {
                return true;
            }

            if (selected instanceof User) {
                return true;
            }

            if (selected instanceof Place){
                return true;
            }
        }

        return false;
    }

    void captureVideo(AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                        .subscribe(granted -> {
                            if (!granted) {
                                return;
                            }

                            File folder = new File(getContext().getFilesDir(), "Muudy");
                            if (!folder.exists()) {
                                if (!folder.mkdirs()) {
                                    mView.onError(new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted."));
                                    return;
                                }
                            }

                            new MaterialCamera(activity)                               // Constructor takes an Activity
                                    .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                                    .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                                    .saveDir(folder)                                   // The folder recorded videos are saved to
                                    .primaryColorAttr(R.attr.colorPrimary)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
                                    .showPortraitWarning(true)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                                    .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
                                    .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                                    .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                                    .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                                    .videoEncodingBitRate(1024000)                     // Sets a custom bit rate for video recording.
                                    .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                                    .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                                    .qualityProfile(MaterialCamera.QUALITY_720P)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                                    .videoPreferredHeight(480)                      // Sets a preferred aspect ratio for the recorded video output.
                                    .maxAllowedFileSize(1024 * 1024 * 5)               // Sets a max file size of 5MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
                                    .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                                    .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                                    .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                                    .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                                    .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                                    .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                                    .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                                    .labelRetry(R.string.compose_retry)                              // Sets a custom button label for the button used to retry recording, when available
                                    .labelConfirm(R.string.compose_ok)                             // Sets a custom button label for the button used to confirm/submit a recording
                                    .showPortraitWarning(false)
                                    .countdownSeconds(10)// Same as the above, expressed with milliseconds instead of seconds.
                                    .audioDisabled(false)
                                    .countdownImmediately(false)// Set to true to record video without any audio.
                                    .start(VIDEO_REQUEST);
                        })
        );
    }

    Uri resolveVideo(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                String path = data.getDataString().replace("file://", "");
                Logcat.v("Video record path is " + path);

                if (!StringUtils.isEmpty(path)) {
                    return Uri.parse(path);
                }
            }
        }

        return null;
    }

    void openSeasonSelection(){
        View content = findViewById(R.id.compose_main_container).getRootView();

        Bitmap bitmap = BlurBuilder.blur(content);
        mView.openSeasonSelection(bitmap);
    }


    void onSeasonSelect(SeasonSelectionEvent event){
        selectedSeason = event.selectedSeasonAndEpisode;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime(2, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
