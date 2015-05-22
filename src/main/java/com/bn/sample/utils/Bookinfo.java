package com.bn.sample.utils;

/**
 * Created by chen-pc on 2015/3/10.
 */
public class Bookinfo
{
    private String doc_type;
    private String title;
    private String link;
    private String ztflh;
    private String gc_count;
    private String kj_count;
    private String author;
    private String publication;

    public void set_doc_type(String doc_type)
    {
        this.doc_type=doc_type;
    }
    public void set_title(String title)
    {
        this.title=title;
    }
    public void set_link(String link)
    {
        this.link=link;
    }
    public void set_ztflh(String ztflh)
    {
        this.ztflh=ztflh;
    }
    public void set_gc_count(String gc_count)
    {
        this.gc_count=gc_count;
    }
    public void set_kj_count(String kj_count)
    {
        this.kj_count=kj_count;
    }
    public void set_author(String author)
    {
        this.author=author;
    }
    public void set_publication(String publication)
    {
        this.publication=publication;
    }


    public String get_doc_type()
    {
        return this.doc_type;
    }
    public String get_title()
    {
        return this.title;
    }
    public String get_link()
    {
        return this.link;
    }
    public String get_ztflh()
    {
        return this.ztflh;
    }
    public String get_gc_count()
    {
        return this.gc_count;
    }
    public String get_kj_count()
    {
        return this.kj_count;
    }
    public String get_author()
    {
        return this.author;
    }
    public String get_publication()
    {
        return this.publication;
    }
}
