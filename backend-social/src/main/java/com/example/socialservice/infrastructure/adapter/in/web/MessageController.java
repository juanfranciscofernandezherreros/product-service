package com.example.socialservice.infrastructure.adapter.in.web;

import com.example.socialservice.application.port.in.SocialCommands.CreateRequestCommand;
import com.example.socialservice.application.port.in.SocialCommands.SendMessageCommand;
import com.example.socialservice.application.port.in.SocialUseCases;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.MessageResponse;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.MessageSendRequest;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.RequestCreateRequest;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.RequestResponse;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.RespondRequest;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for messages and message requests under {@code /v1/social}.
 */
@RestController
@RequestMapping("/v1/social")
public class MessageController {

    private final SocialUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases social use cases
     */
    public MessageController(final SocialUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Sends a private message.
     *
     * @param request payload
     * @return 201 with the message
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody final MessageSendRequest request) {
        final Message message = useCases.sendMessage(new SendMessageCommand(
                request.remitente(), request.destinatario(), request.contenido()));
        return ResponseEntity.created(URI.create("/v1/social/messages/" + message.getId()))
                .body(SocialWebMapper.toResponse(message));
    }

    /**
     * Creates a message request.
     *
     * @param request payload
     * @return 201 with the request
     */
    @PostMapping("/message-requests")
    public ResponseEntity<RequestResponse> createRequest(
            @Valid @RequestBody final RequestCreateRequest request) {
        final MessageRequest created = useCases.createRequest(new CreateRequestCommand(
                request.remitente(), request.destinatario(), request.mensajeInicial()));
        return ResponseEntity.created(URI.create("/v1/social/message-requests/" + created.getId()))
                .body(SocialWebMapper.toResponse(created));
    }

    /**
     * Responds to a message request.
     *
     * @param id      request id
     * @param request payload
     * @return 200 with the request
     */
    @PutMapping("/message-requests/{id}")
    public RequestResponse respond(@PathVariable final long id, @Valid @RequestBody final RespondRequest request) {
        return SocialWebMapper.toResponse(useCases.respondRequest(id, request.estado()));
    }
}
