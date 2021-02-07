package com.github.huoyu820125.idregion.client;

import com.alibaba.fastjson.JSONObject;
import com.github.huoyu820125.idregion.domin.ProposeDataDTO;
import com.github.huoyu820125.idregion.domin.RegisterResultDTO;
import com.github.huoyu820125.idstar.http.Http;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title MasterClient
 * @Athor SunQian
 * @CreateTime 2021/2/3 15:18
 * @Description: todo
 */
public class MasterClient {
    private final Logger log = LoggerFactory.getLogger(getClass());

    String endpoint;

    public MasterClient(String address) {
        this.endpoint = "http://" + address;
    }

    /**
     * @title: 结点注册
     * @author: SunQian
     * @date: 2021/2/2 17:03
     * @descritpion: todo
     * @param address  结点地址
     * @param nodeId   结点id,集群内已存在相同id时，拒绝注册
     * @return 结点在集群内的id，最多4个结点，结点已满时，拒绝注册return null
     */
    public RegisterResultDTO nodeRegister(String address, Integer nodeId) {
        Http http = new Http();
        String response = http.addUriParam("address", address)
                .addUriParam("nodeId", nodeId)
                .post(endpoint + "/idstar/master/node/register", 1000)
                .response();

        JSONObject json = JSONObject.parseObject(response);
        return json.toJavaObject(RegisterResultDTO.class);
    }
}
