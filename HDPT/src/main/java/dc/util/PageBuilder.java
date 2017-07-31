package dc.util;

import com.alibaba.fastjson.JSONObject;

public class PageBuilder
{
  public static final int DEFAULT_ITEMS_PER_PAGE = 10;
  public static final int DEFAULT_SLIDER_SIZE = 7;
  public static final int UNKNOWN_ITEMS = 2147483647;
  private int page;
  private int items;
  private int itemsPerPage;

  public PageBuilder()
  {
    this(0);
  }

  public PageBuilder(int n)
  {
    this.page = 0;

    this.items = 0;

    this.itemsPerPage = (n > 0 ? n : 10);
  }

  public int pages()
  {
    return (int)Math.ceil(this.items / this.itemsPerPage);
  }

  public int page()
  {
    return this.page;
  }

  public int page(int n)
  {
    return this.page = calcPage(n);
  }

  public int items()
  {
    return this.items;
  }

  public int items(int n)
  {
    this.items = (n >= 0 ? n : 0);

    page(this.page);

    return this.items;
  }

  public int itemsPerPage()
  {
    return this.itemsPerPage;
  }

  public int itemsPerPage(int n)
  {
    int tmp = this.itemsPerPage;

    this.itemsPerPage = (n > 0 ? n : 10);

    if (this.page > 0)
    {
      page((int)(this.page - 1 * tmp / this.itemsPerPage) + 1);
    }

    return this.itemsPerPage;
  }

  public int offset()
  {
    return this.page > 0 ? this.itemsPerPage * (this.page - 1) : 0;
  }

  public int length()
  {
    if (this.page > 0)
    {
      return Math.min(this.itemsPerPage * this.page, this.items) - this.itemsPerPage * (this.page - 1);
    }

    return 0;
  }

  public int beginIndex()
  {
    if (this.page > 0)
    {
      return this.itemsPerPage * (this.page - 1) + 1;
    }

    return 0;
  }

  public int endIndex()
  {
    if (this.page > 0)
    {
      return Math.min(this.itemsPerPage * this.page, this.items);
    }

    return 0;
  }

  public int beginIndexAsc()
  {
    if (this.page > 0)
    {
      return this.items - this.itemsPerPage * this.page;
    }

    return 0;
  }

  public int endIndexAsc()
  {
    if (this.page > 0)
    {
      return Math.max(1, this.items - (this.page - 1) * this.itemsPerPage);
    }

    return 0;
  }

  public int showItem(int n)
  {
    return page(n / this.itemsPerPage + 1);
  }

  public int firstPage()
  {
    return calcPage(1);
  }

  public int lastPage()
  {
    return calcPage(pages());
  }

  public int previousPage()
  {
    return calcPage(this.page - 1);
  }

  public int previousPage(int n)
  {
    return calcPage(this.page - n);
  }

  public int nextPage()
  {
    return calcPage(this.page + 1);
  }

  public int nextPage(int n)
  {
    return calcPage(this.page + n);
  }

  public boolean isDisabledPage(int n)
  {
    return (n < 1) || (n > pages()) || (n == this.page);
  }

  public Integer[] slider()
  {
    return slider(7);
  }

  public Integer[] slider(int n)
  {
    int pages = pages();

    if ((pages < 1) || (n < 1))
    {
      return new Integer[0];
    }

    if (n > pages)
    {
      n = pages;
    }

    Integer[] slider = new Integer[n];

    int first = this.page - (n - 1) / 2;

    if (first < 1)
    {
      first = 1;
    }

    if (first + n - 1 > pages)
    {
      first = pages - n + 1;
    }

    for (int i = 0; i < n; i++)
    {
      slider[i] = new Integer(first + i);
    }

    return slider;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("PageBuilder: page ");

    if (pages() < 1)
    {
      sb.append(page());
    }
    else
    {
      Integer[] slider = slider();

      for (int i = 0; i < slider.length; i++)
      {
        if (isDisabledPage(slider[i].intValue()))
        {
          sb.append('[').append(slider[i]).append(']');
        }
        else
        {
          sb.append(slider[i]);
        }

        if (i < slider.length - 1)
        {
          sb.append('\t');
        }

      }

    }

    sb.append(" of ").append(pages()).append(",\n");

    sb.append("    Showing items ").append(beginIndex()).append(" to ").append(endIndex()).append(" (total ").append(items()).append(" items), ");

    sb.append("offset=").append(offset()).append(", length=").append(length());

    return sb.toString();
  }

  protected int calcPage(int n)
  {
    int pages = pages();

    if (pages > 0)
    {
      return n > pages ? pages : n < 1 ? 1 : n;
    }

    return 0;
  }

  public JSONObject toJson()
  {
    JSONObject json = new JSONObject();
    json.put("page", Integer.valueOf(page()));
    json.put("pagesize", Integer.valueOf(itemsPerPage()));
    json.put("pages", Integer.valueOf(pages()));
    json.put("slider", slider());
    json.put("recordCount", Integer.valueOf(items()));
    json.put("prepage", Integer.valueOf(previousPage()));
    json.put("nextpage", Integer.valueOf(nextPage()));
    return json;
  }
}