package com.sq.idstar.service;

import com.sq.idstar.service.nbrestful.sdk.NBRestul;
import org.springframework.stereotype.Service;

/**
 * @author sq
 * @version 1.0
 * @className DefaultRegionProvider
 * @description 默认的id地区提供者
 * @date 2019/4/24 下午4:08
 */
@Service(value = "defaultRegionProvider")
public class DefaultRegionProvider implements IRegionProvider {

    /**
     * author: SunQian
     * date: 2019/5/23 15:25
     * title: 取一个无人区区号
     * descritpion: TODO
     * @param raceNo 种族编号
     * return: 区号
     */
    @Override
    public Long noManRegionNo(Integer raceNo) {
        NBRestul nbRestul = new NBRestul();
        return nbRestul.addUriVariables("version", raceNo)
                .get("id-page", Long.class, "idstar/region/noman");
    }

}
