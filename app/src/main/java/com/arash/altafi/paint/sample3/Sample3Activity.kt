package com.arash.altafi.paint.sample3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arash.altafi.paint.databinding.ActivitySample3Binding
import com.github.onecode369.andy_handy_animations.slideInLeft
import com.github.onecode369.andy_handy_animations.slideOutLeft
import java.io.File
import java.io.FileOutputStream
import java.util.*

class Sample3Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {

            seekBar.progress = 30
            canvas.strokeWidth(seekBar.progress.toFloat())
            resetText()

            savePaint.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@Sample3Activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )  != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@Sample3Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                }else {
                    val paint = canvas.savePainting()
                    val root =
                        Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/MyCanvas/")
                    myDir.mkdirs()
                    val timeStamp: String = Calendar.getInstance().timeInMillis.toString()
                    val fname = "Paint_$timeStamp.jpeg"

                    val file = File(myDir, fname)
                    if (file.exists()) file.delete()
                    try {
                        val out = FileOutputStream(file)
                        paint.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            colors.setOnClickListener {

                if(colorsList.visibility == View.GONE){
                    colorsList.visibility = View.VISIBLE
                    slideInLeft(colorsList,500)
                }else{
                    slideOutLeft(colorsList,500)
                    colorsList.visibility = View.GONE
                }

            }

            circle.setOnClickListener {
                canvas.drawCircle()
            }

            line.setOnClickListener {
                canvas.drawLine()
            }

            rectangle.setOnClickListener {
                canvas.drawRect()
            }

            color1.setOnClickListener {
                resetText()
                color1.text = "^"
                canvas.paintColor(Color.parseColor("#F44336"))
            }

            color2.setOnClickListener {
                resetText()
                color2.text = "^"
                canvas.paintColor(Color.parseColor("#E91E63"))
            }

            color3.setOnClickListener {
                resetText()
                color3.text = "^"
                canvas.paintColor(Color.parseColor("#9C27B0"))
            }

            color4.setOnClickListener {
                resetText()
                color4.text = "^"
                canvas.paintColor(Color.parseColor("#673AB7"))
            }

            color5.setOnClickListener {
                resetText()
                color5.text = "^"
                canvas.paintColor(Color.parseColor("#3F51B5"))
            }

            color6.setOnClickListener {
                resetText()
                color6.text = "^"
                canvas.paintColor(Color.parseColor("#2196F3"))
            }

            color7.setOnClickListener {
                resetText()
                color7.text = "^"
                canvas.paintColor(Color.parseColor("#03A9F4"))
            }

            color8.setOnClickListener {
                resetText()
                color8.text = "^"
                canvas.paintColor(Color.parseColor("#00BCD4"))
            }

            color9.setOnClickListener {
                resetText()
                color9.text = "^"
                canvas.paintColor(Color.parseColor("#009688"))
            }

            color10.setOnClickListener {
                resetText()
                color10.text = "^"
                canvas.paintColor(Color.parseColor("#4CAF50"))
            }

            color11.setOnClickListener {
                resetText()
                color11.text = "^"
                canvas.paintColor(Color.parseColor("#8BC34A"))
            }

            color12.setOnClickListener {
                resetText()
                color12.text = "^"
                canvas.paintColor(Color.parseColor("#CDDC39"))
            }

            color13.setOnClickListener {
                resetText()
                color13.text = "^"
                canvas.paintColor(Color.parseColor("#FFEB3B"))
            }

            color14.setOnClickListener {
                resetText()
                color14.text = "^"
                canvas.paintColor(Color.parseColor("#FFC107"))
            }

            color15.setOnClickListener {
                resetText()
                color15.text = "^"
                canvas.paintColor(Color.parseColor("#FF9800"))
            }

            color16.setOnClickListener {
                resetText()
                color16.text = "^"
                canvas.paintColor(Color.parseColor("#FF5722"))
            }

            erase.setOnClickListener {
                canvas.paintColor(Color.parseColor("#FFFFFF"))
            }


            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    canvas.strokeWidth(seekBar.progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // Write code to perform some action when touch is started.
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    canvas.strokeWidth(seekBar.progress.toFloat())
                }
            })

            colorInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    try {
                        colorOutput.setBackgroundColor(Color.parseColor(colorInput.text.toString()))
                        colorOutput.setOnClickListener {
                            try {
                                canvas.paintColor(Color.parseColor(colorInput.text.toString()))
                            } catch (e : Exception){}
                        }
                    } catch (e: Exception) {}
                }
            })
        }
    }

    private fun resetText() {
        binding.apply {
            color1.text = ""
            color2.text = ""
            color3.text = ""
            color4.text = ""
            color5.text = ""
            color6.text = ""
            color7.text = ""
            color8.text = ""
            color9.text = ""
            color10.text = ""
            color11.text = ""
            color12.text = ""
            color13.text = ""
            color14.text = ""
            color15.text = ""
            color16.text = ""
        }
    }

}