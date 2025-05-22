ARG IMAGE_NAME

# Dockerfile
FROM $IMAGE_NAME
MAINTAINER xxx

ARG DOCKER_NAME
COPY $DOCKER_NAME /opt/xxx/app

RUN chown -R xxx
USER xxx

WORKDIR xxx

ENTRYPOINT["sh","./xxx/start.sh"]