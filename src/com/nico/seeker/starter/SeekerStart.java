package com.nico.seeker.starter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.nico.seeker.dom.DomBean;
import com.nico.seeker.http.HttpMethod;
import com.nico.seeker.plan.HarvestBean;
import com.nico.seeker.plan.SeekerTrack;
import com.nico.seeker.plan.TrackBean;
import com.nico.seeker.plan.TrackBeanType;
import com.nico.seeker.scan.SeekerScanner;
import com.nico.seeker.scan.impl.NicoScanner;
import com.nico.seeker.searcher.SeekerSearcher;

/** 
 * 
 * @author nico
 * @version 创建时间：2017年9月2日 下午10:00:01
 */

public class SeekerStart {
	
	private SeekerTrack seekerTrack;
	
	private SeekerSearcher searcher;
	
	public SeekerStart(SeekerTrack seekerTrack){
		this.seekerTrack = seekerTrack;
	}
	
	public SeekerStart() {}
	
	public void run(){
		SeekerScanner scan = new NicoScanner(seekerTrack.getUri(), seekerTrack.getHttpMethod(), seekerTrack.getParams());
		try {
			Class<?> clazz = Class.forName(seekerTrack.getSearcher());
			Constructor<?> csr = clazz.getConstructor(SeekerScanner.class) ;
			SeekerSearcher searcher = (SeekerSearcher) csr.newInstance(scan);
			excute(searcher, seekerTrack.getTrackBeans(), seekerTrack.getHarvestCollect());
			this.searcher = searcher;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void excute(SeekerSearcher searcher, List<TrackBean> trackBeans, List<HarvestBean> harvestCollect){
		if(trackBeans == null) return;
		Iterator<TrackBean> items = trackBeans.iterator();
		if(!items.hasNext()){
			harvestCollect.add(new HarvestBean(searcher.getResults(), "默认查询页面所有数据"));
		}else{
			while(items.hasNext()){
				TrackBean trackBean = items.next();
				searcher.searching(trackBean.getPrefix(), trackBean.getParamName(), trackBean.getParamValue());
				if(trackBean.getRecycle()){
					harvestCollect.add(new HarvestBean(searcher.getResults(), trackBean.getRecord()));
				}
				if(trackBean.getReset()){
					searcher.reset();
				}
				if(trackBean.getTrackBeans() != null){
					excute(searcher, trackBean.getTrackBeans(), harvestCollect);
				}
			}
		}
	}

	public SeekerTrack getSeekerTrack() {
		return seekerTrack;
	}

	public void setSeekerTrack(SeekerTrack seekerTrack) {
		this.seekerTrack = seekerTrack;
	}
	
	public List<HarvestBean> getHarvestCollect() {
		return this.seekerTrack.getHarvestCollect();
	}

	public SeekerSearcher getSearcher() {
		return searcher;
	}

	public void setSearcher(SeekerSearcher searcher) {
		this.searcher = searcher;
	}
	
	
}
