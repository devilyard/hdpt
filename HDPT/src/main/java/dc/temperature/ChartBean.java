package dc.temperature;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.data.xy.XYSeries;

/***
 * Chartʵ����
*/
public class ChartBean 
{
 private XYSeries series;
 private int index; //�ߵ�����
 private Shape shape;//���ϵ����״
 private Color shapeColor;// ���ϵ����ɫ
 private Boolean shapeFilled; //�Ƿ�����
 private Color lineColor;//�ߵ���ɫ
 private Boolean lineVisible; //���Ƿ�ɼ�
 private Boolean shapeVisible; //���ϵ��Ƿ�ɼ�
 private Boolean lineDash; //���Ƿ�������
 private Float lineWidth; //�ߵĿ��
 
 public int getIndex() {
  return index;
 }
 public void setIndex(int index) {
  this.index = index;
 }
 public XYSeries getSeries() {
  return series;
 }
 public void setSeries(XYSeries series) {
  this.series = series;
 }
 public Shape getShape() {
  return shape;
 }
 public void setShape(Shape shape) {
  this.shape = shape;
 }
 public Color getShapeColor() {
  return shapeColor;
 }
 public void setShapeColor(Color shapeColor) 
 {
  this.shapeColor = shapeColor;
 }
 public Color getLineColor() 
 {
  return lineColor;
 }
 public void setLineColor(Color lineColor) {
  this.lineColor = lineColor;
 }
 public Boolean getShapeVisible() 
 {
  if(this.shapeVisible == null)
   return true;
  return shapeVisible;
 }
 public void setShapeVisible(Boolean shapeVisible) 
 {
  this.shapeVisible = shapeVisible;
 }
 public Boolean getShapeFilled() 
 {
  if(shapeFilled == null)
   return false;
  return shapeFilled;
 }
 public void setShapeFilled(Boolean shapeFilled) {
  this.shapeFilled = shapeFilled;
 }
 public Boolean getLineVisible() {
  return lineVisible;
 }
 public void setLineVisible(Boolean lineVisible) {
  this.lineVisible = lineVisible;
 }
 public Boolean getLineDash() {
  return lineDash;
 }
 public void setLineDash(Boolean lineDash) {
  this.lineDash = lineDash;
 }
 public Float getLineWidth() {
  return lineWidth;
 }
 public void setLineWidth(Float lineWidth) {
  this.lineWidth = lineWidth;
 }
 
 /**
  * ����ߵıʴ�
  * @return
  */
 public Stroke getLineStroke() 
 {
  if(this.lineWidth == null)
   lineWidth = ChartShape.LINE_THIN;
  if(this.lineDash == null)
  {
   lineDash = new Boolean(false);
  }
  
  return ChartShape.getLineStoke(lineWidth, this.lineDash);
 }
}