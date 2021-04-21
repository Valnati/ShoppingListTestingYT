package com.androiddevs.shoppinglisttestingyt.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.androiddevs.shoppinglisttestingyt.adapters.ImageAdapter
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager
): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            else -> super.instantiate(classLoader, className)
        }

    }
}