package com.example.rentpe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rentpe.adapters.ImagesAdapter
import com.example.rentpe.databinding.FragmentImagesBinding


class ImagesFragment : Fragment() {

    private var _binding: FragmentImagesBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images: List<String>? = arguments?.getStringArrayList(ARG_IMAGES)
        images?.let {
            val adapter = ImagesAdapter(images)
            binding.imagesRecyclerView.layoutManager = GridLayoutManager(context, 3)
            binding.imagesRecyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGES = "arg_images"
        fun newInstance(images: ArrayList<String>): ImagesFragment {
            val fragment = ImagesFragment()
            val args = Bundle().apply {
                putStringArrayList(ARG_IMAGES, ArrayList(images))
            }
            fragment.arguments = args
            return fragment
        }
    }
}