package dc.temperature;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.date.DateUtilities;




/**���µ�ʵ���� */
 
public class TwdChartService 
{
 
 private int top_length = 3; //���˵ĸ߶�
 private int top_rows=10;  //����������
 private double top_row_height=top_length/(double)top_rows;  //����ÿ�еĸ߶�
 
 private int bottom_length =3; //�ײ��ĸ߶�
 private int bottom_rows=10 ;//�ײ�������
 private  double bottom_row_height=bottom_length/(double)bottom_rows ; //�ײ�ÿ�еĸ߶�
 
 private int grid_left = 6; //��Y�������
 private int grid_right =0; //��Y�������
 
 private int x_unit = 6; //���ٸ�С�������һ�������
 private double grid_row_height = 0.2; //������ÿ�����ӵĸ߶�
 private double grid_col_width=1.0;
 private int y_grid_length=8; //y�����и��ӵĳ��� 
 private int x_grid_length=42; //x�����и��ӵĳ���
 private int grid_rows=(int)Math.round(y_grid_length/grid_row_height); //�м���ӵ�����
 
 private int start_temperature=34;  //��ʼ������   �ӵײ��߶ȿ�ʼ����
 private int start_pulse=20;       //��ʼ������    �ӵײ��߶ȿ�ʼ����
 
 private int x_length = grid_left+x_grid_length+grid_right; //x��ĳ���
 private int y_length = top_length+y_grid_length+bottom_length; //y��ĳ���
 
 Font font = new Font("SansSerif", Font.PLAIN, 12);
 private double round_size = 8d;
 
 public void  initAllData(ChartProcessor chart,Map map)
 {
  initTopChart(chart, map);
  initGridChart(chart);
  initBottomChart(chart, map);
  initGridDynamicData(chart, map);
  initChart(chart);
  
 }
 
