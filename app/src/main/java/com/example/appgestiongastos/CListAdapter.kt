package com.example.appgestiongastos

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CListAdapter(private val getContext: Context, private val customListItem:ArrayList<CList>):
    ArrayAdapter<CList>(getContext,0,customListItem)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listaLayout = convertView
        val holder : ViewHolder

        if (listaLayout==null){
            val inflateList = (getContext as Activity).layoutInflater
            listaLayout = inflateList.inflate(R.layout.lista_personalizada,parent,false)
            holder = ViewHolder()
            holder.mTextViewOne = listaLayout!!.findViewById(R.id.txt1)
            holder.mTextViewTwo = listaLayout!!.findViewById(R.id.txt2)
            holder.mTextViewThree = listaLayout!!.findViewById(R.id.txt3)
            holder.mTextViewFour = listaLayout!!.findViewById(R.id.txt4)

            listaLayout.setTag(holder)
        }else{
            holder = listaLayout.getTag() as ViewHolder
        }
        val listItem = customListItem[position]
        holder.mTextViewOne!!.setText(listItem.mClistOne)
        holder.mTextViewTwo!!.setText(listItem.mClistTwo)
        holder.mTextViewThree!!.setText(listItem.mClistThree)
        holder.mTextViewFour!!.setText(listItem.mClistFour)

        return listaLayout
    }

    class ViewHolder{
        internal var mTextViewOne: TextView? = null
        internal var mTextViewTwo: TextView? = null
        internal var mTextViewThree: TextView? = null
        internal var mTextViewFour: TextView? = null
    }
}