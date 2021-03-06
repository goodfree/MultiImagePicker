package net.yazeed44.multiimagepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.yazeed44.imagepicker.PickerActivity;
import net.yazeed44.imagepicker.sample.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickPick(View view) {

        pickImages();
    }


    private void pickImages() {
        final Intent pickIntent = new Intent(this, PickerActivity.class);
        pickIntent.putExtra(PickerActivity.LIMIT_KEY, 6); // Set a limit

        startActivityForResult(pickIntent, PickerActivity.PICK_REQUEST); //Open gallery
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {

        final Spanned aboutBody = Html.fromHtml(getResources().getString(R.string.about_body_html));

        new MaterialDialog.Builder(this)
                .title(R.string.about_title)
                .content(aboutBody)
                .contentLineSpacing(1.6f)
                .show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, requestCode, data);
        if (requestCode == PickerActivity.PICK_REQUEST && resultCode == RESULT_OK) {
            //No problemo

            final String[] paths = data.getStringArrayExtra(PickerActivity.PICKED_IMAGES_KEY);//Paths for chosen images (Organized)

            //Do what you want with paths

            setupImageSamples(paths);
            for (String path : paths) {
                Log.d("onActivityResult", path);
            }
        } else {
            //There was a problem
        }
    }


    private void setupImageSamples(final String[] paths) {
        final GridView gridView = (GridView) findViewById(R.id.images_sample);

        gridView.setAdapter(new SamplesAdapter(paths, this));


    }


    private class SamplesAdapter extends BaseAdapter {
        private String[] paths;
        private Activity activity;

        public SamplesAdapter(final String[] paths, final Activity activity) {
            this.paths = paths;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return paths.length;
        }

        @Override
        public Object getItem(int position) {
            return paths[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String path = paths[position];
            final ImageView imageView;
            if (convertView == null) {
                convertView = createImage();
                imageView = (ImageView) convertView;
                convertView.setTag(imageView);

            } else {
                imageView = (ImageView) convertView.getTag();
            }

            loadImage(path, imageView);

            return convertView;
        }


        public ImageView createImage() {
            return new ImageView(activity);

        }

        private void loadImage(final String path, final ImageView imageView) {
            imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 440));
            ImageLoader.getInstance().displayImage("file://" + path, imageView);

        }


    }
}
