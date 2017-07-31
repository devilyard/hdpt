/*
 * @(#)App.java Created on Oct 22, 2012 6:02:46 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import ctd.config.ElementConfig;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class App extends ElementConfig {

	private static final long serialVersionUID = -3391898372292517796L;

	private String id;
	private String title;
	private boolean tab;
	private Map<String, Module> modules = new LinkedHashMap<String, Module>();

	public App() {

	}

	public App(Element define) {
		setDefineElement(define);
	}

	public void setDefineElement(Element define) {
		super.setDefineElement(define);
		id = define.attributeValue("id");
		title = define.attributeValue("title");
		tab = Boolean.parseBoolean(define.attributeValue("tab"));
		@SuppressWarnings("unchecked")
		List<Element> moduleEles = define.elements("module");
		for (Element ele : moduleEles) {
			Module module = new Module(this, ele);
			modules.put(module.getId(), module);
		}
	}

	public Element getDefineElement() {
		return this.define;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isTab() {
		return tab;
	}

	public void setTab(boolean tab) {
		this.tab = tab;
	}

	public Map<String, Module> getModules() {
		return modules;
	}

	public void setModules(Map<String, Module> modules) {
		this.modules = modules;
	}

	public void addModule(Module module) {
		this.modules.put(module.getId(), module);
	}

	public Module getModule(String id) {
		return this.modules.get(id);
	}

	/**
	 * @return
	 */
	public Map<String, Object> asMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("tab", tab);
		m.put("id", id);
		m.put("title", title);
		List<Map<String, Object>> modules = new ArrayList<Map<String, Object>>();
		for (Module mod : this.modules.values()) {
			modules.add(mod.asMap());
		}
		if(tab){
			m.put("children", modules);
		}else{
			m.putAll(modules.get(0));
		}
		return m;
	}
}