 /**
  * ��ʼ�������ᡢ�߿�
  * @param chart
  */
 private void initChart(ChartProcessor chart)
 {
  //��ʼ����������
  chart.setXRange(0, x_length);
  chart.setXUnit(1);
  chart.setXVisible(false);
  chart.setBgHorizontalLineColor(Color.white);
  chart.setBgVerticalLineColor(Color.white);
  chart.setYUnit(ChartProcessor.AXIS_LEFT, 1);
  chart.setYRange(ChartProcessor.AXIS_LEFT, 0, y_length);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_left", 0,y_length);
  chart.setLineVisible(ChartProcessor.AXIS_LEFT, "basic_left", false);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, "basic_left",false);
  chart.setYVisible(ChartProcessor.AXIS_LEFT, false);
  //�����߿�
  //��
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_frame_line_top", 0,y_length);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_frame_line_top",x_length, y_length);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_frame_line_top", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_frame_line_top",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_frame_line_top",ChartShape.LINE_WIDE);
  
  //סԺ����
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_top", 0,bottom_length+y_grid_length+top_row_height*5);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_top",x_length,bottom_length+y_grid_length+top_row_height*5);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_top", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_line_top",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_line_top",ChartShape.LINE_NORMAL);
  
  //��
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_frame_line_bottom", 0,0.01);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_frame_line_bottom",x_length, 0.01);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_frame_line_bottom", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_frame_line_bottom", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_frame_line_bottom", ChartShape.LINE_NORMAL);
  //��
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line", 0, 0);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line", 0,y_length);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, "basic_y_line",false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_y_line",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_y_line",ChartShape.LINE_WIDE);
  
  //��2
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line_sec",grid_left, 0);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line_sec",grid_left, y_length-top_row_height *5);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_y_line_sec", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_y_line_sec",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_y_line_sec",ChartShape.LINE_NORMAL);
  //��
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line_right2",x_length-0.0005 ,0);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_y_line_right2",x_length-0.0005, y_length);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_y_line_right2", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_y_line_right2",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_y_line_right2",ChartShape.LINE_NORMAL);
  //����
  chart.addData(ChartProcessor.AXIS_LEFT, "bottom_horizatal_line", 0,bottom_length);
  chart.addData(ChartProcessor.AXIS_LEFT, "bottom_horizatal_line",x_length-grid_right, bottom_length);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"bottom_horizatal_line", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "bottom_horizatal_line",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "bottom_horizatal_line",ChartShape.LINE_NORMAL);
  //ʱ��߿�
  chart.addData(ChartProcessor.AXIS_LEFT, "top_horizatal_line", 0,y_length - top_length);
  chart.addData(ChartProcessor.AXIS_LEFT, "top_horizatal_line",x_length-grid_right, y_length - top_length);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"top_horizatal_line", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "top_horizatal_line",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "top_horizatal_line",ChartShape.LINE_NORMAL);
  //��ʼ������������������
  for (int i = 0; i<y_grid_length; i++)
  {
   Font font = new Font("SansSerif", Font.PLAIN, 12); 
   String value_left = (start_temperature + i+1 ) + "";
   String value_left_sec = (start_pulse + (i+1)* 20) + "";
   double y =bottom_length+ i + 0.8;
   chart.addBackgroundValue(value_left, grid_left / 6 * 5, y, font,Color.BLUE);
   chart.addBackgroundValue(value_left_sec, grid_left / 4 + 0.05, y,font, Color.RED);
  }
  
  //ʱ�� �� ���� �������  ���� �� �ײ� �������
  //�������
  for (int i = 0; i <= x_grid_length; i++)
  {
   float chartShape = ChartShape.LINE_THIN;
   double temp_top_height=y_length - top_length+top_row_height*2;
   double temp_bottom_height=bottom_length-bottom_row_height*2;
   if (i % 6 == 0)
   {
    chartShape = ChartShape.LINE_WIDE;
    temp_top_height=y_length - top_length+top_row_height*5;
    temp_bottom_height=0;
   }
   String line_name = "line_time2top_vertical_sec_" + i;
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left + i,y_length-top_length);
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left + i,temp_top_height);
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, line_name,false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.BLACK);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT, line_name, chartShape);
   
   String line_name1 = "line_breather2bottom_vertical_sec_" + i;
   chart.addData(ChartProcessor.AXIS_LEFT, line_name1, grid_left + i,bottom_length);
   chart.addData(ChartProcessor.AXIS_LEFT, line_name1, grid_left + i,temp_bottom_height);
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, line_name1,false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name1,Color.BLACK);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT, line_name1, chartShape);
  }
  
  
 }
 
 /*****
  *���������ʼ�� 
  */
 private void initTopChart(ChartProcessor chart,Map map)
 {
  chart.addBackgroundValue("�� ����", grid_left /3, bottom_length+y_grid_length+top_row_height*5 + 0.1, font);
  chart.addBackgroundValue("", grid_left / 2+x_unit/2, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("�� Ժ �� �ڣ�", grid_left / 2+x_unit/3+x_unit, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("", grid_left / 2+x_unit*2, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("�� �ң�", grid_left / 2+x_unit*3, bottom_length+y_grid_length+top_row_height*5 + 0.1, font);
  chart.addBackgroundValue("", grid_left / 2+x_unit*3+x_unit, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("�� �ţ�", x_length-x_unit*2-3, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("",x_length-x_unit*2, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("�����ţ�", x_length-x_unit-2, bottom_length+y_grid_length+top_row_height*5 + 0.1, font);
  chart.addBackgroundValue(map.get("BZBAH")+"", x_length-x_unit/2-2, bottom_length+y_grid_length+top_row_height*5  + 0.1, font);
  chart.addBackgroundValue("��  ��  ��", x_length/2, bottom_length+y_grid_length+top_row_height*6+0.2, new Font("SansSerif", Font.PLAIN, 15));
  chart.addBackgroundValue("������һҽԺ", x_length/2, bottom_length+y_grid_length+top_row_height*8 + 0.1, new Font("SansSerif", Font.PLAIN, 20));
  
  /*****
   * �������ݳ�ʼ��
   **/
  chart.addBackgroundValue("ס  Ժ  ��  ��", grid_left / 2,bottom_length+y_grid_length+top_row_height*4 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_date", 0,bottom_length+y_grid_length+top_row_height*4);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_date",x_length-grid_right, bottom_length+y_grid_length+top_row_height*4);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_time_date", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_line_time_date",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_line_time_date",ChartShape.LINE_THIN);
  
  chart.addBackgroundValue("�� �� �� �� ��", grid_left / 2, bottom_length+y_grid_length+top_row_height*3 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_in_day", 0,bottom_length+y_grid_length+top_row_height*3);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_in_day",x_length-grid_right, bottom_length+y_grid_length+top_row_height*3);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_time_in_day", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_time_in_day", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_time_in_day", ChartShape.LINE_THIN);
  chart.addBackgroundValue("��            ��", grid_left / 2, bottom_length+y_grid_length+top_row_height*2+0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_ops_day", 0,bottom_length+y_grid_length+top_row_height*2);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_ops_day",x_length-grid_right, bottom_length+y_grid_length+top_row_height*2);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_time_ops_day", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_time_ops_day", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_time_ops_day", ChartShape.LINE_THIN);
  chart.addBackgroundValue("ʱ           ��", grid_left / 2, bottom_length+y_grid_length+top_row_height*1+0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_op_day", 0,bottom_length+y_grid_length+top_row_height*1);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_time_op_day",x_length-grid_right,bottom_length+y_grid_length+top_row_height*1);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_time_op_day", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_time_op_day", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_time_op_day", ChartShape.LINE_THIN);
  //ʱ��ڵ�2 ,6,10,14,18,22
  for(int i=0;i<x_grid_length;i++)
  {
   Integer []hours={2,6,10,14,18,22};  //ʱ���
   int beginHour = hours[i<x_unit?i:i%x_unit];
   Color hourColor = null;
   if (beginHour == 2 || beginHour == 6 || beginHour == 22)
    hourColor = Color.RED;
   else
    hourColor = Color.BLACK;
 
   chart.addBackgroundValue(beginHour + "", grid_left + i + 0.5,bottom_length+y_grid_length+0.1, font, hourColor);
  }
  
  chart.addBackgroundValue("����", grid_left / 6 * 5, bottom_length+y_grid_length+0.1, font, Color.BLACK);
  chart.addBackgroundValue("����", grid_left / 4 + 0.2,bottom_length+y_grid_length+0.1 , font, Color.BLACK);
  
  /**
   * ���º�����֮�������
   */
  chart.addData(ChartProcessor.AXIS_LEFT,"vertical_line_between_temp_pulse", grid_left / 2,bottom_length);
  chart.addData(ChartProcessor.AXIS_LEFT,"vertical_line_between_temp_pulse", grid_left / 2,bottom_length+y_grid_length+top_row_height);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"vertical_line_between_temp_pulse", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"vertical_line_between_temp_pulse", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"vertical_line_between_temp_pulse", ChartShape.LINE_THIN);
  
  
  /******************************
   * ������̬����չʾ
   * ****************************
   */
  
  /**
   * ������Ϣ��ʾ ���ӣ�2012-07-31��08-01��02��03��04��05
   */
//  map = setData();
//  List<String> twdDateList=(List<String>)map.get("twdDateList");
  List<String> dateList=(List<String>)map.get("dateList");  //���ڼ���
  if(dateList!=null&&dateList.size()>0)
  {
   for(int i=0;i<dateList.size();i++)
   {
    chart.addBackgroundValue(dateList.get(i),grid_left*(i+1)+ x_unit / 2,bottom_length+y_grid_length+top_row_height*2+ 0.1, font); //����
   }
  }
  
  if(dateList!=null&&dateList.size()>0)
  {
//   List<Operation> operationList=(List<Operation>)map.get("operationList"); //������Ϣ���ϣ������������ڽ���洢
//   for(int i=0;i<dateList.size();i++)
//   {
//    Date  tempDate= DateUtil.parseDate(dateList.get(i).toString());  
//    long diffDays=DateUtil.getDateDiff(tempDate,DateUtil.parseDate(map.get("indate").toString()));
//    //סԺ����
//    chart.addBackgroundValue(diffDays+"",grid_left*(i+1)+ x_unit / 2,bottom_length+y_grid_length+top_row_height*4+0.1, font);
//    //����������
//    int firstOperationDay=0;  //��һ����������
//    int secondOperationDay=0; //�ڶ�����������
//    if(operationList!=null&&operationList.size()>0)
//    {
//     for(int j=0;j<operationList.size();j++)
//     {
//      if(operationList.get(j).getOperationdate()!=null)
//      {
//       int tempDiffDays=(int)DateUtil.getDateDiff(tempDate, operationList.get(j).getOperationdate());
//       if(tempDiffDays>0&&tempDiffDays<=14)
//       {
//        secondOperationDay=tempDiffDays;
//        if(j+1>=operationList.size())
//        {
//         break;
//        }
//        int tempDiffDays1=(int)DateUtils.getDateDiff(operationList.get(j).getOperationdate(), operationList.get(j+1).getOperationdate());
//        if(tempDiffDays1>0&&tempDiffDays1<=14&&operationList.get(j+1).getOperationdate()!=null)
//         firstOperationDay=(int)DateUtil.getDateDiff(tempDate, operationList.get(j+1).getOperationdate());;
//        break;
//       }
//      }
//      
//     }
//    }
//      
//    if(firstOperationDay>0)
//    {
//     chart.addBackgroundValue(secondOperationDay+"/"+firstOperationDay,grid_left*(i+1)+ x_unit / 2,bottom_length+y_grid_length+top_row_height*3+0.1, font); 
//    }
//    else if(secondOperationDay>0)
//    {
//     chart.addBackgroundValue(secondOperationDay+"",grid_left*(i+1)+ x_unit / 2,bottom_length+y_grid_length+top_row_height*3+0.1, font);
//    }
//   }
  }
  
 }
 /***
  * �м���ӳ�ʼ��
  */
 private void initGridChart(ChartProcessor chart)
 {
  //�������
  for (int i = 0; i <=x_grid_length; i++)
  {
   float chartShape = ChartShape.LINE_THIN;
   if (i % 6 == 0)
   {
    chartShape = ChartShape.LINE_WIDE;
   }
   String line_name = "basic_line_ruliang_vertical_sec_" + i;
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left + i,bottom_length);
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left + i,y_length - top_length);
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, line_name,false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.BLACK);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT, line_name, chartShape);
  }
  
  //�������
  for (int i = 0; i < grid_rows; i++)
  {
   String line_name="grid_horizontal_line"+i;
   float horizontalShape=ChartShape.LINE_THIN;
   Color horizontalColor=Color.BLACK;
   if(i%5==0)
   {
    horizontalShape=ChartShape.LINE_WIDE;
   }
   if(i==15) //37�ȶ�Ӧ��Ϊ��ɫ
   {
    horizontalColor=Color.RED;
   }
   chart.addData(ChartProcessor.AXIS_LEFT,line_name,grid_left,bottom_length+grid_row_height*i);
   chart.addData(ChartProcessor.AXIS_LEFT,line_name ,x_length-grid_right,bottom_length+grid_row_height*i);
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,line_name, false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,horizontalColor);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT,line_name,horizontalShape);
  }
 }
 
 
 
 /**
  * �ײ����ݳ�ʼ��
  */
 public void initBottomChart(ChartProcessor chart,Map map)
 {
  //�ײ����ݳ�ʼ��
  chart.addBackgroundValue("�� �� (��/��)", grid_left / 2, bottom_length- bottom_row_height, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_huxi", 0,bottom_length - bottom_row_height*2);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_huxi", x_length-grid_right,bottom_length - bottom_row_height*2);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_huxi", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_line_huxi",Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_line_huxi",ChartShape.LINE_THIN);
  chart.addBackgroundValue(" �� �� ��    ml", grid_left / 2, bottom_length- bottom_row_height * 3 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT,"basic_line_bottom_niaoliang", 0, bottom_length- bottom_row_height * 3);
  chart.addData(ChartProcessor.AXIS_LEFT,"basic_line_bottom_niaoliang", x_length-grid_right, bottom_length- bottom_row_height * 3);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_niaoliang", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_niaoliang", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_niaoliang", ChartShape.LINE_THIN);
  chart.addBackgroundValue(" �ո�Ѫ��ֵ   mmol/L", grid_left / 2, bottom_length- bottom_row_height * 4 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_ruliang",0, bottom_length - bottom_row_height * 4);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_ruliang", x_length-grid_right, bottom_length - bottom_row_height * 4);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, "basic_line_bottom_ruliang", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_line_bottom_ruliang", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_line_bottom_ruliang", ChartShape.LINE_THIN);
  chart.addBackgroundValue(" �ͺ�Ѫ��ֵ   mmol/L", grid_left / 2, bottom_length- bottom_row_height * 5 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_chuliang",0, bottom_length - bottom_row_height *5);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_chuliang",x_length-grid_right, bottom_length - bottom_row_height * 5);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_chuliang", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_chuliang", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_chuliang", ChartShape.LINE_THIN);
  chart.addBackgroundValue(" ��  ��  ��  �� ", grid_left / 2, bottom_length- bottom_row_height * 6 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT,"basic_line_bottom_dabiancishu", 0, bottom_length- bottom_row_height *6);
  chart.addData(ChartProcessor.AXIS_LEFT,"basic_line_bottom_dabiancishu", x_length-grid_right, bottom_length- bottom_row_height * 6);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_dabiancishu", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_dabiancishu", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_dabiancishu", ChartShape.LINE_THIN);
  chart.addBackgroundValue("  Ѫ��ѹ mmHg", grid_left / 2, bottom_length- bottom_row_height * 7 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_xueya", 0,bottom_length - bottom_row_height * 7);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_xueya",x_length-grid_right, bottom_length - bottom_row_height * 7);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_xueya", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_xueya", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_xueya", ChartShape.LINE_THIN);
  
  
  for (int i = 0; i < 7; i++)
  {
   String line_name = "basic_line__bottom_xueya_vertical" + i;
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left+ x_unit / 2 + x_unit * (i), bottom_length- bottom_row_height * 6);
   chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left+ x_unit / 2 + x_unit * (i), bottom_length- bottom_row_height * 7);
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, line_name,false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.black);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT, line_name,ChartShape.LINE_THIN);
  }
  
  
  chart.addBackgroundValue("�塡  ��    kg", grid_left / 2, bottom_length- bottom_row_height * 8 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_tizhong",0, bottom_length - bottom_row_height *8);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_tizhong",x_length-grid_right, bottom_length - bottom_row_height * 8);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_tizhong", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_tizhong", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_tizhong", ChartShape.LINE_THIN);
  
  chart.addBackgroundValue("��    �ߡ�  cm", grid_left / 2, bottom_length- bottom_row_height * 9 + 0.1, font);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_shenggao",0, bottom_length - bottom_row_height *9);
  chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_shenggao",x_length-grid_right, bottom_length - bottom_row_height * 9);
  chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"basic_line_bottom_shenggao", false);
  chart.setLineColor(ChartProcessor.AXIS_LEFT,"basic_line_bottom_shenggao", Color.BLACK);
  chart.setLineWidth(ChartProcessor.AXIS_LEFT,"basic_line_bottom_shenggao", ChartShape.LINE_THIN);
  
  for(int i=1;i<5;i++)
  {
   chart.addBackgroundValue("", grid_left / 2, bottom_length- bottom_row_height * (9+i) + 0.1, font);
   chart.addData(ChartProcessor.AXIS_LEFT, "basic_line_bottom_beizhu"+i,0, bottom_length - bottom_row_height *(9+i));
   chart.addData(ChartProcessor.AXIS_LEFT,  "basic_line_bottom_beizhu"+i,x_length, bottom_length - bottom_row_height * (9+i));
   chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT, "basic_line_bottom_beizhu"+i, false);
   chart.setLineColor(ChartProcessor.AXIS_LEFT, "basic_line_bottom_beizhu"+i, Color.BLACK);
   chart.setLineWidth(ChartProcessor.AXIS_LEFT, "basic_line_bottom_beizhu"+i, ChartShape.LINE_THIN);
  }
  
  
  /******************************
   * �ײ���̬����չʾ
   * ****************************
   */
  
  //begin
