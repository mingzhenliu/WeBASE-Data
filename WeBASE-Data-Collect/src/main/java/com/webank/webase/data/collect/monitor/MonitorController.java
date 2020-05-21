/**
 * Copyright 2014-2020  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webase.data.collect.monitor;

import com.alibaba.fastjson.JSON;
import com.webank.webase.data.collect.base.code.ConstantCode;
import com.webank.webase.data.collect.base.entity.BasePageResponse;
import com.webank.webase.data.collect.base.entity.BaseResponse;
import com.webank.webase.data.collect.base.exception.BaseException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
//@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * monitor user list.
     */
    @GetMapping(value = "/userList/{groupId}")
    public BaseResponse monitorUserList(@PathVariable("groupId") Integer groupId)
        throws BaseException {
        BaseResponse response = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start monitorUserList startTime:{} groupId:{} ", startTime.toEpochMilli(),
            groupId);

        List<TbMonitor> listOfUser = monitorService.qureyMonitorUserList(groupId);
        response.setData(listOfUser);

        log.info("end monitorUserList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(response));
        return response;
    }

    /**
     * monitor interface list.
     */
    @GetMapping(value = "/interfaceList/{groupId}")
    public BaseResponse monitorInterfaceList(@PathVariable("groupId") Integer groupId,
        @RequestParam(value = "userName") String userName) throws BaseException {
        BaseResponse response = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start monitorInterfaceList startTime:{} groupId:{} ", startTime.toEpochMilli(),
            groupId);

        List<TbMonitor> listOfInterface = monitorService
            .qureyMonitorInterfaceList(groupId, userName);
        response.setData(listOfInterface);

        log.info("end monitorInterfaceList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(response));
        return response;
    }

    /**
     * monitor trans list.
     */
    @GetMapping(value = "/transList/{groupId}")
    public BaseResponse monitorTransList(@PathVariable("groupId") Integer groupId,
        @RequestParam(value = "userName", required = false) String userName,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate,
        @RequestParam(value = "interfaceName", required = false) String interfaceName)
        throws BaseException {
        Instant startTime = Instant.now();
        log.info(
            "start monitorTransList startTime:{} groupId:{} userName:{} startDate:{}"
                + " endDate:{} interfaceName:{}",
            startTime.toEpochMilli(), groupId, userName,
            startDate, endDate, interfaceName);

        BaseResponse response = monitorService
            .qureyMonitorTransList(groupId, userName, startDate, endDate, interfaceName);

        log.info("end monitorTransList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(response));
        return response;
    }

    /**
     * unusual user list.
     */
    @GetMapping(value = "/unusualUserList/{groupId}/{pageNumber}/{pageSize}")
    public BasePageResponse unusualUserList(@PathVariable("groupId") Integer groupId,
        @PathVariable("pageNumber") Integer pageNumber,
        @PathVariable("pageSize") Integer pageSize,
        @RequestParam(value = "userName", required = false) String userName)
        throws BaseException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
            "start unusualUserList startTime:{} groupId:{} pageNumber:{} pageSize:{}"
                + " userName:{}",
            startTime.toEpochMilli(), groupId, pageNumber,
            pageSize, userName);

        Integer count = monitorService.countOfUnusualUser(groupId, userName);
        if (count != null && count > 0) {
            List<UnusualUserInfo> listOfUnusualUser = monitorService
                .qureyUnusualUserList(groupId, userName, pageNumber, pageSize);
            pagesponse.setData(listOfUnusualUser);
            pagesponse.setTotalCount(count);
        }

        log.info("end unusualUserList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }

    /**
     * unusual contract list.
     */
    @GetMapping(value = "/unusualContractList/{groupId}/{pageNumber}/{pageSize}")
    public BasePageResponse unusualContractList(@PathVariable("groupId") Integer groupId,
        @PathVariable("pageNumber") Integer pageNumber,
        @PathVariable("pageSize") Integer pageSize,
        @RequestParam(value = "contractAddress", required = false) String contractAddress)
        throws BaseException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
            "start unusualContractList startTime:{} groupId:{} pageNumber:{}"
                + " pageSize:{} contractAddress:{}",
            startTime.toEpochMilli(), groupId, pageNumber,
            pageSize, contractAddress);

        Integer count = monitorService.countOfUnusualContract(groupId, contractAddress);
        if (count != null && count > 0) {
            List<UnusualContractInfo> listOfUnusualContract = monitorService
                .qureyUnusualContractList(groupId, contractAddress, pageNumber, pageSize);
            pagesponse.setData(listOfUnusualContract);
            pagesponse.setTotalCount(count);
        }

        log.info("end unusualContractList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }
}
