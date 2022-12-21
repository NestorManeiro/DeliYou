package pmn.dev.deliyou

import android.content.Context
import android.util.Log
import pmn.dev.deliyou.FavItem
import androidx.recyclerview.widget.RecyclerView
import pmn.dev.deliyou.FavDB
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import pmn.dev.deliyou.R
import android.widget.TextView
import com.squareup.picasso.Picasso

class FavAdapter(private val favItemList: ArrayList<FavItem>) :
    RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    private var favDB: FavDB? = null
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        context = parent.context
        favDB = FavDB(context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Mal", favItemList[position].item_name.toString())
        //holder.favTextView.text = favItemList[position].item_name
        //holder.favImageView.setImageResource(favItemList[position].item_name.toString())
        val item = favItemList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return favItemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var favTextView = itemView.findViewById<TextView>(R.id.favTextView)
        var favBtn = itemView.findViewById<Button>(R.id.favBtn)
        var favImageView = itemView.findViewById<ImageView>(R.id.favImageView)

        fun render(favItem: FavItem){
            favTextView.text = favItem.item_name
            favBtn = itemView.findViewById(R.id.favBtn)
            favImageView = itemView.findViewById(R.id.favImageView)
            val imageUri = favItem.item_image
            Picasso.get().load(imageUri)
                .error(R.mipmap.ic_launcher_round)
                .into(favImageView)
            favBtn.setOnClickListener {
                val position = adapterPosition
                //val favItem = favItemList[position]
                favDB!!.remove_fav(favItem.id!!)
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        favItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favItemList.size)
    }
}