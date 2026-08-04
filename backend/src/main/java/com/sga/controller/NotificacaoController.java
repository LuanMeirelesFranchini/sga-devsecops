package com.sga.controller;

import com.sga.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final SseService sseService;

    public NotificacaoController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/notificacoes/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return sseService.addEmitter();
    }
}
