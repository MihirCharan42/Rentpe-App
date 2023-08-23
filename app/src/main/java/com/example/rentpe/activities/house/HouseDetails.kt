package com.example.rentpe.activities.house

import android.content.ClipData
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.rentpe.R
import com.example.rentpe.databinding.ActivityHouseDetailsBinding
import com.example.rentpe.models.house.HouseRequest
import com.example.rentpe.utils.Helper
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.viewModels.HouseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HouseDetails : AppCompatActivity() {

    private lateinit var binding: ActivityHouseDetailsBinding
    private val houseViewModel by viewModels<HouseViewModel>()
    private var imagesEncodedList: ArrayList<String>? = null
    private val imageList = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        houseViewModel.houseStatusLiveData.observe(this@HouseDetails) {
            binding.loader.isVisible = false
            binding.submitBtn.isVisible = true
            when(it) {
                is NetworkResult.Loading -> {
                    binding.loader.isVisible = true
                    binding.submitBtn.isVisible = false
                }
                is NetworkResult.Success -> {
                    binding.loader.isVisible = false
                    binding.submitBtn.isVisible = true
                    Toast.makeText(this@HouseDetails, it.data!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is NetworkResult.Error -> {
                    binding.loader.isVisible = false
                    binding.submitBtn.isVisible = true
                    Toast.makeText(this@HouseDetails, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.imagesBtn.setOnClickListener {
            val PICK_IMAGE_MULTIPLE = 1
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_MULTIPLE
            )
        }

        binding.submitBtn.setOnClickListener {
            if(validHouse()) {
                val houseRequest = createHouseRequest()
                uploadHome(houseRequest)
            }
        }

    }

    private fun validHouse(): Boolean {
        if(!Helper().validMobile(binding.tenantNumberTextfield.text.toString())){
            Log.e("Tenant", binding.tenantNumberTextfield.text.toString())
            Toast.makeText(this@HouseDetails, "Invalid Tenant Phone Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Helper().validMobile(binding.landlordNumberTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid Landlord Phone Number", Toast.LENGTH_SHORT).show()

            return false
        }
        if(!Helper().validName(binding.landlordNameTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid Landlord Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Helper().validName(binding.tenantNameTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid Tenant Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Helper().validTextField(binding.addressTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid or Empty Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Helper().validTextField(binding.descriptionTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid or Empty Description", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Helper().validTextField(binding.rentTextfield.text.toString())){
            Toast.makeText(this@HouseDetails, "Invalid or Empty Rent", Toast.LENGTH_SHORT).show()
            return false
        }
        if(imageList.isEmpty()) {
            Toast.makeText(this@HouseDetails, "Please select images", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun uploadHome(houseRequest: HouseRequest) {
        houseViewModel.postHomes(houseRequest)
    }

    private fun createHouseRequest(): HouseRequest {
        val address: String = binding.addressTextfield.text.toString()
        val description: String = binding.descriptionTextfield.text.toString()
        val landlord_name: String = binding.landlordNameTextfield.text.toString()
        val landlord_phone: String = binding.landlordNumberTv.text.toString()
        val rent: Int = Integer.parseInt(binding.rentTextfield.text.toString())
        val tenant_name: String = binding.tenantNameTv.text.toString()
        val tenant_phone: String = binding.tenantNumberTv.text.toString()
        return HouseRequest(address, description, landlord_name, landlord_phone, rent, tenant_name, tenant_phone, imageList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                && data != null
            ) {
                // Get the Image from data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                imagesEncodedList = ArrayList()
                if (data.data != null) {
                    val mImageUri = data.data

                    // Get the cursor
                    val cursor: Cursor? =
                        contentResolver.query(mImageUri!!, filePathColumn, null, null, null)
                    // Move to first row
                    cursor?.moveToFirst()

                    val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0]) ?: -1
                    if (columnIndex != -1) {
                        val imageEncoded: String? = cursor?.getString(columnIndex)
                        val file = File(imageEncoded)
                        val uri = file.toUri()
                        imageList.add(uri)
                        cursor?.close()
                    }
                } else {
                    if (data.clipData != null) {
                        val mClipData: ClipData? = data.clipData
                        val mArrayUri: ArrayList<Uri> = ArrayList()
                        for (i in 0 until mClipData!!.itemCount) {
                            val item: ClipData.Item = mClipData.getItemAt(i)
                            val uri: Uri = item.uri

                            mArrayUri.add(uri)
                            // Get the cursor
                            val cursor: Cursor? =
                                contentResolver.query(uri, filePathColumn, null, null, null)
                            // Move to first row
                            cursor?.moveToFirst()

                            val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0]) ?: -1
                            if (columnIndex != -1) {
                                val imageEncoded: String? = cursor?.getString(columnIndex)
                                imagesEncodedList?.add(imageEncoded ?: "")
                                val file = File(imageEncoded)
                            }
                            cursor?.close()
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size)
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val PICK_IMAGE_MULTIPLE = 1
    }
}