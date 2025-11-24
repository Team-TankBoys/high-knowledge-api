package com.tankboy.highknowledgeapi.domain.post.presentation.dto.request;

public record PostCreateRequest(
        // record 사용법: 일반 class 객체의 필드를 선언하듯
        //  이 소괄호 안에 필드를 선언하면 된다.

        String title

        // 이렇게 선언한 필드는 아래와 같이 사용(참조)할 수 있음
        // PostCreateRequest request = new PostCreateRequest("title")
        // request.title()
) {
}
