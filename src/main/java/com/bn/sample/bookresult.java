package com.bn.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.sample.utils.Bookinfo;
import com.bn.sample.utils.Htmlutil;
import com.bn.sample.utils.view.XListView;
import com.bn.sample.utils.Bookinfo_adapter;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;
import org.htmlparser.visitors.TextExtractingVisitor;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen-pc on 2015/3/10.
 */
public class bookresult extends Activity implements XListView.IXListViewListener,AdapterView.OnItemClickListener
{
    private static String ENCODE = "UTF-8";
    private Handler mHandler;
    private String url;
    XListView booklist_view;
    private int pagenum;
    private int pagecount=2;
    private ArrayList<Bookinfo> booklist;
    private Bookinfo_adapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xlist);

        mHandler = new Handler();
        Bundle bundle = this.getIntent().getExtras();

        url=bundle.getString("url");
        String first_htmldata=bundle.getString("data");

        this.booklist=getBookList(first_htmldata);

        booklist_view=(XListView)findViewById(R.id.xlist);

        if(booklist!=null)
        {


            adapter = new Bookinfo_adapter(this,booklist);
            booklist_view.setAdapter(adapter);
//        System.out.println(booklist.get(0).get_author());

            booklist_view.setPullLoadEnable(true);
            booklist_view.setOnItemClickListener(this);
            booklist_view.setXListViewListener(this);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "no data",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }
    @Override
    public void onLoadMore()
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(pagecount <= pagenum)
                {
                    String newurl=url+("&page="+ Integer.toString(pagecount));
                    String newhtmldata=Htmlutil.getHtmlString(newurl);
                    ArrayList<Bookinfo> moreBookList=getBookList(newhtmldata,pagecount);
                    booklist.addAll(moreBookList);
                    adapter.notifyDataSetChanged();
                    pagecount++;
                    booklist_view.stopLoadMore();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "no more data",
                            Toast.LENGTH_SHORT).show();
                    booklist_view.stopLoadMore();
                }
            }

        }, 2000);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, final View view,
                            int position, long id)
    {
        final AdapterView<?> par=parent;
        final int posi=position;
        final ProgressDialog progressDialog = new ProgressDialog(bookresult.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Bookinfo curbook=(Bookinfo)par.getItemAtPosition(posi);

                String curbook_link=curbook.get_link();

                Intent getItem = new Intent(bookresult.this,bookitem.class);
                Bundle postdata=new Bundle();
//                postdata.putString("url",curbook_link);
                postdata.putString("data",Htmlutil.getHtmlString(curbook_link));
                getItem.putExtras(postdata);
                progressDialog.dismiss();
                startActivity(getItem);
                //                Toast.makeText(getApplicationContext(), curbook_link,
//                        Toast.LENGTH_SHORT).show();
            }
        },2000);




    }
    private ArrayList<Bookinfo> getBookList(String htmldata,int...pagecount)
    {
        ArrayList<Bookinfo> moreBooklist;//
        if(pagecount.length == 0)
        {
            moreBooklist=this.booklist;
        }
        else
        {
            moreBooklist=new ArrayList<Bookinfo>();
//            url+=("&page="+ Integer.toString(pagecount[0]));
        }
        try
        {
//            String data=Htmlutil.getHtmlString(url);
            String data=htmldata;
            Parser parser= new Parser(data);
            parser.setEncoding("UTF-8");
            if(pagecount.length == 0)
            {
                NodeFilter filter_num= new NodeFilter() {
                    @Override
                    public boolean accept(Node node) {
                        return ((node instanceof Tag)
                                && !((Tag)node).isEndTag()
                                && ((Tag)node).getTagName().equals("SELECT")
                                && ((Tag)node).getAttribute("name") != null
                                && ((Tag)node).getAttribute("name").equals("topage"));
                    }
                };
                NodeList div_select=parser.extractAllNodesThatMatch(filter_num);
                NodeList div_option=new Parser(div_select.toHtml()).
                        extractAllNodesThatMatch(new TagNameFilter("option"));
                this.pagenum=div_option.size()/2;
            }

            parser.setInputHTML(data);
            NodeFilter filter= new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    return ((node instanceof Tag)
                            && !((Tag)node).isEndTag()
                            && ((Tag)node).getTagName().equals("DIV")
                            && ((Tag)node).getAttribute("class") != null
                            && ((Tag)node).getAttribute("class").equals("list_books"));
                }
            };

            //nodes  the list of the books
            NodeList nodes=parser.extractAllNodesThatMatch(filter);
            moreBooklist=new ArrayList<Bookinfo>(nodes.size());

            if(nodes.size()!=0)
            {
                message("extract success"+nodes.size());
                for(int i = 0;i < nodes.size();i++)
                {
                    Bookinfo cur_bki=new Bookinfo();


                    NodeList div_h3=new Parser(nodes.elementAt(i).toHtml()).
                            extractAllNodesThatMatch(new TagNameFilter("H3"))
                            .elementAt(0).getChildren();



                    String doc_type=div_h3.elementAt(0).toPlainTextString();

                    String title=div_h3.elementAt(1).toPlainTextString();
                    title=title.substring(0,2)+
                            Htmlutil.decodeUnicode(title.substring(2,title.length())).toString();

                    String link="http://202.195.195.137:8080/opac/"+
                            ((LinkTag)div_h3.elementAt(1)).getLink();

                    String ztflh=div_h3.elementAt(2).toPlainTextString();
                    ztflh= Htmlutil.decodeUnicode(ztflh.replaceAll(" ","")).toString();

                    message("extract date :" + doc_type + " " + title + " "+link+" " + ztflh);
                    cur_bki.set_doc_type(doc_type);
                    cur_bki.set_title(title);
                    cur_bki.set_link(link);
                    cur_bki.set_ztflh(ztflh);

                    NodeList div_p=new Parser(nodes.elementAt(i).toHtml()).
                            extractAllNodesThatMatch(new TagNameFilter("P"))
                            .elementAt(0).getChildren();

                    String gc_count=div_p.elementAt(1).getChildren().
                            elementAt(3).getText().replace(" ","");

                    String kj_count=div_p.elementAt(1).getChildren()
                            .elementAt(9).getText().replace(" ","");
                    message("extract gc_count: "+gc_count+"kj_count: "+kj_count);
                    cur_bki.set_gc_count(gc_count);
                    cur_bki.set_kj_count(kj_count);

                    String author=div_p.elementAt(2).toPlainTextString();
                    String publication=div_p.elementAt(4).toPlainTextString();

                    author=Htmlutil.decodeUnicode(author.replaceAll("[\\r\\n ]","")).toString();

                    publication=Htmlutil.decodeUnicode(publication.replaceAll("[\\r\\n ]",""))
                            .toString().replace("&nbsp;"," ");

                    message("extract author: "+author);
                    message("extract publication: "+publication);
                    cur_bki.set_author(author);
                    cur_bki.set_publication(publication);

                    moreBooklist.add(cur_bki);
                    Log.d("?","?");
                }

                Log.d("?","?");
            }
            else
                message("extract failure");

        }
        catch (Exception e)
        {
            message("exception:"+e.getMessage());
        }
        return moreBooklist;

    }
    private static void message( String szMsg )
    {
        try
        {
//            System.out.println(new String(szMsg.getBytes(ENCODE), System.getProperty("file.encoding")));
        }
        catch(Exception e )
        {

        }
    }
}






