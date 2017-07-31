package dc.util;

public class PageModel
{
  private int page = 1;
  private int pagesize;
  private int pages;
  private int recordCount;
  private int beforeCount;
  private int prepage;
  private int nextpage;
  private String url;
  private Integer[] slider;
  private int pagecount = 1;

  public PageModel(PageBuilder pb) {
    this.page = pb.page();
    this.pagesize = pb.itemsPerPage();
    this.pages = pb.pages();
    this.slider = pb.slider();
    this.recordCount = pb.items();
    this.prepage = pb.previousPage();
    this.nextpage = pb.nextPage();
  }

  public int getBeforeCount() {
    return (this.page - 1) * this.pagesize;
  }

  public int getPage()
  {
    return this.page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPagesize()
  {
    return this.pagesize;
  }

  public void setPagesize(int pagesize) {
    this.pagesize = pagesize;
  }

  public int getPagecount()
  {
    if (this.pagesize != 0) {
      return (this.recordCount + this.pagesize - 1) / this.pagesize;
    }
    return 0;
  }

  public void setPageCount(int pagecount) {
    this.pagecount = pagecount;
  }

  public int getRecordCount()
  {
    return this.recordCount;
  }

  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }

  public int getPages() {
    return this.pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getPrepage() {
    return this.prepage;
  }

  public void setPrepage(int prepage) {
    this.prepage = prepage;
  }

  public int getNextpage() {
    return this.nextpage;
  }

  public void setNextpage(int nextpage) {
    this.nextpage = nextpage;
  }

  public Integer[] getSlider() {
    return this.slider;
  }

  public void setSlider(Integer[] slider) {
    this.slider = slider;
  }

  public void setBeforeCount(int beforeCount) {
    this.beforeCount = beforeCount;
  }

  public void setPagecount(int pagecount) {
    this.pagecount = pagecount;
  }
}