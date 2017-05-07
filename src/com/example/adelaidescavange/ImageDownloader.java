package com.example.adelaidescavange;

import android.app.Activity;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageDownloader {
	 
	private static ImageDownloader instance;
	public static ImageDownloader getInstance(Activity activity)
	{
		if (instance == null)
		{
			instance = new ImageDownloader(activity);
		}
		return instance;
	}
	
	private ImageLoader imageLoader;
	
	public ImageDownloader(Activity activity)
	{
		imageLoader = ImageLoader.getInstance();

		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
	}
    /**
     * This method will download the image specified by the imageURL
     * and show it with the imageView
     *
     * @param imageView The imageView which will show the
     *        image that is hosted online.
     * @param imageURL  the url of the image to be shown.
     */
	public void downloadAndShowImage(final String imageUrl, final Activity activity, final int imageView) {
    	
		downloadAndShowImage(imageUrl, (ImageView) activity.findViewById(imageView));
		
    }
	public void downloadAndShowImage(final String imageUrl, final ImageView imageView) {
	
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.build();
		
		imageLoader.displayImage(imageUrl, imageView, options);
		
	}
}

