package dc.util;
/**
 * 工具类：分页类
 * @author WY
 * @time 2012-03-09
 *
 */
public class PageCount {
	/**
	 * 总页数
	 */
	private int totalPage = 0;
	/**
	 * 总行数
	 */
	private int totalRows = 0;
	/**
	 * 每页显示行数
	 */
	private int perPage = 0;
	/**
	 * 当前页
	 */
	private int currentPage = 0;
	/**
	 * 当前页开始行
	 */
	private int startRow = 0;
	/**
	 * 当前页结束行
	 */
	private int endRow = 0;
	/**
	 * 首行偏移量(0或1)，即开始行为第1行还是第0行，默认为0
	 */
	private int baseNum = 0;
	/**
	 * 构造函数
	 * @param totalRows	总行数
	 * @param perPage	每页显示行数
	 */
	public PageCount(int totalRows, int perPage){
		this(totalRows, perPage, 1);
	}
	/**
	 * 构造函数
	 * @param totalRows		总行数
	 * @param perPage		每页显示行数
	 * @param currentPage	当前页数
	 */
	public PageCount(int totalRows, int perPage, int currentPage){
		if(totalRows != 0){
			this.totalRows = totalRows;
			this.perPage = perPage;
			this.currentPage = 1;
			this.init();
			this.setPage(currentPage);
		}else{
			this.totalPage = 1;
			this.totalRows = 0;
			this.currentPage = 1;
			this.startRow = 0;
			this.endRow = -1;
		}
	}
	
	/**
	 * 初始化分页数据
	 */
	private void init(){
		if(totalRows==0 || perPage==0){
			return;
		}
		totalPage = (totalRows%perPage)==0? totalRows/perPage: (totalRows/perPage)+1;
		currentPage = 1;
		startRow = 0;
		endRow = startRow+perPage>totalRows? totalRows-1: startRow+perPage-1;
	}
	/**
	 * 设置页码，更新开始和结束行
	 * @param pageNum
	 */
	public void setPage(int pageNum){
		if(pageNum > totalPage){
			currentPage = totalPage;
		}else if(pageNum < 1){
			currentPage = 1;
		}else{
			currentPage = pageNum;
		}
		startRow = (currentPage-1)*perPage;
		endRow = pageNum==totalPage? totalRows-1: startRow+perPage-1;
	}
	/**
	 * 返回是否为第一页
	 * @return
	 */
	public boolean isFirstPage(){
		if(currentPage == 1){
			return true;
		}
		return false;
	}
	/**
	 * 返回是否为最后一页
	 * @return
	 */
	public boolean isLastPage(){
		if(currentPage == totalPage){
			return true;
		}
		return false;
	}
	
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		this.init();
	}
	public int getPerPage() {
		return perPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public int getBaseNum() {
		return baseNum;
	}
	public void setBaseNum(int baseNum) {
		this.baseNum = baseNum;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public int getStartRow() {
		return startRow + baseNum;
	}
	public int getEndRow() {
		return endRow + baseNum;
	}
}
