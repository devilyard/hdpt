package dc.temperature;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

/** Chart ���Ĵ�����*/
 
public class ChartProcessor extends BaseChart
{
 @SuppressWarnings("unused")
 private HttpServletRequest req;
 private HttpServletResponse rep;
 //�������б�
 private List<XYTextAnnotation> annoList;
 public static String AXIS_LEFT = "axis_left"; //����;
 private Map<String, NumberAxis> axisMap = new HashMap <String, NumberAxis>(); //��ӳ��
 private Map<String, Map<String, ChartBean>> axis_data_map = new HashMap <String, Map<String, ChartBean>>(); //ÿ��������ݼ�
 private Map<String, XYLineAndShapeRenderer> axit_render_map = new HashMap <String, XYLineAndShapeRenderer>(); //ÿ�����renderer��ӳ��
 private Map<String, Integer> axis_series_index_map = new HashMap <String, Integer>();//ÿ�����ϵ�������ӳ��
 //������ɫ
 private Color bgColor;
 //ˮƽ���ߵ���ɫ
 private Color bgHorizontalColor;
 //��ֱ������ɫ
 private Color bgVerticalColor;
 private NumberAxis domainAxis;
 public ChartProcessor(HttpServletResponse rep)
 {
  this(null, rep);
 }
 public ChartProcessor(HttpServletRequest req, HttpServletResponse rep)
 {
  this.req = req;
  this.rep = rep;
  annoList = new ArrayList<XYTextAnnotation>();
  //��ʼ����
  axisMap.put(ChartProcessor.AXIS_LEFT, new NumberAxis());
  //ÿ��������ݼ������ʼ��
  axis_data_map.put(ChartProcessor.AXIS_LEFT,
    new HashMap<String, ChartBean>());
  //ÿ�����renderer��ʼ��
  axit_render_map.put(ChartProcessor.AXIS_LEFT,
    new XYLineAndShapeRenderer());
  //ÿ�����ϵ�����������ʼ��
  axis_series_index_map.put(ChartProcessor.AXIS_LEFT, new Integer(0));
 }
 /**
  * ��ͼ����
  * @param title
  *            ͼƬ�ı��⣨ͼƬ��������
  * @param xDesc
  *            x �������
  * @param ylDesc
  *            ��y�������
  * @param yrDesc
  *            ��Y�������
  * @param showLineDesc
  *            �Ƿ���ʾÿ���ߵ���������ÿ�����ߵĺ���,��x�������г�ÿ���ߵĺ��壩
  * @param width
  *            ���ͼƬ�Ŀ��
  * @param height
  *            ���ͼƬ�ĸ߶�
  * @throws Exception
  */
 public String createChart(String title, String xDesc, String ylDesc,
   String ylsDesc, String yrDesc, boolean showLineDesc, int width,
   int height) throws Exception
 {
  String chartPath = null;
  if (rep == null)
  {
   throw new Exception("response Ϊ��! �������� response! ");
  }
  JFreeChart chart = ChartFactory.createXYLineChart(title, xDesc, ylDesc,createDataset(ChartProcessor.AXIS_LEFT),PlotOrientation.VERTICAL, showLineDesc, true, false);
  //����ӱ���
  this.addTitle(chart);
  XYPlot plot = chart.getXYPlot();
  chart.setTextAntiAlias(false);
  //���ñ���ͼƬ
  this.setBgImg(plot);
  //��ӱ�����
  for (XYTextAnnotation anno : annoList)
  {
   plot.addAnnotation(anno);
  }
  GradientPaint bg = new GradientPaint(0, 1000, Color.white, 0, 800,Color.white);  //���ñ���ɫ
  plot.setBackgroundPaint(bg);
  plot.setRangeGridlinePaint(bgHorizontalColor);
  plot.setDomainGridlinePaint(bgVerticalColor);
  /**
   * ���ø�����
   */
  if (axisMap.get(ChartProcessor.AXIS_LEFT) != null
    && axis_data_map.get(ChartProcessor.AXIS_LEFT) != null
    && !axis_data_map.get(ChartProcessor.AXIS_LEFT).isEmpty())
  {
   NumberAxis axisLeft = axisMap.get(ChartProcessor.AXIS_LEFT);
   axisLeft.setLabel(ylDesc);
   plot.setRangeAxis(0, axisLeft);
   plot.setRenderer(0, axit_render_map.get(ChartProcessor.AXIS_LEFT));
  }
  
  if (domainAxis != null)
  {
   domainAxis.setLabel(xDesc);
   domainAxis.setAutoRangeIncludesZero(false);
   domainAxis.setLowerMargin(0);
   plot.setDomainAxis(domainAxis);
  }
  Shape shape = new Rectangle(20, 10);
  ChartEntity entity = new ChartEntity(shape);
  StandardEntityCollection coll = new StandardEntityCollection();
  coll.add(entity);
  ChartRenderingInfo info = new ChartRenderingInfo(coll);
  chartPath = basesaveChartAsPNG(chart, width, height,info, req.getSession());
  axis_data_map.get(ChartProcessor.AXIS_LEFT).clear();
  return chartPath;
 }
 
