package org.fxtravel.fxspringboot.controller;

import org.fxtravel.fxspringboot.mapper.TestMapper;
import org.fxtravel.fxspringboot.pojo.dto.TestDTO;
import org.fxtravel.fxspringboot.pojo.entities.test_obj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestMapper testMapper;

    @RequestMapping("/insert")
    public String insert(@RequestBody TestDTO dto) {
        return testMapper.insert(new test_obj(dto.getId(), dto.getStringValue())) > 0 ? "success" : "failed";
    }

    @RequestMapping("/selectAll")
    public List<test_obj> selectAll() {
        return testMapper.selectList(null);
    }

    @RequestMapping("/selectByStr")
    public List<test_obj> selectByStr(String str) {
        return testMapper.selectByStr(str);
    }
}
