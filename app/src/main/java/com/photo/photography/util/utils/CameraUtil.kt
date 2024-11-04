package com.photo.photography.util.utils

import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.photo.photography.MyApp
import com.photo.photography.secure_vault.utils.VaultFileUtil
import java.io.File
import java.util.*

class CameraUtil {
    companion object {

        private const val TAG = "CameraXBasic"

        @JvmStatic
        fun takeSnapShot(mActivity: AppCompatActivity, viewFinder: PreviewView) {
            startCamera(mActivity, viewFinder)
        }

        private fun startCamera(mActivity: AppCompatActivity, viewFinder: PreviewView) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(mActivity)

            cameraProviderFuture.addListener(Runnable {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val imageCapture: ImageCapture = ImageCapture.Builder().build()

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(mActivity, cameraSelector, preview, imageCapture)

//                    Handler().postDelayed({
                    takePhoto(mActivity, imageCapture)
//                    }, 5000)


                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(mActivity))
        }

        private fun takePhoto(mActivity: AppCompatActivity, imageCapture: ImageCapture) {
            // Get a stable reference of the modifiable image capture use case

            val photoFile = File(
                VaultFileUtil(MyApp.mContext)
                    .getFilePath(VaultFileUtil.FOLDER_TO_SNAP),
                "snapTrack_" + Calendar.getInstance().timeInMillis + ".jpg"
            )

            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            // Set up image capture listener, which is triggered after photo has
            // been taken
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(mActivity),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo capture succeeded: $savedUri"
//                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                    }
                })
        }
    }
}