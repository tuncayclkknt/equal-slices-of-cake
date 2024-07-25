@file:Suppress("DEPRECATION")

package com.tuncay.eitpastadilil

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tuncay.eitpastadilil.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(), SurfaceHolder.Callback {

    private val CAMERA_REQUEST_CODE = 100
    private lateinit var binding: ActivityMainBinding
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var pieChartCounter = 2
        binding.pieChartView.setSliceCount(2)

        binding.btnPlus.setOnClickListener {
            if (pieChartCounter >= 2 && pieChartCounter <= 20){
                pieChartCounter++
                binding.txtslices.text = pieChartCounter.toString()
                binding.pieChartView.setSliceCount(pieChartCounter)
            }else{
                Toast.makeText(this,"En az 2, en fazla 20 dilim seç",Toast.LENGTH_LONG).show()
            }

        }

        binding.btnMinus.setOnClickListener {
            if (pieChartCounter > 3 && pieChartCounter <= 20){
                pieChartCounter--
                binding.txtslices.text = pieChartCounter.toString()
                binding.pieChartView.setSliceCount(pieChartCounter)
            }else{
                Toast.makeText(this,"En az 2, en fazla 20 dilim seç",Toast.LENGTH_LONG).show()
            }

        }

        val surfaceView: SurfaceView = binding.surfaceView
        val surfaceHolder: SurfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun openCamera() {
        try {
            camera = Camera.open()
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(binding.surfaceView.holder)
            camera?.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        openCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (holder.surface == null) {
            return
        }
        camera?.stopPreview()
        try {
            val parameters = camera?.parameters
            parameters?.let {
                val supportedPreviewSizes = it.supportedPreviewSizes
                val previewSize = supportedPreviewSizes.find { size ->
                    size.width.toDouble() / size.height.toDouble() == 4.0 / 3.0
                } ?: supportedPreviewSizes[0] // Default to first supported size if 4:3 is not found
                it.setPreviewSize(previewSize.width, previewSize.height)
                camera?.parameters = it
            }
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }
}