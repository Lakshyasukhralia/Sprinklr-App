package com.sukhralia.sprinklrtest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sukhralia.sprinklrtest.MainActivity
import com.sukhralia.sprinklrtest.R
import com.sukhralia.sprinklrtest.constants.ED_TECH
import com.sukhralia.sprinklrtest.constants.MACHINE_LEARNING
import com.sukhralia.sprinklrtest.constants.MEDICAL
import com.sukhralia.sprinklrtest.constants.TRENDING
import com.sukhralia.sprinklrtest.databinding.ProductItemBinding
import com.sukhralia.sprinklrtest.listener.ProductListener
import com.sukhralia.sprinklrtest.model.ProductModel
import com.sukhralia.sprinklrtest.utils.GlideApp

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    private lateinit var mContext: Context
    lateinit var mListener: ProductListener

    var myData = listOf<ProductModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun submitData(data: List<ProductModel>) {
        myData = data
    }

    inner class MyViewHolder(val myItemView: ProductItemBinding) :
        RecyclerView.ViewHolder(myItemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        return MyViewHolder(
            DataBindingUtil.inflate<ProductItemBinding>(
                LayoutInflater.from(mContext),
                R.layout.product_item,
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = myData[position]

        holder.myItemView.title.text = item.name
        holder.myItemView.plt.text = item.description
        holder.myItemView.users.text = item.upvotes.toString()
        holder.myItemView.worth.text = String.format(
            mContext.resources.getString(
                R.string.founder_info,
                item.founder.name,
                item.founder.info
            )
        )

        var imgUrl: Drawable? = null

        when (item.category) {
            MACHINE_LEARNING -> {
                imgUrl = mContext.resources.getDrawable(R.drawable.ic_learning)
            }
            ED_TECH -> {
                imgUrl = mContext.resources.getDrawable(R.drawable.ic_teaching)
            }
            TRENDING -> {
                imgUrl = mContext.resources.getDrawable(R.drawable.ic_trend)
            }
            MEDICAL -> {
                imgUrl = mContext.resources.getDrawable(R.drawable.ic_doctor)
            }
        }

        GlideApp.with(mContext)
            .load(imgUrl)
            .into(holder.myItemView.image)

        holder.myItemView.users.setOnClickListener {
            when (item.isUpvoted) {
                true -> { Toast.makeText(mContext,mContext.getString(R.string.upvote_fail),Toast.LENGTH_SHORT).show()}
                false -> {
                    item.upvotes = item.upvotes + 1
                    item.isUpvoted = true
                    mListener.updateProduct(item)
                }
            }
        }

        when (item.isBookmarked) {
            true -> {
                holder.myItemView.bookmark.setImageResource(R.drawable.ic_bookmark_true)
            }
            false -> {
                holder.myItemView.bookmark.setImageResource(R.drawable.ic_bookmark)
            }
        }

        holder.myItemView.bookmark.setOnClickListener {
            when (item.isBookmarked) {
                true -> {
                    item.isBookmarked = false
                }
                false -> {
                    item.isBookmarked = true
                }
            }
            mListener.updateProduct(item)
        }

        holder.myItemView.share.setOnClickListener {
            mListener.share(item.url)
        }
    }

    override fun getItemCount(): Int {
        return myData.size
    }

}