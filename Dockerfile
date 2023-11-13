FROM --platform=linux/amd64 openjdk:17
EXPOSE 8080
ADD target/shopService.jar shopService.jar
CMD ["sh","-c","java -jar /shopService.jar"]