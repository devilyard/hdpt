package dc.temperature;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.TextTitle;
/**
* Chart����
*/
public abstract class BaseChart 
{
 //����ͼƬ
 protected Image bgImg;
 //�ӱ����б�
 protected List<TextTitle> textTitleList;
 
 /**
  * ����ӱ���, Ҫ�ڼ̳�����ʵ��������������ڻ�ͼ֮ǰ������<br>
  * ���õ��ӱ����б� textTitleList
  * @param chart
  */
 protected abstract void addTitle(JFreeChart chart);
 
 /**
  * ���ñ���ͼƬ, Ҫ�ڼ̳�����ʵ��������������ڻ�ͼ֮ǰ������<br>
  * ���õ�����ͼƬ����bgImg
  * @param plot
  */
 protected abstract void setBgImg(Plot plot);
 
 /**
  * ��������ͼ�ı���ͼƬ
  * <pre>setBackgroundImg("images/bg.gif")</pre>
  * @param fileName �ļ���
  * @throws Exception
  */
 public void setBackgroundImg(HttpServletRequest request,String fileName) throws Exception
 {
  ImageIcon icon = new ImageIcon(request.getSession().getServletContext().getRealPath("/images"+fileName));
  bgImg = icon.getImage();
 }
 
 
 /**
  * ����ӱ���
  * @param title
  */
 public void addSubtitle(String title)
 {
  Font font = new Font("����", Font.PLAIN, 15);
  this.addSubtitle(title, font);
 }
 
 /**
  * ����ӱ��Ⲣ���ñ���Ϊָ������
  * @param title
  * @param font
  */
 public void addSubtitle(String title, Font font)
 {
  if(title == null )
   return;
  if(textTitleList == null)
   textTitleList = new ArrayList<TextTitle>();
  if(font == null)
   font = new Font("����", Font.PLAIN, 15);
  TextTitle tt = new TextTitle(title, font);
  textTitleList.add(tt);
 }
 
 
}