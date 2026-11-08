package com.example.socialservice.infrastructure.adapter.in.web;

import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.MessageResponse;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.ProfileResponse;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.RequestResponse;

/**
 * Maps domain models to web responses.
 */
public final class SocialWebMapper {

    private SocialWebMapper() {
    }

    /** @param p profile @return response */
    public static ProfileResponse toResponse(final PublicProfile p) {
        return new ProfileResponse(p.getUserId(), p.getAlias(), p.getCreadoEn(), p.getBiografia(),
                p.getAvatarEmoji());
    }

    /** @param m message @return response */
    public static MessageResponse toResponse(final Message m) {
        return new MessageResponse(m.getId(), m.getRemitente(), m.getDestinatario(), m.getContenido(),
                m.getCreadoEn());
    }

    /** @param r request @return response */
    public static RequestResponse toResponse(final MessageRequest r) {
        return new RequestResponse(r.getId(), r.getRemitente(), r.getDestinatario(), r.getMensajeInicial(),
                r.getEstado(), r.getCreadoEn(), r.getRespondidoEn());
    }
}
