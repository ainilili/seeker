package com.nico.seeker.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.nico.seeker.dom.DomBean;
import com.nico.seeker.dom.DomHelper;
import com.nico.seeker.scan.SeekerScanner;
import com.nico.seeker.scan.impl.NicoScanner;
import com.nico.seeker.searcher.SeekerSearcher;
import com.nico.seeker.searcher.impl.NicoSearcher;
import com.nico.seeker.stream.NioUtils;

public class TestSeeker {
	
	public static void main(String[] args) {
		try {
		String dom = NioUtils.readFileToString(new File("E:\\test\\test.xml"));
		SeekerScanner scan = new NicoScanner(dom);
		System.out.println(scan.getDomBeans());
		SeekerSearcher ns = new NicoSearcher(scan);
		StringBuffer sb = new StringBuffer();
		DomHelper.viewHelper(sb, ns.getResults(), 0);
		System.out.println("写入");
		File f = new File("E://test/out2.html");
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//DomHelper.viewHelper(ns.getDomBeans(), 0);
		/*List<DomBean> domBeans = ns.searchingCollect("script", null, null);
		System.out.println(domBeans.size());
		for(DomBean db : domBeans){
			System.out.println(db.getParam("src"));
		}*/
		
		/*System.out.println(domBean.getPrefix());
		System.out.println(domBean.getParam("class"));
		System.out.println(domBean.getParamStr());
		System.out.println(domBean.getBody());*/
		/*DomBean domBean = ns.searchingById("instrument-checkbox");
		//ns.setDomBean(domBean);
		domBean = ns.searchingUnique(null, "class", "panel_checked_selected");
		System.out.println(domBean.getChild(0).getParam("type"));
		
		List<DomBean> domBeans = ns.searchingCollect("div", "class", "instrumentPanel-xin-warning");
		System.out.println(domBeans.size());
		domBean = domBeans.get(0);
		System.out.println("子元素:   " + domBean.getDomProcessers().size());
		System.out.println("前缀:     " + domBean.getPrefix());
		System.out.println("是否自闭: " + domBean.isSelfSealing());
		System.out.println("标签属性: " + domBean.getParamStr());*/
		
		
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
