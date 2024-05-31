package com.happyplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(private var list: ArrayList<HappyPlaceModel>) : RecyclerView.Adapter<HappyPlacesAdapter.MyViewHolder>() {

    private var onClickListener: onClickInterface?=null
    inner class MyViewHolder(private val binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: HappyPlaceModel) {
            binding.ivPlaceImage.setImageURI(Uri.parse(model.image))
            binding.tvTitle.text = model.title
            binding.tvDescription.text = model.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHappyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)

        holder.itemView.setOnClickListener{
            //whatever you want to do when clicked
            if(onClickListener!=null){
                onClickListener!!.onClick(position,model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: onClickInterface){
        this.onClickListener=onClickListener
    }
    //for clicking recycler view items
    interface onClickInterface{
        //we need position to identify
        //model to populate the detail activity
        fun onClick(position: Int, model: HappyPlaceModel)
    }

}
