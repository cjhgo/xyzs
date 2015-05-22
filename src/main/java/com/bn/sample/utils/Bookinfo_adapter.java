package com.bn.sample.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bn.sample.R;

import java.util.ArrayList;

/**
 * Created by chen-pc on 2015/3/13.
 */
public class Bookinfo_adapter extends ArrayAdapter<Bookinfo>
{
    private final Context context;

    private ArrayList<Bookinfo> booklist;

    public Bookinfo_adapter(Context context,ArrayList<Bookinfo> objects)
    {

        super(context, R.layout.bookrow, objects);
        this.context=context;
        this.booklist=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bookrow = inflater.inflate(R.layout.bookrow, parent, false);

        TextView title =(TextView)bookrow.findViewById(R.id.title);
        TextView doc_type =(TextView)bookrow.findViewById(R.id.doc_type);
        TextView author =(TextView)bookrow.findViewById(R.id.author);
        TextView gc_count =(TextView)bookrow.findViewById(R.id.gc_count);
        TextView publication =(TextView)bookrow.findViewById(R.id.publication);
        TextView kj_count =(TextView)bookrow.findViewById(R.id.kj_count);
        TextView ztflh =(TextView)bookrow.findViewById(R.id.ztflh);

        Bookinfo cur_book=booklist.get(position);

        title.setText(cur_book.get_title());
        doc_type.setText(cur_book.get_doc_type());
        author.setText(cur_book.get_author());
        gc_count.append(cur_book.get_gc_count());
        publication.setText(cur_book.get_publication());
        kj_count.append(cur_book.get_kj_count());
        ztflh.append(cur_book.get_ztflh().length()==0?"暂不可借":cur_book.get_ztflh());

        return bookrow;
    }
    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}