package com.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.AddHappyActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel
import kotlinx.coroutines.NonDisposableHandle.parent

//Context is necessary to pass intents Line 60
open class HappyPlacesAdapter(private val context: Context,private var list: ArrayList<HappyPlaceModel>) : RecyclerView.Adapter<HappyPlacesAdapter.MyViewHolder>() {

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

    //swipe to edit
    fun notifyEditItem(activity:Activity,position: Int,requestCode:Int){
        val intent= Intent(context, AddHappyActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        //we require the activity to call startActivity, because adapter can't do it on own
        activity.startActivityForResult(intent,requestCode)

        //notify the adapter that the item has been edited
        notifyItemChanged(position)
    }

    //swipe to delete
    fun removeAt(position: Int){
        val dbHandler= DatabaseHandler(context)
        val isDeleted=dbHandler.deleteHappyPlace(list[position])
        if(isDeleted>0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }else{
            Toast.makeText(context,"Error deleting",Toast.LENGTH_SHORT).show()
        }
    }

}