//  map = setData();
  List<String> dateList=(List<String>)map.get("dateList");  //���ڼ���
  List<PTempInfo> tempInfoList=(List<PTempInfo>)map.get("tempInfoList");  //���µ�����
  if(dateList != null)
  {
  for(int i=0;i<dateList.size();i++)
  {
   PTempInfo resultInfo=tempInfoList.get(i);
    if(resultInfo!=null)
     {
     chart.addBackgroundValue(resultInfo.getUrineVolume()==null?"":resultInfo.getUrineVolume()+"",grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 3 + 0.1, font); //����
     chart.addBackgroundValue(resultInfo.getKFXT()==null?"":resultInfo.getKFXT()+"",grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 4 + 0.1, font); //�ո�Ѫ��
     chart.addBackgroundValue(resultInfo.getCHXT()==null?"":resultInfo.getCHXT()+"",grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 5 + 0.1, font); //�ͺ�Ѫ��
     chart.addBackgroundValue(resultInfo.getPoopCount()==null?"":resultInfo.getPoopCount()+"",grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 6 + 0.1, font); //������
     chart.addBackgroundValue(resultInfo.getBloodPressure1()==null?"":resultInfo.getBloodPressure1()+"",grid_left*(i+1)+ 1+0.4,bottom_length- bottom_row_height * 7 + 0.1, font); //Ѫѹ1
     chart.addBackgroundValue(resultInfo.getBloodPressure2()==null?"":resultInfo.getBloodPressure2()+"",grid_left*(i+1)+ 5-0.5,bottom_length- bottom_row_height * 7 + 0.1, font); //Ѫѹ1
     chart.addBackgroundValue(resultInfo.getWeight(),grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 8 + 0.1, font); //����
     chart.addBackgroundValue(resultInfo.getHeight(),grid_left*(i+1)+ x_unit / 2,bottom_length- bottom_row_height * 9 + 0.1, font); //���
     }
  }
  }
  
  
  
 }
 /**
  * ��̬�������ݵĳ�ʼ��
  * @param chart
  * @param map
  */
 public void initGridDynamicData(ChartProcessor chart ,Map map){
  //begin
  List<String> dateList= (List<String>)map.get("dateList");  //���ڼ���
  List<PTempInfo> tempInfoList=(List<PTempInfo>)map.get("tempInfoList");  //���µ�����
  PTempInfo resultInfo=null;
  PTempDetailInfo resultDetailInfo=null;
  if(dateList != null)
  {
  for(int i=0;i<dateList.size();i++)
  {
      resultInfo=tempInfoList.get(i);
   if(resultInfo!=null)
    {
     List<PTempDetailInfo> detailInfoList=resultInfo.getDetailInfo();
     int k=0;
     for(int j=0;j<detailInfoList.size();j++)  //����ÿ�������
     {
      resultDetailInfo = detailInfoList.get(j);
      if(null == resultDetailInfo)
      {
    	  continue;
      }
       /**
        * ����  
        */
       double y_breathe_height=bottom_length- bottom_row_height+((k%2==0)?-bottom_row_height+0.1:0.1); //��ÿ�ռ�¼����2�����ϣ�Ӧ������Ӧ����Ŀ�����½����¼
//       if(resultDetailInfo.getBreathe()!=null&&"1".equals(resultDetailInfo.getBreatheType()+""))  //�ж��Ƿ�ʹ�ú�����
//       {
//        Font aril_font = new Font("����", Font.BOLD, 18);
//        chart.addBackgroundValue("R", grid_left+i*6+grid_col_width/2+j,y_breathe_height, aril_font);
        /*String line_name="grid_point_breatherType_"+i+"_"+j;
        chart.addData(ChartProcessor.AXIS_LEFT, line_name,grid_left+i*6+grid_col_width/2+j,y_breathe_height+0.1);
        chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,line_name, true);
        chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.BLACK);
        chart.setLineShape(ChartProcessor.AXIS_LEFT, line_name,ChartShape.getShape(ChartShape.ROUND, round_size+2,round_size));
        chart.setLineVisible(ChartProcessor.AXIS_LEFT, line_name,false);
        chart.setLineShapeFilledColor(ChartProcessor.AXIS_LEFT, line_name, Color.WHITE);*/
//       }
//       else if(resultDetailInfo.getBreathe()!=null)  //����
//       {
            chart.addBackgroundValue(resultDetailInfo.getBreathe()+"", grid_left+i*6+grid_col_width/2+j,y_breathe_height, font,Color.RED);
//       }
       
        double grid_temperature_height=0;  //���µ���Ӧ��y��ֵ 
        double grid_pulse_height=0; //������Ӧ��y��ֵ 
        double grid_heartRate_height=0; //���ʶ�Ӧ��y��ֵ 
        boolean temperature_pulse=false;  //���¡������غ�
        boolean pulse_heartRate=false;     //���� �������غ� 
        boolean temperature_heartRate=false; //���¡������غ� 
       if(!resultDetailInfo.getTemperature().equals(""))
       {
        grid_temperature_height=Double.valueOf(resultDetailInfo.getTemperature())-(start_temperature-bottom_length);
       }else
       {
       grid_temperature_height=Double.valueOf(34)-(start_temperature-bottom_length);
       }
       if(resultDetailInfo.getPulse()!=0)
       {
        grid_pulse_height=bottom_length+(resultDetailInfo.getPulse()-20)*0.05;
       }else
       {
           grid_pulse_height=bottom_length;
       }
       if(Math.abs(grid_temperature_height- grid_pulse_height)<0.05 )  //���� �����غ�
       {
        temperature_pulse=true;
        grid_temperature_height=grid_pulse_height;
       }
       /**
        * ���� 
        */
       if(resultDetailInfo.getTemperature()!=null && !resultDetailInfo.getTemperature().equals(""))
       {
        //����
        String line_name="grid_point_temperatures_"+i+"_"+j;
         line_name="grid_point_temperatures_kw";
         if(temperature_pulse||temperature_heartRate)
          line_name="d_grid";
         chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left+i*6+grid_col_width/2+j,grid_temperature_height);
         chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.BLUE);  
         chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,line_name, true);
         chart.setLineVisible(ChartProcessor.AXIS_LEFT, line_name,false);
            if(temperature_pulse||temperature_heartRate)
             chart.setLineShape(ChartProcessor.AXIS_LEFT, line_name,ChartShape.getShape(ChartShape.ROUND, round_size-1,round_size-1));
            else
                chart.setLineShape(ChartProcessor.AXIS_LEFT, line_name,ChartShape.getShape(ChartShape.ROUND, round_size,round_size));
             
//        //����  >>> ���²�����ʱ�� ����Ҫ����
//        if(resultDetailInfo.getTemperatureType()!=4)  
//        {
         chart.addData(ChartProcessor.AXIS_LEFT, "grid_line_temperature", grid_left+i*6+grid_col_width/2+j,grid_temperature_height);
         chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"grid_line_temperature", false);
         chart.setLineColor(ChartProcessor.AXIS_LEFT, "grid_line_temperature",Color.BLUE);
         chart.setLineWidth(ChartProcessor.AXIS_LEFT, "grid_line_temperature",ChartShape.LINE_THIN);
         chart.setLineVisible(ChartProcessor.AXIS_LEFT, "grid_line_temperature",true);
//        }
        
       }
       
       /**
        * ����
        */
       if(resultDetailInfo.getPulse() != 0)
       {
        //���� 
        String line_name="grid_point_pulse_"+i+"_"+j;
        if(temperature_pulse )  //�����������غ�
         line_name="b_grid";
        else if(pulse_heartRate)
         line_name="c_grid";  //�����������غ�
        chart.addData(ChartProcessor.AXIS_LEFT, line_name, grid_left+i*6+grid_col_width/2+j,grid_pulse_height);
        if(temperature_pulse) //�����������غ� 
        {
         chart.setLineShape(ChartProcessor.AXIS_LEFT, line_name,ChartShape.getShape(ChartShape.ROUND, round_size+3,round_size+3)); 
         chart.setLineShapeFilledColor(ChartProcessor.AXIS_LEFT, line_name,Color.WHITE);
        }
        else
        {
         chart.setLineShape(ChartProcessor.AXIS_LEFT, line_name,ChartShape.getShape(ChartShape.ROUND, round_size,round_size));
        }
        chart.setLineColor(ChartProcessor.AXIS_LEFT, line_name,Color.RED);
        chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,line_name, true);
        chart.setLineVisible(ChartProcessor.AXIS_LEFT, line_name,false);
        
        
        //����
        chart.addData(ChartProcessor.AXIS_LEFT, "grid_line_pulse", grid_left+i*6+grid_col_width/2+j,grid_pulse_height);
        chart.setLineShapeVisible(ChartProcessor.AXIS_LEFT,"grid_line_pulse", false);
        chart.setLineColor(ChartProcessor.AXIS_LEFT, "grid_line_pulse",Color.RED);
        chart.setLineWidth(ChartProcessor.AXIS_LEFT, "grid_line_pulse",ChartShape.LINE_THIN);
        chart.setLineVisible(ChartProcessor.AXIS_LEFT, "grid_line_pulse",true);
       }
//      
//       /**
//        * ��ע�͡���ע��
//        */
//       if(StringUtils.isNotEmpty(resultDetailInfo.getComment1()))
//       {
//          String comment1=TwdConstant.Comment1Type.getName(Long.parseLong(resultDetailInfo.getComment1Type()))+resultDetailInfo.getComment1();
//          String [] comment1s=new String[comment1.length()];
//          for(int b=0;b<comment1s.length;b++)
//          {
//           comment1s[b]=comment1.substring(b,b+1);
//          }
//        for(int a=0;a<comment1s.length;a++)
//        {
//         double grid_comment1_height=y_length-top_length-(a+1)*0.2+0.05;
//          chart.addBackgroundValue(comment1s[a], grid_left+i*6+grid_col_width/2+j, grid_comment1_height,new Font("����", Font.LAYOUT_NO_LIMIT_CONTEXT, 11),Color.RED);
//        }
//        
//       }
//       
//      }
     }
    }
//  }
  }
  }
//  //end
 }
 
}