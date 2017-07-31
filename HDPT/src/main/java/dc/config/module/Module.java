/*
 * @(#)Module.java Created on Oct 22, 2012 5:44:54 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import wl.zs.common.Bean;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class Module implements Serializable, Bean {

	private static final long serialVersionUID = 1609585134132216715L;

	private App app;
	private String id;
	private String title;
	private String script;
	private String entryName;
	private String docEntryName;
	private boolean titleBar;

	public Module() {

	}

	public Module(App app, Element define) {
		this.app = app;
		if (define == null) {
			return;
		}
		id = define.attributeValue("id");
		title = define.attributeValue("title", "未命名");
		script = define.attributeValue("script");
		entryName = define.attributeValue("entryName");
		docEntryName = define.attributeValue("docEntryName", entryName + "_Doc");
		titleBar = Boolean.parseBoolean(define.attributeValue("titleBar", "true"));
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getFullId() {
		return this.app.getId() + "." + this.id;
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

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getDocEntryName() {
		return docEntryName;
	}

	public void setDocEntryName(String docEntryName) {
		this.docEntryName = docEntryName;
	}

	public boolean isTitleBar() {
		return titleBar;
	}

	public void setTitleBar(boolean titleBar) {
		this.titleBar = titleBar;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> m = new HashMap<String, Object>();
	    m.put("docEntryName", docEntryName);
	    m.put("entryName", entryName);
	    m.put("id", id);
	    m.put("title", title);
	    m.put("script", script);
	    m.put("titleBar", titleBar);
	    return m;
	}
	
}
