package com.smpl.main.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import com.smpl.base.entity.DataMap;
import com.smpl.base.entity.Result;
import com.smpl.base.mapper.BaseMapper;
import com.smpl.main.service.QyhAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QyhAccessServiceImpl implements QyhAccessService {
	@Autowired
	private BaseMapper baseMapper;
	@Override
	public DataMap selectQyhAccess(String corpId, String type) throws Exception {
		DataMap args = new DataMap();
		args.put("corpId", corpId);
		args.put("type", type);
		DataMap qyhAccess = baseMapper.findById(args);
		return qyhAccess;
	}

	@Override
	public Result insertQyhAccess(DataMap qyhAccess) throws Exception{
		Integer flag = baseMapper.insert(qyhAccess);
		if(flag == 1){
			return Result.succeed("操作成功！");
		}else{
			return Result.falied("操作失败，请重试！");
		}
	}

	@Override
	public Result updateQyhAccess(DataMap qyhAccess) throws Exception{
		DataMap args = new DataMap();
		args.put("CORP_ID",qyhAccess.get("CORP_ID"));
		args.put("TYPE",qyhAccess.get("TYPE"));
		args.put("VALUE",qyhAccess.get("VALUE"));
		args.put("BEGIN_TIME",qyhAccess.get("BEGIN_TIME"));
		args.put("GROUP_ID",qyhAccess.get("GROUP_ID"));
		int flag = baseMapper.update(args);
		if(flag == 1){
			return Result.succeed("操作成功！");
		}else{
			return Result.falied("操作失败，请重试！");
		}
	}
}