 public  String basesaveChartAsPNG(JFreeChart chart, int width, int height,
	     ChartRenderingInfo info, HttpSession session) throws IOException {
	     String dir = session.getServletContext().getRealPath("/");
	     if (chart == null) {
	     throw new IllegalArgumentException("Null 'chart' argument.");   
	     }
	     String prefix = ServletUtilities.getTempFilePrefix();
	     if (session == null) {
	     prefix = ServletUtilities.getTempOneTimeFilePrefix();
	     }
	     File tempFile = File.createTempFile(prefix, ".png",  new File(dir+"/jfreechart"));
	     ChartUtilities.saveChartAsPNG(tempFile, chart, width, height, info);
//	     if (session != null) {
//	     ServletUtilities.registerChartForDeletion(tempFile, session);
//	     }
	     return tempFile.getName();
	   }
 /**
  * ����ĳ������Ҫ��ʾ������<br>
  * @param axisName
  *            �������
  * @param lineName
  *            �ߵ�����
  * @param x
  *            ������
  * @param y
  *            ������
  * @return �����Ƿ����ɹ�
  */
 public boolean addData(String axisName, String lineName, Number x, Number y)
 {
  if (lineName == null || "".equals(lineName.trim()) || x == null
    || y == null)
   return false;
  if (axisName == null || axisMap.get(axisName) == null)
   return false;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (map.containsKey(lineName))
   {
    ChartBean seriesbean = map.get(lineName);
    if (seriesbean.getSeries() != null)
     seriesbean.getSeries().add(x, y);
   }
   else
   {
    XYSeries series = new XYSeries(lineName);
    series.add(x, y);
    ChartBean bean = new ChartBean();
    bean.setSeries(series);
    map.put(lineName, bean);
   }
  }
  return true;
 }
 /**
  * ����ĳ���������Ƿ���ʾ���������( ƽ�����߻��߱�������������,����ĳ�����ϵ������ߵĵ�)
  * @param lineShapeVisible(true:��ʾ
  *            false:����ʾ)
  */
 public void setLineShapeVisible(String axisName, boolean lineShapeVisible)
 {
  if (axisName != null && !"".equals(axisName.trim()))
  {
   if (axit_render_map.get(axisName) != null)
   {
    axit_render_map.get(axisName).setBaseShapesVisible(
      lineShapeVisible);
   }
  }
 }
 /**
  * ����ĳ�������ĳ�������Ƿ���ʾ���������
  * @param axisName
  *            ������
  * @param lineName
  *            ������
  * @param flag
  */
 public void setLineShapeVisible(String axisName, String lineName,
   boolean flag)
 {
  if (lineName == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (map.containsKey(lineName))
   {
    ChartBean bean = map.get(lineName);
    bean.setShapeVisible(flag);
   }
   else
   {
    ChartBean bean = new ChartBean();
    bean.setShapeVisible(flag);
    map.put(lineName, bean);
   }
  }
 }
 /**
  * ����ĳ�������ĳ�������Ƿ�������
  * @param lineName
  *            ������
  * @param flag
  */
 public void setLineDash(String axisName, String lineName, boolean flag)
 {
  if (lineName == null)
   return;
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (map.containsKey(lineName))
   {
    ChartBean bean = map.get(lineName);
    bean.setLineDash(flag);
   }
   else
   {
    ChartBean bean = new ChartBean();
    bean.setLineDash(flag);
    map.put(lineName, bean);
   }
  }
 }
 /**
  * ����ĳ�������ĳ�������Ƿ���ʾ��������
  * @param lineName
  *            ������
  * @param flag
  */
 public void setLineVisible(String axisName, String lineName, boolean flag)
 {
  if (lineName == null)
   return;
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (map.containsKey(lineName))
   {
    ChartBean bean = map.get(lineName);
    bean.setLineVisible(flag);
   }
   else
   {
    ChartBean bean = new ChartBean();
    bean.setLineVisible(flag);
    map.put(lineName, bean);
   }
  }
 }
 /**
  * ���ñ�����ɫ
  * @param color
  */
 public void setBackgroundColor(Color color)
 {
  if (color != null)
   this.bgColor = color;
 }
 /**
  * ����ˮƽ���ߵ���ɫ
  * @param color
  */
 public void setBgHorizontalLineColor(Color color)
 {
  if (color != null)
  {
   this.bgHorizontalColor = color;
  }
 }
 /**
  * ������ֱ���ߵ���ɫ
  * @param color
  */
 public void setBgVerticalLineColor(Color color)
 {
  if (color != null)
  {
   this.bgVerticalColor = color;
  }
 }
 /**
  * ����ĳ��������ߵ���ɫ
  * @param lineName
  *            Ҫ������ɫ���ߵ�����
  * @param color
  *            �ߵ���ɫ
  */
 public void setLineColor(String axisName, String lineName, Color color)
 {
  if (lineName == null)
   return;
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (map.containsKey(lineName))
   {
    ChartBean bean = map.get(lineName);
    bean.setLineColor(color);
   }
   else
   {
    ChartBean bean = new ChartBean();
    bean.setLineColor(color);
    map.put(lineName, bean);
   }
  }
 }
 /**
  * ����ĳ��������ߵĵ����״
  * @param lineName
  *            Ҫ���õ��ߵ�����
  * @param shape
  *            ��״
  */
 public void setLineShape(String axisName, String lineName, Shape shape)
 {
  if (lineName == null)
   return;
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (!map.containsKey(lineName))
   {
    ChartBean bean = new ChartBean();
    bean.setShape(shape);
    map.put(lineName, bean);
   }
   else
   {
    ChartBean bean = map.get(lineName);
    bean.setShape(shape);
   }
  }
 }
 /**
  * ����ĳ��������ߵĿ��
  * @param lineName
  *            Ҫ���õ��ߵ�����
  * @param width
  *            ���
  */
 public void setLineWidth(String axisName, String lineName, float width)
 {
  if (lineName == null)
   return;
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  if (axis_data_map.get(axisName) != null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if (!map.containsKey(lineName))
   {
    ChartBean bean = new ChartBean();
    bean.setLineWidth(width);
   }
   else
   {
    ChartBean bean = map.get(lineName);
    bean.setLineWidth(width);
   }
  }
 }
 /**
  * ����ĳ��������ߵĵ�������ɫ
  * @param lineName
  *            Ҫ���õ��ߵ�����
  * @param color
  *            ��ɫ
  */
 public void setLineShapeFilledColor(String axisName, String lineName, Color color)
 {
  if(lineName == null)
   return ;
  if(axisName == null || axisMap.get(axisName) == null)
   return ;
  if(axis_data_map.get(axisName)!= null)
  {
   Map<String, ChartBean> map = axis_data_map.get(axisName);
   if(!map.containsKey(lineName))
   {
    ChartBean bean = new ChartBean();
    bean.setShapeFilled(true);
    bean.setShapeColor(color);
    map.put(lineName, bean);
   }
   else
   {
    ChartBean bean = map.get(lineName);
    bean.setShapeFilled(true);
    bean.setShapeColor(color);
   }
  }
  
 }
 /**
  * ����ĳ��Y��ļ��
  * 
  * <pre>
  * �����������Y��������̶�֮����Ϊ10 ��setYUnit(MulAxisLineChart.AXIS_LEFT,10)
  * </pre>
  * 
  * @param unit
  */
 public void setYUnit(String axisName, double unit)
 {
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  NumberTickUnit tickUnit = new NumberTickUnit(unit);
  axisMap.get(axisName).setAutoTickUnitSelection(false);
  axisMap.get(axisName).setTickUnit(tickUnit);
 }
 /**
  * ����X��ķ�Χ
  * 
  * <pre>
  * ���������X��ķ�ΧΪ0-30 ��setXRange(0,30)
  * </pre>
  */
 public void setXRange(double lower, double upper)
 {
  if (domainAxis == null)
   domainAxis = new NumberAxis();
  domainAxis.setRange(lower, upper);
 }
 /**
  * ����Y��ķ�Χ
  * 
  * <pre>
  * �����������Y��ķ�ΧΪ0-30 ��setYRangeLeft(MulAxisLineChart.AXIS_LEFT,0,30)
  * </pre>
  */
 public void setYRange(String axisName, double lower, double upper)
 {
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  axisMap.get(axisName).setRange(lower, upper);
 }
 /**
  * ����X��ļ��
  * 
  * <pre>
  * ���������X��������̶�֮����Ϊ10 ��setXUnit(10)
  * </pre>
  * 
  * @param unit
  */
 public void setXUnit(double unit)
 {
  if (domainAxis == null)
  {
   domainAxis = new NumberAxis();
  }
  NumberTickUnit tickUnit = new NumberTickUnit(unit);
  domainAxis.setAutoTickUnitSelection(false);
  domainAxis.setTickUnit(tickUnit);
 }
 /**
  * ����X���Ƿ�ɼ�
  * @param flag
  */
 public void setXVisible(boolean flag)
 {
  if (domainAxis == null)
  {
   domainAxis = new NumberAxis();
  }
  domainAxis.setVisible(flag);
 }
 /**
  * ����ĳ��Y���Ƿ�ɼ�
  * @param flag
  */
 public void setYVisible(String axisName, boolean flag)
 {
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  axisMap.get(axisName).setVisible(flag);
 }
 /**
  * ����X���Ƿ���㿪ʼ
  * @param flag
  *            true:�� false:��
  */
 public void setXStartWithZero(boolean flag)
 {
  if (domainAxis == null)
  {
   domainAxis = new NumberAxis();
  }
  domainAxis.setAutoRangeIncludesZero(flag);
 }
 /**
  * ����ĳ��Y���Ƿ���㿪ʼ
  * @param flag
  *            true:�� false:��
  */
 public void setYStartWithZero(String axisName, boolean flag)
 {
  if (axisName == null || axisMap.get(axisName) == null)
   return;
  axisMap.get(axisName).setAutoRangeIncludesZero(flag);
 }
 /**
  * ����ͼƬ�ı�����
  * @param value
  *            Ҫ��ʾ����
  * @param x
  *            Ҫ��ʾ����������ͼƬ�еĺ�����
  * @param y
  *            Ҫ��ʾ����������ͼƬ�е�������
  */
 public void addBackgroundValue(String value, double x, double y)
 {
  Font font = new Font("����", Font.PLAIN, 15);
  this.addBackgroundValue(value, x, y, font);
 }
 /**
  * ����ͼƬ�ı�����
  * @param value
  *            Ҫ��ʾ����
  * @param x
  *            Ҫ��ʾ����������ͼƬ�еĺ�����
  * @param y
  *            Ҫ��ʾ����������ͼƬ�е�������
  * @param font
  *            Ҫ��ʾ�ֵ���ʽ
  */
 public void addBackgroundValue(String value, double x, double y, Font font)
 {
  if (value == null)
   value = new String("");
  if (font == null)
  {
   font = new Font("����", Font.PLAIN, 15);
  }
  addBackgroundValue(value, x, y, font, null);
 }
 public void addBackgroundValue(String value, double x, double y, Font font,
   Color color)
 {
  if (value == null)
   value = new String("");
  XYTextAnnotation anno = new XYTextAnnotation(value, x, y);
  if (font == null)
  {
   font = new Font("����", Font.PLAIN, 15);
  }
  if (color == null)
   color = Color.black;
  anno.setFont(font);
  anno.setTextAnchor(TextAnchor.BASELINE_CENTER);
  anno.setPaint(color);
  annoList.add(anno);
 }
 /**
  * �������������ǹ������������ݼ� ������ɺ����������������߳�ʼ������
  * @return
  */
 private XYSeriesCollection createDataset(String axisName)
 {
  if (axisName == null)
   return null;
  XYSeriesCollection collection = new XYSeriesCollection();
  if (axis_data_map.get(axisName) != null
    && !axis_data_map.get(axisName).isEmpty())
  {
   if (axis_data_map.get(axisName) == null)
    return null;
   Integer index = axis_series_index_map.get(axisName);
   for (ChartBean bean : axis_data_map.get(axisName).values())
   {
    if (bean.getSeries() == null)
     continue;
    collection.addSeries(bean.getSeries());
    if (index != null)
    {
     bean.setIndex(index);
     index++;
    }
   }
   initSeries(axisName);
  }
  return collection;
 }
 /**
  * ���漸������������������ĳ�ʼ���� Ҫ�ڹ��������ݼ����� ֪��ÿ�����ߵ������ſ�������
  */
 private void initSeries(String axisName)
 {
  if (axisName == null)
   return;
  if (axis_data_map.get(axisName) != null
    && !axis_data_map.get(axisName).isEmpty())
  {
   XYLineAndShapeRenderer renderer = axit_render_map.get(axisName);
   for (ChartBean bean : axis_data_map.get(axisName).values())
   {
    renderer.setSeriesStroke(bean.getIndex(), bean.getLineStroke()); //�ߵıʴ�
    renderer.setUseFillPaint(true);
    if (bean.getLineColor() != null) //�ߵ���ɫ
    {
     renderer.setSeriesPaint(bean.getIndex(), bean
       .getLineColor());
    }
    if (bean.getShapeVisible() != null) //���Ƿ�ɼ�
    {
     renderer.setSeriesShapesVisible(bean.getIndex(), bean
       .getShapeVisible());
    }
    if (bean.getLineVisible() != null)
    {
     renderer.setSeriesLinesVisible(bean.getIndex(), bean
       .getLineVisible()); //���Ƿ�ɼ�
    }
    if (bean.getShape() != null)
    {
     renderer.setSeriesShape(bean.getIndex(), bean.getShape()); //���õ����״
    }
    if (bean.getShapeFilled() && bean.getShapeColor() != null) //���öԵ�ľ������ɫ
    {
     renderer.setSeriesFillPaint(bean.getIndex(), bean
       .getShapeColor());
    }
    else if (!bean.getShapeFilled() && renderer.getUseFillPaint()) //û�����öԵ�ľ������ɫ����Ĭ�����ߵ���ɫ���
    {
     renderer.setSeriesFillPaint(bean.getIndex(), bean
       .getLineColor());
    }
   }
  }
 }
 @Override
 protected void addTitle(JFreeChart chart)
 {
  if (textTitleList != null)
  {
   for (TextTitle tt : textTitleList)
   {
    chart.addSubtitle(tt);
   }
  }
 }
 @Override
 protected void setBgImg(Plot plot)
 {
  if (bgImg != null)
   plot.setBackgroundImage(bgImg);
 }


}
