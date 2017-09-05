package com.smpl.main.service;

import com.smpl.base.entity.DataMap;
import com.smpl.base.entity.Result;


import java.util.Map;

public interface QyhAccessService {
	/**
	 * 选择一条指定ID的记录
	 */
	DataMap selectQyhAccess(String corpId, String type) throws Exception;

	/**
	 * 保存一条记录
	 */
	Result insertQyhAccess(DataMap qyhAccess) throws Exception;

	/**
	 * 更新一条记录
	 */
	Result updateQyhAccess(DataMap qyhAccess) throws Exception;
}
