package com.example.project

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ItemObject(val icon: String?,
                      val iconname:String?,
                      val status : String?,
                      val title : String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(iconname)
        parcel.writeString(status)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemObject> {
        override fun createFromParcel(parcel: Parcel): ItemObject {
            return ItemObject(parcel)
        }

        override fun newArray(size: Int): Array<ItemObject?> {
            return arrayOfNulls(size)
        }
    }

}

//class MyAdapter(private val items: List<ItemObject>) : RecyclerView.Adapter<MyViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount() = items.size
//}
//
//class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//
//
//    fun bind(item: ItemObject) {
//        stateTextView.text = item.status
//    }
//}

