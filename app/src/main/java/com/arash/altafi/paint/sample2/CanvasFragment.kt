package com.arash.altafi.paint.sample2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.HorizontalScrollView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.listItems
import com.arash.altafi.paint.R
import com.arash.altafi.paint.databinding.FragmentCanvasSample2Binding
import kotlinx.coroutines.*

class CanvasFragment : Fragment() {

    private lateinit var binding: FragmentCanvasSample2Binding
    private var brushColor: Int = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentCanvasSample2Binding.inflate(inflater, container, false)

        // Initiate for long click.
        eraser()
        brush()
        onBackPressed()
        listener()
        return binding.root
    }

    private fun listener() {
        binding.ibMoveRight.setOnClickListener {
            moveScroll(0)
        }

        binding.ibMoveLeft.setOnClickListener {
            moveScroll(1)
        }

        binding.ibShareDrawing.setOnClickListener {
            shareDrawing()
        }

        binding.ibClearCanvas.setOnClickListener {
            binding.drawingView.clearDrawing()
        }

        binding.ibBackgroundImage.setOnClickListener {
            imageBackground()
        }

        binding.ibBrushColor.setOnClickListener {
            brushColor()
        }

        binding.ibRedoDraw.setOnClickListener {
            binding.drawingView.redoPath()
        }

        binding.ibUndoDraw.setOnClickListener {
            binding.drawingView.undoPath()
        }

        binding.ibEraseDraw.setOnClickListener {
            eraser()
        }

        binding.ibBrushSize.setOnClickListener {
            brush()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode == Activity.RESULT_OK && requestCode == GALLERY -> {
                try {
                    when {
                        data!!.data != null -> binding.ivBackground.setImageURI(data.data)
                        else -> snackBarMsg(requireView(), getString(R.string.error_parsing))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE
        )
    }

    private fun saveDrawing() {
        when {
            isReadStorageAllowed() -> saveBitmap()
            else -> requestStoragePermission()
        }
    }

    fun brush() {
        binding.drawingView.setBrushColor(brushColor)

        binding.ibBrushSize.setOnLongClickListener {
            showBrushSizeDialog(false)
            binding.drawingView.setBrushColor(brushColor)
            return@setOnLongClickListener true
        }
    }

    fun eraser() {
        binding.drawingView.setBrushColor(Color.WHITE)

        binding.ibEraseDraw.setOnLongClickListener {
            showBrushSizeDialog(true)
            binding.drawingView.setBrushColor(Color.WHITE)
            return@setOnLongClickListener true
        }
    }

    @SuppressLint("CheckResult")
    fun brushColor() {
        @Suppress("DEPRECATION")
        val colors = intArrayOf(
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.MAGENTA, Color.GRAY, Color.CYAN,
            resources.getColor(R.color.beige), resources.getColor(R.color.orange),
            resources.getColor(R.color.greenLight), resources.getColor(R.color.purpleBlue)
        )

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.dialog_choose_color)
            colorChooser(colors, allowCustomArgb = true, showAlphaSelector = true) { _, color ->
                brushColor = color
                binding.drawingView.setBrushColor(brushColor)
            }
            positiveButton(R.string.dialog_select)
            negativeButton(R.string.dialog_negative)
        }
    }

    @SuppressLint("CheckResult")
    fun imageBackground() {
        val options = listOf(
            getString(R.string.background_image_add), getString(R.string.background_image_clear)
        )

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.dialog_background_image)
            listItems(items = options) { _, index, _ ->
                when (index) {
                    0 -> when {
                        isReadStorageAllowed() -> requestImage()
                        else -> requestStoragePermission()
                    }
                    1 -> binding.ivBackground.setImageURI(null)
                }
            }
        }
    }

    fun shareDrawing() {
        binding.drawingView.saveBitmap(binding.drawingView.getBitmap(binding.flDrawingViewContainer))

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(binding.drawingView.result))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        startActivity(Intent.createChooser(intent, getString(R.string.share_drawing)))
    }

    fun moveScroll(direction: Int) {
        when (direction) {
            0 -> {
                binding.hsvRight.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
                binding.ibMoveRight.visibility = View.INVISIBLE
                binding.ibMoveLeft.visibility = View.VISIBLE
            }
            1 -> {
                binding.hsvRight.fullScroll(HorizontalScrollView.FOCUS_LEFT)
                binding.ibMoveLeft.visibility = View.INVISIBLE
                binding.ibMoveRight.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun showBrushSizeDialog(eraser: Boolean) {
        val sizes = listOf(
            BasicGridItem(R.drawable.brush_small, getString(R.string.brush_small)),
            BasicGridItem(R.drawable.brush_medium, getString(R.string.brush_medium)),
            BasicGridItem(R.drawable.brush_large, getString(R.string.brush_large))
        )

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            when (eraser) {
                true -> title(R.string.dialog_choose_eraser_size)
                else -> title(R.string.dialog_choose_brush_size)
            }

            gridItems(sizes) { _, index, _ ->
                when (index) {
                    0 -> binding.drawingView.setBrushSize(5F)
                    1 -> binding.drawingView.setBrushSize(10F)
                    2 -> binding.drawingView.setBrushSize(20F)
                }
            }
        }
    }

    private fun requestImage() {
        try {
            val pickPhotoIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhotoIntent, GALLERY)
        } catch (e: Exception) {
            snackBarMsg(requireView(), getString(R.string.gallery_not_available))
        }
    }

    private fun saveBitmap() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.drawingView.saveBitmap(binding.drawingView.getBitmap(binding.flDrawingViewContainer))
            snackBarMsg(requireView(), getString(R.string.drawing_saved))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                        title(R.string.dialog_exit)
                        message(R.string.dialog_exit_message)

                        positiveButton(R.string.option_save_drawing) {
                            saveDrawing()

                            GlobalScope.launch(Dispatchers.Main) {
                                delay(1500L)
                                if (isEnabled) {
                                    isEnabled = false
                                    requireActivity().onBackPressed()
                                }
                            }
                        }
                        negativeButton(R.string.dialog_exit_confirmation) {
                            if (isEnabled) {
                                isEnabled = false
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                }
            }
        )
    }
}