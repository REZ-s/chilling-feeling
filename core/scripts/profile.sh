#!/usr/bin/env bash

# bash 는 value return 이 안되므로 echo로 결과 출력후, 클라이언트에서 값을 사용한다.

function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/environment/profile)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=live2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/environment/profile)
    fi

    if [ ${CURRENT_PROFILE} == live1 ]
    then
      IDLE_PROFILE=live2
    else
      IDLE_PROFILE=live1
    fi

    echo "${IDLE_PROFILE}"
}

function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == live1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}