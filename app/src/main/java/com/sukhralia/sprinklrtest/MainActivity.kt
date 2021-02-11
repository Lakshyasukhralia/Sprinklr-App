package com.sukhralia.sprinklrtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sukhralia.sprinklrtest.adapter.ProductAdapter
import com.sukhralia.sprinklrtest.constants.*
import com.sukhralia.sprinklrtest.database.ProductDatabase
import com.sukhralia.sprinklrtest.databinding.ActivityMainBinding
import com.sukhralia.sprinklrtest.listener.ProductListener
import com.sukhralia.sprinklrtest.model.FounderModel
import com.sukhralia.sprinklrtest.model.ProductModel
import com.sukhralia.sprinklrtest.utils.AppDataStore
import com.sukhralia.sprinklrtest.viewmodel.ProductViewModel
import com.sukhralia.sprinklrtest.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), ProductListener, AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: ProductViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter
    private var selectedCategory = ALL

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val application = requireNotNull(this).application

        val dataSource = ProductDatabase.getInstance(application).productDatabaseDao

        val dealViewModelFactory =
            ProductViewModelFactory(
                dataSource, application
            )

        readPreferences()

        binding.productList.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            productAdapter = ProductAdapter()
            productAdapter.mListener = this
            it.adapter = productAdapter
        }

        viewModel =
            ViewModelProviders.of(this, dealViewModelFactory).get(ProductViewModel::class.java)

        binding.insertData.setOnClickListener {
            addProducts()
        }

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {

                viewModel.databaseResponse.collect() {
                    when (it) {
                        is ProductViewModel.ResponseState.Success -> {
                            Snackbar.make(binding.root, "Success", Snackbar.LENGTH_LONG).show()
                            loadProductAdapter(it.data)
                        }
                        is ProductViewModel.ResponseState.SuccessUpdate -> {
                            Snackbar.make(
                                binding.root,
                                "Successfully Updated",
                                Snackbar.LENGTH_LONG
                            ).show()
                            viewModel.fetchAllProducts(selectedCategory)
                        }
                        is ProductViewModel.ResponseState.Error -> {
                            Snackbar.make(binding.root, "Error", Snackbar.LENGTH_LONG).show()
                            loadProductAdapter(listOf<ProductModel>())
                        }
                        is ProductViewModel.ResponseState.Loading -> {
                            Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                }
            }
        }

    }

    private fun setFilter(id: Int, spinner: Spinner) {
        val arrayAdapter = ArrayAdapter.createFromResource(
            this,
            id,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(arrayAdapter.getPosition(selectedCategory), false)
        spinner.onItemSelectedListener = this
    }

    private fun loadProductAdapter(modelList: List<ProductModel>) {

        if (modelList.isNotEmpty()) {
            binding.productList.visibility = View.VISIBLE
        } else {
            binding.productList.visibility = View.GONE
        }

        productAdapter.submitData(modelList)

    }

    private fun addProducts() {

        val productList = mutableListOf<ProductModel>()

        productList.add(
            ProductModel(
                name = "Autonomous Vehicles",
                category = MACHINE_LEARNING,
                description = "The future of transportation will never be the same as it is now, once autonomous vehicles will hit the market. Certain Predictions imply that the adoption of self-driving cars will reduce traffic-related issues by nearly 90%.",
                founder = FounderModel("Nina William", "AI Expert"),
                upvotes = 143,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Content Moderation",
                category = ED_TECH,
                description = "With high accessibility of the internet and modern technology, comes the bane of platform abuse too. Specifically, Pseudo-news or Fake news is the most common digital abuse at present. Outspread of disinformation via digital culture, social media, in particular, is encouraging the necessity of content moderation.",
                founder = FounderModel("Marshall Law", "Content Moderator"),
                upvotes = 112,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Cybersecurity",
                category = ED_TECH,
                description = "As per studies, cybercrime damage costs are expected to increase by \$6 trillion annually by 2021. Different experts foresaw that companies will invest more than 1 trillion in cybersecurity advancement to cope up with the alarming risk.",
                founder = FounderModel("Marshall Law", "Content Moderator"),
                upvotes = 241,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Better Healthcare System",
                category = MACHINE_LEARNING,
                description = "The healthcare industry still operates on conventional infrastructure and it becomes worrisome to preserve sensitive patient data while optimizing the network.",
                founder = FounderModel("Anna William", "AI Expert"),
                upvotes = 74,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Genome Analysis",
                category = MEDICAL,
                description = "As per studies, cybercrime damage costs are expected to increase by \$6 trillion annually by 2021. Different experts foresaw that companies will invest more than 1 trillion in cybersecurity advancement to cope up with the alarming risk.",
                founder = FounderModel("Roger Law", "Bio Expert"),
                upvotes = 98,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Mutation Research",
                category = MEDICAL,
                description = "The healthcare industry still operates on conventional infrastructure and it becomes worrisome to preserve sensitive patient data while optimizing the network.",
                founder = FounderModel("Robin William", "Bio Expert"),
                upvotes = 542,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Pseudo Science",
                category = TRENDING,
                description = "With high accessibility of the internet and modern technology, comes the bane of platform abuse too. Specifically, Pseudo-news or Fake news is the most common digital abuse at present. Outspread of disinformation via digital culture, social media, in particular, is encouraging the necessity of content moderation.",
                founder = FounderModel("Marshall Law", "Researcher"),
                upvotes = 335,
                isBookmarked = false
            )
        )

        productList.add(
            ProductModel(
                name = "Health Science",
                category = "Trending",
                description = "As per studies, cybercrime damage costs are expected to increase by \$6 trillion annually by 2021. Different experts foresaw that companies will invest more than 1 trillion in cybersecurity advancement to cope up with the alarming risk.",
                founder = FounderModel("Marshall Law", "Researcher"),
                upvotes = 68,
                isBookmarked = false
            )
        )

        viewModel.insertAllProducts(productList)
    }

    override fun updateProduct(product: ProductModel) {
        viewModel.updateProduct(product)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategory = resources.getStringArray(R.array.category)[position]
        viewModel.fetchAllProducts(selectedCategory)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        super.onStop()
        savePreferences()
    }

    private fun savePreferences() {
        val mActivity = this
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDataStore.instance(mActivity)
                    .savePreferences("selected_category", selectedCategory)
            }
        }
    }

    private fun readPreferences() {
        val mActivity = this
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                selectedCategory = AppDataStore.instance(mActivity).readPreferences("selected_category") ?: ALL
                setFilter(R.array.category, binding.filter)
                viewModel.fetchAllProducts(selectedCategory)
            }
        }
    }
}