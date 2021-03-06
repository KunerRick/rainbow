package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.core.util.IdUtil;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.AddContactMessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ContactBriefDTO;
import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.MessageDTO;
import com.milchstrabe.rainbow.biz.domain.vo.MessageVO;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ch3ng
 * @Date 2020/4/29 15:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/contact")
public class AddContactRequestController {


    @Autowired
    private IContactService contactService;


    @PutMapping(path = APIVersion.V_1 + "/add/request")
    public Result addContactRequest(@CurrentUser RequestUser user, @RequestBody MessageVO message) throws LogicException {

        //warp messageDTO
        MessageDTO<AddContactMessageDTO> dto = new MessageDTO<>();
        BeanUtils.copyProperties(message,dto);
        dto.setId(IdUtil.objectId());
        dto.setDate(System.currentTimeMillis());
        dto.setSender(user.getUserId());


        //warp sender ContactBriefDTO
        ContactBriefDTO senderContactBriefDTO = new ContactBriefDTO();
        BeanUtils.copyProperties(message.getContent().getSender(), senderContactBriefDTO);
        //warp receiver ContactBriefDTO
        ContactBriefDTO receiverContactBriefDTO = new ContactBriefDTO();
        BeanUtils.copyProperties(message.getContent().getReceiver(), receiverContactBriefDTO);
        //warp AddContactMessageDTO
        AddContactMessageDTO build = AddContactMessageDTO.builder()
                .sender(senderContactBriefDTO)
                .receiver(receiverContactBriefDTO)
                .status((short) 0)
                .note(message.getContent().getNote())
                .build();


        dto.setContent(build);

        GetContactDetailDTO getContactDetailDTO = contactService.addContactMessage(dto);
        return ResultBuilder.success(getContactDetailDTO);
    }
}
