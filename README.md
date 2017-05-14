# Assignment1MT
MindTickle Assignment

Flickr slideshow app which would load the photos in batches and show it. 
The app should load set of photos on a view in its native resolution 
(you will get the width/height of photos in the Flickr API response) in the following way,

#1. The app needs to download the photos in batches. 
    The number of photos in the first batch will be calculated based on the device screen height.
    For example, if first 3 photos will comfortably fit in device’s screen but not the 4th one,
    then only first 3 photos should be downloaded in the 1st batch and loaded in the view. In case,
    if any one photo is larger than the device screen size then let us skip showing that photo.

#2. Once the 1st batch of photos is downloaded, they should be loaded in the view.
    And in the background, the number of photos for next batch should be calculated and the download should be initiated. 
    After 10 seconds the next set of photos should be loaded in the view and the view should scroll to
    next page automatically. In case, if the next batch of photos download is not complete by the 
    10th second then they should be loaded once the download completes. 

#3. Like this let us do the slideshow of 100 photos.

Memory optimized code is a huge plus and so is the structure and readability of the code. 


Files Information
activity/MainActivity.java :  Default Activity and Implementation of slideshow logic with all volley request
                              (asynchronous task + timer task)
adapter/FeedAdapter.java :    RecyclerView adapter to show images/photos from flickr api
utils/Assignment1Utils.java : Common utility fuctions to calculate screen size and other
utils/Constants.java  :       constants
Assignment1Application.java : Application
models/Photo.java :           Photo object from flickr


Libraries used:
butterknife, jackson, volley and support libraries from android

How to use it:
1. Open the App
2. Press start slide show button or play button in action bar
3. To restart press play button and to pause press pause button

Path for debug apk:


