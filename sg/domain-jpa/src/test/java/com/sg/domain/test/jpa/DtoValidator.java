/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.test.jpa;

import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import javax.validation.Valid;

/**
 *
 * @author tarasev
 */
public class DtoValidator {

    public void validate(@Valid SignupDto dto) {
    }

    public void validate(@Valid CompleteSignupDto dto) {
    }

    public void validate(@Valid SigninDto dto) {
    }

    public void validate(@Valid ThreadCreateDto dto) {
    }

    public void validate(@Valid CanvasCreateDto dto) {
    }

    public void validate(@Valid ThreadDeleteDto dto) {
    }

    public void validate(@Valid CanvasDeleteDto dto) {
    }

    public void validate(@Valid ThreadUpdateDto dto) {
    }

    public void validate(@Valid CanvasUpdateDto dto) {
    }
}
