package com.borebrian.statussaver.image

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.borebrian.statussaver.R
import com.bumptech.glide.Glide

import com.borebrian.statussaver.utils.Utils
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.content_image_view.*
import org.apache.commons.io.FileUtils
import java.io.File
import android.app.WallpaperManager
import android.content.Context

import android.net.Uri
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.widget.*
import com.borebrian.statussaver.home.HomeActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.gms.ads.reward.RewardedVideoAd
import java.util.*
import kotlin.concurrent.fixedRateTimer


class ImageViewActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var mAdView: AdView
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var nativeExpressAddViewx: NativeExpressAdView
    private lateinit var mRewardedVideoAd: RewardedVideoAd


     //NATIVE IDS
    val ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    val ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    var currentNativeAd: UnifiedNativeAd? = null



    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/6300978111";

        
        mInterstitialAd.loadAd(AdRequest.Builder().build());

         //ADVIEW   BANNER
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712")



        //NATIVE ADS
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, ADMOB_APP_ID)
        

      /*  refresh_button.setOnClickListener { refreshAd() }
*/       //TIMER FOR NATIVE ADDS
     /*   fixedRateTimer("timer",false,5000,10000){
            this@ImageViewActivity.runOnUiThread {
               *//* Toast.makeText(this@ImageViewActivity, "text", Toast.LENGTH_SHORT).show()*//*

                *//*refreshAd()*//*
            }}*/
        //TIMER FOR INTERSTITIAL ADDS
      /*  fixedRateTimer("timer",false,3000,15000){
            this@ImageViewActivity.runOnUiThread {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }
                else{
                    mInterstitialAd = InterstitialAd(this@ImageViewActivity);
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
                    mInterstitialAd.loadAd(AdRequest.Builder().build());
                    call()

                }

            }}*/


        var status=0
        showInterstitialAd()



        // Interstitial
      /*  mInterstitialAd.adUnitId = "ca-app-pub-7643266345625929/3567501731"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.show()
*/
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713








          val adRequest = AdRequest.Builder().build()
          addV.loadAd(adRequest)



        download.visibility=View.GONE
        setas.visibility=View.GONE
        delete.visibility=View.GONE
        sharestatus.visibility=View.GONE
        val imageFile = File(intent.getStringExtra("image"))


        fab4.setOnClickListener(){

            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.fromFile(imageFile), "image/jpeg")
            intent.putExtra("mimeType", "image/jpeg")
            this.startActivity(Intent.createChooser(intent, "Set as:"))
            showInterstitialAd()

        }







        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())



        Glide.with(this)
                .load(imageFile)
                .into(imageView)

           fab2.setOnClickListener(){
               val destFile = File("${Environment.getExternalStorageDirectory()}${Utils.WHATSAPP_STATUSES_SAVED_LOCATION}/${imageFile.name}")
               FileUtils.copyFile(imageFile, destFile)
               Utils.addToGallery(this, destFile)
               Toast.makeText(this, this.getString(R.string.status_saved_to_gallery), Toast.LENGTH_SHORT).show()
           }
        fab1.setOnClickListener(){
            Toast.makeText(this,"Please select app to share to",Toast.LENGTH_LONG).show()
            Utils.shareFile(this, imageFile)
            showInterstitialAd()
        }

        fab3.setOnClickListener(){
            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.fromFile(imageFile), "image/jpeg")
            intent.putExtra("mimeType", "image/jpeg")
            this.startActivity(Intent.createChooser(intent, ""))
            showInterstitialAd()

        }
        fab4.setOnClickListener(){
            showAdd()
            val fdelete =(imageFile)
            if (fdelete.exists())
            {
                if (fdelete.delete())
                {
                    showInterstitialAd()

                    Toast.makeText(this,"Status Deleted successfully from:"+imageFile,Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }
                else
                {
                    showInterstitialAd()

                    Toast.makeText(this,"file was not deleted!!",Toast.LENGTH_LONG).show();
                }
            }
            else{
                showInterstitialAd()
                Toast.makeText(this,"No file to delete",Toast.LENGTH_LONG).show()
                finish()
            }



        }

        fab.setOnClickListener(){

            if(status==0 && imageFile.toString().contains("statusSaver")){

                /*Toast.makeText(this,"Please select app to share to", Toast.LENGTH_LONG).show()*/
                sharestatus.visibility=View.VISIBLE;
                setas.visibility=View.VISIBLE;
                download.visibility=View.GONE
                delete.visibility=View.VISIBLE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                var status2=status
                status=status2+1
                mInterstitialAd.show()
            }
            else if(status==0 && imageFile.toString().contains("Statuses")){
                sharestatus.visibility=View.VISIBLE;
                setas.visibility=View.VISIBLE;
                download.visibility=View.VISIBLE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                var status2=status
                status=status2+1
                mInterstitialAd.show()

            }
            else{
                sharestatus.visibility=View.GONE;
                setas.visibility=View.GONE;
                download.visibility=View.GONE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add_black_24dp))
                var status2=status
                status=0
                mInterstitialAd.show()
            }

        }

    }
    

    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
   /*     currentNativeAd?.destroy()
        currentNativeAd = nativeAd*/
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        val vc = nativeAd.videoController

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
          /*  videostatus_text.text = String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.aspectRatio)*/

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    /*refresh_button.isEnabled = true*/
                   /* videostatus_text.text = "Video status: Video playback has ended."*/
                    super.onVideoEnd()
                }
            }
        } else {
            /*videostatus_text.text = "Video status: Ad does not contain a video asset."
            refresh_button.isEnabled = true*/
        }
    }
   /* private fun refreshAd() {
        *//*refresh_button.isEnabled = false*//*

        val builder = AdLoader.Builder(this, ADMOB_AD_UNIT_ID)

        builder.forUnifiedNativeAd { unifiedNativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            val adView = layoutInflater
                    .inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            ad_frame.removeAllViews()
            ad_frame.addView(adView)
        }

     *//*   val videoOptions = VideoOptions.Builder()
                .setStartMuted(start_muted_checkbox.isChecked)
                .build()
*//*
    *//*    val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()*//*

       *//* builder.withNativeAdOptions(adOptions)*//*

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                *//*refresh_button.isEnabled = true*//*
               *//* Toast.makeText(this@ImageViewActivity, "" + errorCode, Toast.LENGTH_SHORT).show()*//*
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
*//*
        videostatus_text.text = ""*//*
    }*/
    override fun onDestroy() {
       /* currentNativeAd?.destroy()*/
        super.onDestroy()
    }
    


    fun showAdd(){
        /* if (mInterstitialAd.isLoaded()) {*/
        mInterstitialAd = InterstitialAd(this);
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/6300978111";
        mInterstitialAd.loadAd(AdRequest.Builder().build());
        mInterstitialAd.show()

        /* }
         else{*/
        /*  Toast.makeText(this,"Not loaded",Toast.LENGTH_LONG).show()
          mInterstitialAd = InterstitialAd(this);
          mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
          mInterstitialAd.loadAd(AdRequest.Builder().build());
          mInterstitialAd.show()*/

    }


    fun showInterstitialAd() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
    else{
            mInterstitialAd = InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            mInterstitialAd.loadAd(AdRequest.Builder().build());
            call()

        }}
    fun call(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            
        }


    }







    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        mScaleGestureDetector?.onTouchEvent(motionEvent)
        return true
    }



    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f))
            imageView.scaleX = mScaleFactor
            imageView.scaleY = mScaleFactor
            return true
        }
    }


}
