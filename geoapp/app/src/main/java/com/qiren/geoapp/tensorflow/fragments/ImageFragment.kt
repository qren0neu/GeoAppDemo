package com.qiren.geoapp.tensorflow.fragments

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.qiren.geoapp.tensorflow.ObjectDetectorHelper
import com.qiren.geoapp.tensorflow.OverlayView
import com.qiren.geoapp.R
import org.tensorflow.lite.task.vision.detector.Detection

class ImageFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var overlayView: OverlayView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        imageView = view.findViewById(R.id.image_view)
        overlayView = view.findViewById(R.id.overlay)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the image URI from the arguments
        val imageStr = arguments?.getString("imageUri")
        val imageUri = Uri.parse(imageStr)

        val bitmap = if (imageUri != null) {
            MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
        } else {
            // handle the error condition if imageUri is null
            null
        }
        if (bitmap != null) {
            val helper = ObjectDetectorHelper(
                    context = requireActivity(),
                    objectDetectorListener = object : ObjectDetectorHelper.DetectorListener {

                        override fun onError(error: String) {
                            Toast.makeText(requireContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }

                        override fun onResults(results: MutableList<Detection>?, inferenceTime: Long, imageHeight: Int, imageWidth: Int) {
                            overlayView.setResults(results!!, imageHeight, imageWidth)
                        }
                    })
            helper.detect(bitmap, 0)
        }

        if (imageUri != null) {
            // load the image into the image view
            Glide.with(requireContext())
                    .load(imageUri)
                    .into(imageView)
        }

        // configure the overlay view
        overlayView.apply {
            setWillNotDraw(false)
            bringToFront()
            visibility = View.VISIBLE
        }
    }
}
