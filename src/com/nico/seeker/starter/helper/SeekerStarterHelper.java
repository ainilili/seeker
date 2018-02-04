package com.nico.seeker.starter.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nico.noson.Noson;
import com.nico.noson.util.string.StringUtils;
import com.nico.seeker.http.HttpMethod;
import com.nico.seeker.plan.HarvestBean;
import com.nico.seeker.plan.SeekerTrack;
import com.nico.seeker.plan.TrackBean;
import com.nico.seeker.starter.SeekerStart;

/** 
 * 
 * @author nico
 * @version 创建时间：2017年9月3日 下午7:17:22
 */

public class SeekerStarterHelper {
	
	private SeekerStart seeker;
	
	public SeekerStarterHelper(SeekerStart seeker) {
		this.seeker = seeker;
	}
	
	public List<HarvestBean> getHarvestCollect() {
		return this.seeker.getHarvestCollect();
	} 
	
	public void runHelper(String json){
		Noson noson = Noson.parseNoson(json);
		Noson header = (Noson) noson.get("header");
		Object[] tracks = (Object[]) noson.get("tracks");
		SeekerTrack track = new SeekerTrack();
		headerHandler(header, track);
		tracksHandler(tracks, track);
		seeker.setSeekerTrack(track);
		seeker.run();
	}
	
	private void headerHandler(Noson header, SeekerTrack track){
		if(header == null) return;
		if(header.get("method").equals("get")){
			track.setHttpMethod(HttpMethod.GET);
		}else{
			track.setHttpMethod(HttpMethod.POST);
		}
		track.setSearcher(header.get("seacher").toString());
		track.setUri(header.get("uri").toString());
		Object[] params = (Object[]) header.get("params");
		if(params != null){
			Map<String, Object> map = new HashMap<String, Object>();
			for(Object param: params){
				Noson n = (Noson)param;
				map.put(n.get("key").toString(), n.get("value"));
			}
			track.setParams(map);
		}
	}
	
	private void tracksHandler(Object[] tracks, SeekerTrack track){
		if(tracks == null) return;
		List<TrackBean> list = new ArrayList<TrackBean>();
		tracksHandlerHelper(tracks, list);
		track.setTrackBeans(list);
	}
	
	private void tracksHandlerHelper(Object[] tracks, List<TrackBean> tlist){
		for(Object o: tracks){
			if(o instanceof Noson){
				Noson noson = (Noson)o;
				TrackBean tb = new TrackBean(
						StringUtils.isNotBlank(noson.get("prefix")) ? noson.get("prefix").toString() : null,
						StringUtils.isNotBlank(noson.get("paramName")) ? noson.get("paramName").toString() : null,
						StringUtils.isNotBlank(noson.get("paramValue")) ? noson.get("paramValue").toString() : null,
						noson.get("recycle") != null ? Boolean.parseBoolean(noson.get("recycle").toString()) : false,
						noson.get("reset") != null ? Boolean.parseBoolean(noson.get("reset").toString()) : false );
				if(tb.getRecycle()){
					tb.setRecord(String.valueOf(noson.get("record")));
				}
				tlist.add(tb);
				Object[] ns = null;
				if((ns = (Object[]) noson.get("trackBeans")) != null){
					List<TrackBean> dipList = new ArrayList<TrackBean>();
					tracksHandlerHelper(ns, dipList);
					tb.setTrackBeans(dipList);
				}
			}
		}
		
	}
}
