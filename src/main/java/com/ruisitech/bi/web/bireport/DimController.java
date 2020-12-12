package com.ruisitech.bi.web.bireport;

import com.ruisitech.bi.entity.bireport.ParamDto;
import com.ruisitech.bi.entity.model.Dimension;
import com.ruisitech.bi.service.bireport.OlapService;
import com.ruisitech.bi.service.model.DimensionService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/bireport")
public class DimController extends BaseController {
	
	@Autowired
	private OlapService service;
	
	@Autowired
	private DimensionService dimService;

	@RequestMapping(value="/queryDims.action")
	public @ResponseBody
    Object queryDims(Integer cubeId){
		return service.listDims(cubeId);
	}
	
	@RequestMapping(value="/paramFilter.action")
	public @ResponseBody
	Object paramFilter(ParamDto param) throws Exception{
		Dimension d = dimService.getDimInfo(param.getId(), param.getCubeId());
		List<Map<String, Object>> ls = service.paramFilter(d, null, param.getDsid());
		Map<String, Object> ret = new HashMap<>();
		ret.put("datas", ls);
		if(d.getType().equals("month") || d.getType().equals("day")){
			ret.put("st", param.getSt());
			ret.put("end", param.getEnd());
		}
		return super.buildSucces(ret);
	}
	
	@RequestMapping(value="/paramSearch.action")
	public String paramSearch(ParamDto param, String keyword, ModelMap model) throws Exception{
		
		Dimension d = dimService.getDimInfo(param.getId(), param.getCubeId());
		List<Map<String, Object>> ls = service.paramFilter(d, keyword, param.getDsid());
		model.addAttribute("datas", ls);
		model.addAttribute("dimType", d.getType());
		model.addAttribute("vals", param.getVals());
		model.addAttribute("dimId", param.getId());
		return "bireport/DimFilter-search";
	}
	
	@RequestMapping(value="/DimFilter.action")
	public String dimFilter(ParamDto param, ModelMap model) throws Exception{
		Dimension d = dimService.getDimInfo(param.getId(), param.getCubeId());
		List<Map<String, Object>> ls = service.paramFilter(d, null, param.getDsid());
		model.addAttribute("datas", ls);
		model.addAttribute("dimType", d.getType());
		model.addAttribute("vals", param.getVals());
		model.addAttribute("dimId", param.getId());
		if(d.getType().equals("month") || d.getType().equals("day")){
			model.addAttribute("st", param.getSt());
			model.addAttribute("end", param.getEnd());
		}
		model.addAttribute("filtertype", param.getFiltertype());
		model.addAttribute("dateformat", param.getDateformat());
		return "bireport/DimFilter";
	}
}
