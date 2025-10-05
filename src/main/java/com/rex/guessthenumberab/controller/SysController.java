package com.rex.guessthenumberab.controller;

import com.rex.guessthenumberab.domain.LineUserRequest;
import com.rex.guessthenumberab.model.GameMaster;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;
import com.rex.guessthenumberab.response.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/line/guess/sys")
public class SysController {


    @Resource
    Room room;

    @Resource
    private RedisService redisService;


    //
}
